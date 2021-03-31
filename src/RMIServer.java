import java.io.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.*;
import java.net.*;

import static java.lang.Thread.sleep;

public class RMIServer extends UnicastRemoteObject implements RMI_S_Interface {

	public ArrayList<Eleicao> eleicoes = new ArrayList<>();
	public ArrayList<Pessoa> pessoas = new ArrayList<>();
	public ArrayList<MesaVoto> mesas = new ArrayList<>();
	public ArrayList<Resultado> resultados = new ArrayList<>();

	public RMIServer() throws RemoteException {
		super();
		load();
	}

	public static void main(String args[]) {

		if(args.length!=1){
			System.out.println("Bad arguments. run java RMIServer {RMIHostIP}");
			System.exit(1);
		}

		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());

		try {
			Registry r = LocateRegistry.getRegistry(args[0],7040);
			RMI_S_Interface servidorPrincipal = (RMI_S_Interface) r.lookup("ServidorRMI");
			System.out.println("Servidor RMI Secundario em execucao.");
			int failed = 0;
			int toRecover = 3;
			int frequency = 10;
			while (failed < toRecover) {
				sleep(frequency);
				try {
					servidorPrincipal.ping();
				} catch (RemoteException e) {
					failed++;
					System.out.println("Heartbeat falhou." + failed + "/" + toRecover);
				}
			}
			servidorPrincipal(args[0]);
		}catch (NotBoundException e) {
			System.out.println("Objeto servidorRMI nao eiste em RMI.");
			servidorPrincipal(args[0]);
		}catch (RemoteException e){
			System.out.println("RMI nao existe.");
			servidorPrincipal(args[0]);
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		finally {
			System.exit(1);
		}
	}

	public static void servidorPrincipal(String RMIHostIP){
		try {
			System.out.println("Servidor RMI Principal em execucao.");
			System.setProperty("java.rmi.server.hostname", RMIHostIP);
			Registry r = LocateRegistry.createRegistry(7040);
			System.out.println(r.toString());
			RMIServer servidor = new RMIServer();
			r.rebind("ServidorRMI", servidor);

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter para encerrar servidor.");
			String espera=reader.readLine();
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}


	public String addPessoa(String nome, String password, Departamento departamento, String telefone, String morada,String numberCC, GregorianCalendar expireCCDate, Profissao profissao) throws RemoteException {
		if(getPessoaByCC(numberCC)==null){
			Pessoa pessoa = new Pessoa(nome, password, departamento, telefone, morada, numberCC, expireCCDate, profissao);
			this.pessoas.add(pessoa);
			save("pessoas");
			return nome+"("+numberCC+") adicionado.";
		}
		else {
			return nome + "(" + numberCC + ") jA existe.";
		}
	}

	public String addEleicao(String titulo, String descricao,GregorianCalendar dataInicio, GregorianCalendar dataFim, ArrayList<Profissao> profissoes, ArrayList<Departamento> departamentos) throws RemoteException {
		if(getEleicaoByName(titulo)==null){
			Eleicao eleicao = new Eleicao(dataInicio, dataFim, titulo, descricao, profissoes, departamentos);
			this.eleicoes.add(eleicao);
			save("eleicoes");
			return titulo + " adicionada.";
		}
		else{
			return titulo + " ja existe.";
		}
	}

	public String editEleicao(String tituloAntigo,String tituloNovo, String descricaoNova,GregorianCalendar dataInicio,GregorianCalendar dataFim) throws java.rmi.RemoteException{
		Eleicao escolhida=getEleicaoByName(tituloAntigo);
		if(escolhida==null || getEleicaoByName(tituloNovo)!=null){
			return tituloAntigo + " nao existe ou titulo novo ja em uso.";
		}
		String status=escolhida.editDados(tituloNovo,descricaoNova,dataInicio,dataFim);
		save("eleicoes");
		return status;
	}

	public String addMesa(Departamento departamento, ArrayList<Pessoa> membros, String ip, String port) throws RemoteException {
		if(getMesaByDepartamento(departamento.name())== null && getMesaByMulticastGroup(ip,port)==null){
			MesaVoto mesa = new MesaVoto(departamento, membros, ip, port);
			this.mesas.add(mesa);
			save("mesas");
			return departamento+" adicionada.";
		}
		else{
			return departamento + " ou grupo Multicast ja existe.";
		}
	}

	public String addMesaEleicao(String nomeMesa,String nomeEleicao) throws  RemoteException{
		MesaVoto mesa=getMesaByDepartamento(nomeMesa);
		Eleicao ele=getEleicaoByName(nomeEleicao);
		if(mesa==null){
			return "Mesa nao existe.";
		}
		if(ele==null){
			return "Eleicao nao existe.";
		}
		String status= ele.addMesa(mesa);
		save("eleicoes");
		return status;
	}

	public String editMesa(String nomeMesa,String membro1,String membro2,String membro3) throws RemoteException{
		MesaVoto mesa=getMesaByDepartamento(nomeMesa);
		if(mesa==null){
			return "Mesa nao existe.";
		}
		ArrayList<Pessoa> membros=new ArrayList<Pessoa>();
		Pessoa p1=new Pessoa(membro1,membro1,Departamento.DA,"-1","-1","-1", new GregorianCalendar(),Profissao.Estudante);
		Pessoa p2=new Pessoa(membro2,membro2,Departamento.DA,"-1","-1","-1", new GregorianCalendar(),Profissao.Estudante);
		Pessoa p3=new Pessoa(membro3,membro3,Departamento.DA,"-1","-1","-1", new GregorianCalendar(),Profissao.Estudante);
		membros.add(p1);
		membros.add(p2);
		membros.add(p3);
		mesa.setMembros(membros);
		save("mesas");
		return "Membros de Mesa alterados.";
	}

	public String addLista(String nomeEleicao,String nomeLista, ArrayList<Pessoa> listaPessoas, Profissao tipoLista) throws RemoteException {
		Eleicao election=getEleicaoByName(nomeEleicao);
		if(election==null){
			return nomeEleicao+" nao existe.";
		}

		Lista lista = new Lista(listaPessoas, tipoLista, nomeLista);
		String status= election.addLista(lista);
		save("eleicoes");
		return status;

	}

	public void removeLista(Eleicao eleicao, String nome) throws RemoteException {
		eleicao.removeLista(nome);
	}

	public String adicionarVoto(String nomeEleicao, Voto voto, String nomeLista) throws RemoteException {
		Eleicao ele = getEleicaoByName(nomeEleicao);

		String tipo="";

		Lista candidato = ele.getListaByName(nomeLista);
		if (candidato != null){
			tipo = "Valido";
		}else if(nomeLista.equals("Branco")){
			tipo= "Branco";
		}else{
			tipo = "Nulo";
		}

		boolean hasVoted = ele.addVoto(voto, nomeLista, tipo);

		if (hasVoted){
			return "true | Voto com Sucesso";
		}
		return "false | Voto não aceite";
	}

	public String addVotoAntecipado(String numeroCC,String password,String nomeEleicao,String nomeLista) throws RemoteException {
		Pessoa p=getPessoaByCC(numeroCC);
		Eleicao ele =getEleicaoByName(nomeEleicao);
		if(p==null || !(p.getPassword().equals(password))){
			return "Pessoa ou password incorreta.";
		}
		if(ele==null){
			return "Eleicao ("+ele.getTitulo()+") nao existe.";
		}
		Voto v=new Voto(p,new GregorianCalendar());
		String tipo="";

		Lista candidato = ele.getListaByName(nomeLista);
		if (candidato != null){
			tipo = "Valido";
		}else if(nomeLista.equals("Branco")){
			tipo= "Branco";
		}else{
			tipo = "Nulo";
		}

		String status=ele.addVotoAntecipado(v,nomeLista,tipo);
		save("eleicoes");
		return status;
	}

	public Resultado getResultados(String nomeEleicao) throws RemoteException{
		Eleicao ele=getEleicaoByName(nomeEleicao);

		if(ele==null){
			return null;
		}
		return ele.getResultados();
	}

	public Voto getVoto(String numeroCC, String nomeEleicao) throws RemoteException {
		Pessoa p=getPessoaByCC(numeroCC);
		Eleicao ele=getEleicaoByName(nomeEleicao);

		if(p==null){
			return null;
		}
		if(ele==null){
			return null;
		}
		return ele.getVotoByCC(numeroCC);
	}

	public Pessoa getPessoaByCC(String numberCC) throws java.rmi.RemoteException{
		for (Pessoa pessoa : pessoas){
			if (pessoa.getNumberCC().equals(numberCC))
				return pessoa;
		}
		return null;
	}
	public Eleicao getEleicaoByName(String nome){
		for (Eleicao eleicao : eleicoes){
			if (eleicao.getTitulo().equals(nome))
				return eleicao;
		}
		return null;
	}

	public MesaVoto getMesaByDepartamento(String dep){
		for(MesaVoto mesa: mesas){
			if(mesa.getDepartamento().name().equals(dep)){
				return mesa;
			}
		}
		return null;
	}

	public MesaVoto getMesaByMulticastGroup(String ip,String port) throws java.rmi.RemoteException{
		for(MesaVoto mesa: mesas){
			if(mesa.getIp().equals(ip) && mesa.getPort().equals(port)){
				return mesa;
			}
		}
		return null;
	}

	public Lista getListaByName(String eleicao,String nome){
		Eleicao election=getEleicaoByName(eleicao);
		if(election==null){
			return null;
		}
		for(Lista lista:election.getListas()){
			if(lista.getNome().equals(nome)){
				return lista;
			}
		}
		return null;
	}

	public ArrayList<Resultado> getResultados() throws RemoteException {
		return this.resultados;
	}

	public ArrayList<Pessoa> getMembrosMesa(Departamento departamento) throws RemoteException {

		for (MesaVoto mesa : mesas){
			if (mesa.getDepartamento() == departamento){
				return mesa.getMembros();
			}
		}
		return null;
	}

	public ArrayList<MesaVoto> getMesasByStatus(boolean status){

		ArrayList<MesaVoto> aux = new ArrayList<>();

		for (MesaVoto mesa : mesas) {
			if (mesa.isStatus() == status)
				aux.add(mesa);
		}
		return aux;
	}

	public void setMembrosMesa(Departamento departamento, ArrayList<Pessoa> membros) throws RemoteException {

		for (MesaVoto mesa : mesas){
			if (mesa.getDepartamento() == departamento){
				mesa.setMembros(membros);
				return;
			}
		}
	}

	public ArrayList<Pessoa> listPessoas() throws  RemoteException{
		return pessoas;
	}

	public ArrayList<Eleicao> listEleicoes() throws  RemoteException{
		return eleicoes;
	}

	public ArrayList<Eleicao> listEleicoes(MesaVoto mesa) throws  RemoteException{
		ArrayList<Eleicao> lista = new ArrayList<>();
		Departamento departamento = mesa.getDepartamento();
		for(Eleicao eleicao: this.eleicoes){
			if(eleicao.getMesaVotoByDepartamento(departamento) != null)
				lista.add(eleicao);
		}
		return lista;
	}

	public ArrayList<Lista> listListas(String nomeEleicao) throws  RemoteException {
		Eleicao escolhida=getEleicaoByName(nomeEleicao);
		if(escolhida!=null){
			return escolhida.getListas();
		}
		return null;
	}


	public void setDataInicio(String nomeEleicao, GregorianCalendar dataInicio) throws  RemoteException {
		Eleicao eleicao = getEleicaoByName(nomeEleicao);
		if (eleicao != null)
			eleicao.setDataInicio(dataInicio);
	}

	public void setDataFim(String nomeEleicao, GregorianCalendar dataFim) throws  RemoteException {
		Eleicao eleicao = getEleicaoByName(nomeEleicao);
		if (eleicao != null)
			eleicao.setDataFim(dataFim);
	}

	public String login(String cc, String password) throws  RemoteException {

		for (Pessoa pessoa : pessoas) {
			if (pessoa.getNumberCC().equals(cc) && pessoa.getPassword().equals(password))
				return "true | Login com Sucesso";
		}
		return "false | Login Incorreto";
	}

	public void ping() throws RemoteException {
	}

	public Pessoa identificar(String cc) throws  RemoteException {

		for (Pessoa pessoa : pessoas) {
			if (pessoa.getNumberCC().equals(cc))
				return pessoa;
		}
		return null;
	}

	public void save(String arrayName) {
		File file = new File("./ObjectFiles/" + arrayName + ".obj");

		writeObjects(arrayName, file);
	}

	public void saveAll() {

		File eleicoes = new File("./ObjectFiles/eleicoes.obj");
		File pessoas = new File("./ObjectFiles/pessoas.obj");
		File mesas = new File("./ObjectFiles/mesas.obj");

		writeObjects( "eleicoes", eleicoes);
		writeObjects( "pessoas", pessoas);
		writeObjects( "mesas", mesas);
	}


	public void writeObjects(String aux, File f){

		try {
			f.getParentFile().mkdirs();
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			switch (aux) {
				case "eleicoes":
					oos.writeObject(eleicoes);
					break;
				case "pessoas":
					oos.writeObject(pessoas);
					break;
				case "mesas":
					oos.writeObject(mesas);
					break;
				default:
					System.out.println("Erro: Array nao existente.");
			}
			oos.close();
		}
		catch (FileNotFoundException ex) {
			System.out.println("Erro a criar ficheiro.");

		}
		catch (IOException ex) {
			System.out.println("Erro a escrever para o ficheiro.");
		}

	}

	public void load() {

		File eleicoes = new File("./ObjectFiles/eleicoes.obj");
		File pessoas = new File("./ObjectFiles/pessoas.obj");
		File mesas = new File("./ObjectFiles/mesas.obj");

		readObjects( "eleicoes", eleicoes);
		readObjects( "pessoas", pessoas);
		readObjects( "mesas", mesas);
	}

	public void readObjects(String aux, File f) {

		try {
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);

			while(true){
				try{
					switch (aux) {
						case "eleicoes":
							List<Eleicao> listEleicao = (List<Eleicao>)ois.readObject();
							eleicoes = (ArrayList<Eleicao>) listEleicao;
							break;
						case "pessoas":
							List<Pessoa> listPessoa = (List<Pessoa>)ois.readObject();
							pessoas = (ArrayList<Pessoa>) listPessoa;
							break;
						case "mesas":
							List<MesaVoto> listMesas = (List<MesaVoto>)ois.readObject();
							mesas = (ArrayList<MesaVoto>) listMesas;
							break;
						default:
							System.out.println("Erro: Array nao existente.");
					}
				}
				catch (ClassNotFoundException ex) {
					System.out.println("Erro a converter objeto");
				}
				catch (EOFException ex){
					ois.close();
				}
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println("Erro a abrir ficheiro.");
		}
		catch (IOException ex) {
			System.out.println("Erro a ler ficheiro.");
		}
	}

	public String sayHello() throws RemoteException {
		return "¡Hola mundo, soy el servidor y he establecido contacto con el cliente!";
	}
}
