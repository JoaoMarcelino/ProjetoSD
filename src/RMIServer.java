import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.*;
import java.net.*;

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
			System.getProperties().put("java.security.policy", "policy.all");
			RMIServer servidor = new RMIServer();
			Registry r = LocateRegistry.createRegistry(7000);
			r.rebind("ServidorRMI", servidor);
			System.out.println("¡El servidor RMI está listo!");
		} catch (RemoteException re) {
			System.out.println("Exception in HelloImpl.main: " + re);
		}catch (Exception e){
			e.printStackTrace();
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
