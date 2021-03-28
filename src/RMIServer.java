import java.lang.reflect.Array;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.*;

public class RMIServer extends UnicastRemoteObject implements RMI_S_Interface {

	ArrayList<Eleicao> eleicoes = new ArrayList<>();
	ArrayList<Pessoa> pessoas = new ArrayList<>();
	ArrayList<MesaVoto> mesas = new ArrayList<>();
	ArrayList<Resultado> resultados = new ArrayList<>();

	public RMIServer() throws RemoteException {
		super();
	}

	public static void main(String args[]) {

		try {
			RMIServer servidor = new RMIServer();
			Registry r = LocateRegistry.createRegistry(7000);
			r.rebind("ServidorRMI", servidor);

			System.out.println("¡El servidor RMI está listo!");
		} catch (RemoteException re) {
			System.out.println("Exception in HelloImpl.main: " + re);
		}
		/*
		Pessoa PessoaA1 = new Pessoa("jorge", "123", Departamento.DEI, "91", "Rua 1", "1",
				new GregorianCalendar(2030, Calendar.MARCH, 29).getTime(), Profissao.Estudante);
		Pessoa PessoaA2 = new Pessoa("paula", "123", Departamento.DEI, "92", "Rua 1", "2",
				new GregorianCalendar(2030, Calendar.MARCH, 29).getTime(), Profissao.Estudante);
		Pessoa PessoaB1 = new Pessoa("antonio", "123", Departamento.DEI, "92", "Rua 1", "2",
				new GregorianCalendar(2030, Calendar.MARCH, 29).getTime(), Profissao.Estudante);
		Pessoa PessoaB2 = new Pessoa("manuel", "123", Departamento.DEI, "92", "Rua 1", "2",
				new GregorianCalendar(2030, Calendar.MARCH, 29).getTime(), Profissao.Estudante);
		Pessoa PessoaC = new Pessoa("marta", "123", Departamento.DEI, "92", "Rua 1", "2",
				new GregorianCalendar(2030, Calendar.MARCH, 29).getTime(), Profissao.Docente);

		ArrayList<Pessoa> pessoasA = new ArrayList<>();
		ArrayList<Pessoa> pessoasB = new ArrayList<>();

		pessoasA.add(PessoaA1);
		pessoasA.add(PessoaA2);

		pessoasB.add(PessoaB1);
		pessoasB.add(PessoaB2);

		Lista ListaA = new Lista(pessoasA, Profissao.Estudante, "Lista A");
		Lista ListaB = new Lista(pessoasB, Profissao.Estudante, "Lista B");

		ArrayList<Lista> listas = new ArrayList<>();

		listas.add(ListaA);
		listas.add(ListaB);

		Date dateInicial = new GregorianCalendar(2021, Calendar.MARCH, 28).getTime();
		Date dateFinal = new GregorianCalendar(2021, Calendar.MARCH, 29).getTime();

		ArrayList<Pessoa> lista3 = new ArrayList<>();

		lista3.add(PessoaA1);
		lista3.add(PessoaB1);
		MesaVoto mesa1 = new MesaVoto(Departamento.DEI, lista3, "224.0.224.0", "5432");

		ArrayList<MesaVoto> mesas = new ArrayList<>();

		mesas.add(mesa1);

		ArrayList<Profissao> profissoes = new ArrayList<>();
		profissoes.add(Profissao.Estudante);

		ArrayList<Departamento> departamentos = new ArrayList<>();
		departamentos.add(Departamento.DEI);

		Eleicao nucleo_dei = new Eleicao(dateInicial, dateFinal, "Nucleo Dep Informatica", "Descricao", listas, mesas,
				profissoes, departamentos);

		Voto voto1 = new Voto(PessoaA1, dateInicial, mesa1);
		Voto voto2 = new Voto(PessoaA2, dateInicial, mesa1);
		Voto voto2_1 = new Voto(PessoaA2, dateInicial, mesa1);
		Voto voto3 = new Voto(PessoaB1, dateInicial, mesa1);
		Voto voto4 = new Voto(PessoaB2, dateInicial, mesa1);
		Voto voto5 = new Voto(PessoaC, dateInicial, mesa1);

		nucleo_dei.addVoto(voto1, "Lista A", "Valido");
		nucleo_dei.addVoto(voto2, "null", "Branco");
		nucleo_dei.addVoto(voto2_1, "Lista A", "Valido"); // Não Conta
		nucleo_dei.addVoto(voto3, "Lista B", "Valido");
		nucleo_dei.addVoto(voto4, "Lista B", "Valido");
		nucleo_dei.addVoto(voto5, "null", "Nulo"); // Não Conta

		Resultado resultado = nucleo_dei.getResultados();

		System.out.println("Nome da Eleição: " + resultado.getTitulo());
		System.out.println("Total de Votos: \n" + nucleo_dei.getTotalVotosString());

		System.out.println("Vencedor: " + nucleo_dei.getVencedor());


		 * De momento a pessoaC que é docente conseguiu votar na eleição apenas para
		 * Estudantes O voto 2 foi apenas realizado 1 vez O voto 2.1 tmb não foi aceite
		 * pelo sistema No resultado não é indicado quem ganha nem quantos votos são
		 * feitos em cada lista

		*/
	}

	public String addPessoa(String nome, String password, Departamento departamento, String telefone, String morada,
			String numberCC, Date expireCCDate, Profissao profissao) throws RemoteException {
		if(getPessoaByCC(numberCC)==null){
			Pessoa pessoa = new Pessoa(nome, password, departamento, telefone, morada, numberCC, expireCCDate, profissao);
			this.pessoas.add(pessoa);
			return nome+"("+numberCC+") adicionado.";
		}
		else {
			return nome + "(" + numberCC + ") jA existe.";
		}
	}

	public String addEleicao(String titulo, String descricao,Date dataInicio, Date dataFim, ArrayList<Profissao> profissoes, ArrayList<Departamento> departamentos) throws RemoteException {
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
		if(election==null || getListaByName(nomeEleicao,nomeLista)!=null){
			return nomeEleicao+" nao existe ou "+nomeLista+" ja existe";
		}
		else{
			Lista lista = new Lista(listaPessoas, tipoLista, nomeLista);
			election.addLista(lista);
			return nomeLista+" adicionada a "+nomeEleicao;
		}
	}

	public void removeLista(Eleicao eleicao, String nome) throws RemoteException {
		eleicao.removeLista(nome);
	}

	public void adicionarVoto(Eleicao eleicao, Voto voto, String lista, String tipo) throws RemoteException {
		eleicao.addVoto(voto, lista, tipo);
	}

	public void adicionarVotoAntecipado(Eleicao eleicao, Voto voto, String lista, String tipo) throws RemoteException {
		eleicao.addVotoAntecipado(voto, lista, tipo);
	}

	public Voto getVoto(Eleicao eleicao, String nome) throws RemoteException {
		return eleicao.getVotoByName(nome);
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

	public ArrayList<Lista> listListas(String nomeEleicao) throws  RemoteException{
		Eleicao escolhida=getEleicaoByName(nomeEleicao);
		if(escolhida!=null){
			return escolhida.getListas();
		}
		return null;
	}

	public String sayHello() throws RemoteException {
		return "¡Hola mundo, soy el servidor y he establecido contacto con el cliente!";
	}


}
