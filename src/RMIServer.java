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
			int frequency = 1;
			while (failed < toRecover) {
				sleep(frequency * 1000);
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
			return titulo + " adicionada.";
		}
		else{
			return titulo + " ja existe.";
		}
	}

	public String editEleicao(String tituloAntigo,String tituloNovo, String descricaoNova) throws java.rmi.RemoteException{
		Eleicao escolhida=getEleicaoByName(tituloAntigo);
		if(escolhida==null || getEleicaoByName(tituloNovo)!=null){
			return tituloAntigo + " nao existe ou titulo novo ja em uso.";
		}
		else{
			escolhida.setTitulo(tituloNovo);
			escolhida.setDescricao(descricaoNova);
			return escolhida.getTitulo() + " alterada.";
		}
	}

	public String addMesa(Departamento departamento, ArrayList<Pessoa> membros, String ip, String port) throws RemoteException {
		if(getMesaByDepartamento(departamento.name())== null && getMesaByMulticastGroup(ip,port)==null){
			MesaVoto mesa = new MesaVoto(departamento, membros, ip, port);
			this.mesas.add(mesa);
			return departamento+" adicionada.";
		}
		else{
			return departamento + " ou grupo Multicast ja existe.";
		}

	}

	public String addLista(String nomeEleicao,String nomeLista, ArrayList<Pessoa> listaPessoas, Profissao tipoLista) throws RemoteException {
		Eleicao election=getEleicaoByName(nomeEleicao);
		if(election==null){
			return nomeEleicao+" nao existe.";
		}
		else{
			Lista lista = new Lista(listaPessoas, tipoLista, nomeLista);
			return election.addLista(lista);
		}
	}

	public void removeLista(Eleicao eleicao, String nome) throws RemoteException {
		eleicao.removeLista(nome);
	}

	public void adicionarVoto(Eleicao eleicao, Voto voto, String lista, String tipo) throws RemoteException {
		eleicao.addVoto(voto, lista, tipo);
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
		switch (nomeLista){
			case "Branco":
				tipo="Branco";
				break;
			case "Nulo":
				tipo="Nulo";
				break;
			default:
				tipo="Valido";
				break;
		}
		String status=ele.addVotoAntecipado(v,nomeLista,tipo);
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

	public Pessoa getPessoaByCC(String numberCC){
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

	public MesaVoto getMesaByMulticastGroup(String ip,String port){
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

	public boolean login(String cc, String password) throws  RemoteException {

		for (Pessoa pessoa : pessoas) {
			if (pessoa.getNumberCC().equals(cc) && pessoa.getPassword().equals(password))
				return true;
		}
		return false;
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
		File file = new File("\\ObjectFiles\\" + arrayName + ".obj");

		writeObjects(arrayName, file);
	}

	public void saveAll() {

		File eleicoes = new File("\\ObjectFiles\\eleicoes.obj");
		File pessoas = new File("\\ObjectFiles\\pessoas.obj");
		File mesas = new File("\\ObjectFiles\\mesas.obj");

		writeObjects( "eleicoes", eleicoes);
		writeObjects( "pessoas", pessoas);
		writeObjects( "mesas", mesas);
	}


	public void writeObjects(String aux, File f){

		try {
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			switch (aux) {
				case "eleicoes":
					for (Eleicao eleicao : eleicoes)
						oos.writeObject(eleicao);
					break;
				case "pessoas":
					for (Pessoa pessoa : pessoas)
						oos.writeObject(pessoa);
					break;
				case "mesas":
					for (MesaVoto mesa : mesas)
						oos.writeObject(mesa);
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

		File eleicoes = new File("\\ObjectFiles\\eleicoes.obj");
		File pessoas = new File("\\ObjectFiles\\pessoas.obj");
		File mesas = new File("\\ObjectFiles\\mesas.obj");

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
							eleicoes = (ArrayList<Eleicao>) ois.readObject();
							break;
						case "pessoas":
							pessoas = (ArrayList<Pessoa>) ois.readObject();
							break;
						case "mesas":
							mesas = (ArrayList<MesaVoto>) ois.readObject();
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
