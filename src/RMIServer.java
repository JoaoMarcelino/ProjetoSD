import java.io.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.*;
import java.net.*;

import static java.lang.Thread.sleep;

public class RMIServer extends UnicastRemoteObject implements RMI_S_Interface {
	public static String RMIHostIP;
	public static int RMIHostPort;
	public static int frequency=1000; //frequencia de pings entre servidores (milisegundos)
	public static int totalTries=3;//n tentativas ate assumir papel de servidor principal

	public ArrayList<Eleicao> eleicoes = new ArrayList<>();
	public ArrayList<Pessoa> pessoas = new ArrayList<>();
	public ArrayList<MesaVoto> mesas = new ArrayList<>();
	public ArrayList<Resultado> resultados = new ArrayList<>();
	public Stack<String> novidades=new Stack<>();
	public RMIServer() throws RemoteException {
		super();
		load();
	}

	public static void main(String args[]) {
		if(args.length!=2){
			System.out.println("Bad arguments. Run java RMIServer {RMIHostIP} {RMIHostPort}");
			System.exit(1);
		}
		RMIHostIP=args[0];
		RMIHostPort=Integer.parseInt(args[1]);

		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());

		try {
			Registry r = LocateRegistry.getRegistry(RMIHostIP,RMIHostPort);
			RMI_S_Interface servidorPrincipal = (RMI_S_Interface) r.lookup("ServidorRMI");
			int failed = 0;

			System.out.println("Servidor RMI Secundario em execucao.");
			while (failed < totalTries) {
				sleep(frequency);
				try {
					servidorPrincipal.ping();
				} catch (RemoteException e) {
					failed++;
					System.out.println("Heartbeat falhou." + failed + "/" + totalTries);
				}
			}
			servidorPrincipal();

		}catch (NotBoundException | RemoteException e) {
			servidorPrincipal();
		} catch (Exception e){
			System.out.println(e.getMessage());
		}finally {
			System.exit(0);
		}
	}

	public static void servidorPrincipal(){
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.setProperty("java.rmi.server.hostname", RMIHostIP);
			Registry r = LocateRegistry.createRegistry(RMIHostPort);
			RMIServer servidor = new RMIServer();
			r.rebind("ServidorRMI", servidor);

			System.out.println("Servidor RMI Principal em execucao. ["+System.getProperty("java.rmi.server.hostname")+" "+RMIHostPort+"]");
			System.out.println("Enter para encerrar servidor.");
			reader.readLine();
			reader.close();
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
			return "Eleicao nao existe.";
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
		if(status.equals("Voto realizado com sucesso.")){
			save("eleicoes");
			String update=p.getNome()+" votou na Eleicao "+ele.getTitulo()+" a "+printGregorianCalendar(new GregorianCalendar())+ " na mesa "+v.getMesa();
			accessNovidades(true,update);
		}
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

	public Eleicao getEleicaoByName(String nome)  throws java.rmi.RemoteException{
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

	public Lista getListaByName(String eleicao,String nome) throws java.rmi.RemoteException{
		Eleicao election= getEleicaoByName(eleicao);
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

		public ArrayList<MesaVoto> listMesas() throws  RemoteException {
		return mesas;
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

	synchronized public String accessNovidades(boolean flagPublish,String update) throws RemoteException{
		if(flagPublish){
			novidades.push(update);	//publicar novidade na stack e acordar consumidores
			notifyAll();
			return "";
		}
		else{
			int tam=novidades.size();
			while(tam==novidades.size()){ //verificar se foram publicadas novidades na stack
				try{
					wait();
				}catch (InterruptedException e){
					System.out.println(e.getMessage());
				}
			}
			return novidades.peek(); //aceder à ultima novidade publicada, sem a remover(outros consumidores podem necessitar tambem dela
		}
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
		}catch (FileNotFoundException ex) {
			System.out.println("Erro a criar ficheiro.");
		}catch (IOException ex) {
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
			}
			ois.close();
		}
		catch (ClassNotFoundException ex) {
			System.out.println("Erro a converter objeto");
		}catch (FileNotFoundException ex) {
			System.out.println("Erro a abrir ficheiro.");
		}catch (IOException ex) {
			System.out.println("Erro a ler ficheiro.");
		}
	}

	public static String printGregorianCalendar(GregorianCalendar data){
		int hora=data.get(Calendar.HOUR);
		int dia=data.get(Calendar.DATE);
		int mes=data.get(Calendar.MONTH)+1;
		int ano=data.get(Calendar.YEAR);

		return hora+"h "+dia+" "+mes+" "+ano;
	}

	public String sayHello() throws RemoteException {
		return "¡Hola mundo, soy el servidor y he establecido contacto con el cliente!";
	}
}
