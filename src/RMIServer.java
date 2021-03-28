import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.Date;

public class RMIServer extends UnicastRemoteObject implements RMI_S_Interface {

	ArrayList<Eleicao> eleicoes = new ArrayList<>();
	ArrayList<Pessoa>   pessoas = new ArrayList<>();
	ArrayList<MesaVoto>   mesas = new ArrayList<>();

	public RMIServer() throws RemoteException {
		super();
	}

	// Inserir métodos aqui

	public void createPessoa(String nome, String password, String departamento, String telefone, String morada,
							 String numberCC, Date expireCCDate, Profissao profissao) throws RemoteException
	{
		Pessoa pessoa = new Pessoa(nome, password, departamento, telefone, morada, numberCC, expireCCDate, profissao);
		pessoas.add(pessoa);
	}

	public void createEleicao(Date dataInicio, Date dataFim, String titulo, String descricao, ArrayList<Lista> listas,
							  ArrayList<MesaVoto> mesas, ArrayList<Profissao> profissoes,
							  ArrayList<Departamento> departamentos) throws RemoteException
	{
		Eleicao eleicao = new Eleicao(dataInicio, dataFim, titulo, descricao, listas, mesas, profissoes, departamentos);
		eleicoes.add(eleicao);
	}

	public void createMesa(Departamento departamento, ArrayList<Pessoa> membros, String ip, String port)
			throws RemoteException
	{
		MesaVoto mesa = new MesaVoto(departamento, membros, ip, port);
		mesas.add(mesa);
	}

	public void addLista(Eleicao eleicao, Lista lista) throws RemoteException {
		eleicao.addLista(lista);
	}

	public void addLista(Eleicao eleicao, ArrayList<Pessoa> listaPessoas, Profissao tipoLista, String nome)
			throws RemoteException
	{
		Lista lista = new Lista(listaPessoas, tipoLista, nome);
		eleicao.addLista(lista);
	}

	public void removeLista(Eleicao eleicao, String nome) throws RemoteException {
		eleicao.removeLista(nome);
	}

	public String sayHello() throws RemoteException {
		return "¡Hola mundo, soy el servidor y he establecido contacto con el cliente!";
	}

	public static void main(String args[]) {

		try {
			RMIServer servidor = new RMIServer();
			Registry r = LocateRegistry.createRegistry(7000);
			r.rebind("ServidorRMI", servidor);

			System.out.println("¡El servidor RMI está listo!");
		}
		catch (RemoteException re) {
			System.out.println("Exception in HelloImpl.main: " + re);
		}
	}
}
