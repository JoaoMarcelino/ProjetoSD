import java.rmi.*;
import java.util.ArrayList;
import java.util.Date;

public interface RMI_S_Interface extends Remote {
	public String sayHello() throws java.rmi.RemoteException;

	public void createPessoa(String nome, String password, Departamento departamento, String telefone, String morada,
							 String numberCC, Date expireCCDate, Profissao profissao) throws java.rmi.RemoteException;

	public void createEleicao(Date dataInicio, Date dataFim, String titulo, String descricao, ArrayList<Lista> listas,
							  ArrayList<MesaVoto> mesas, ArrayList<Profissao> profissoes,
							  ArrayList<Departamento> departamentos) throws java.rmi.RemoteException;

	public void createMesa(Departamento departamento, ArrayList<Pessoa> membros, String ip, String port)
			throws java.rmi.RemoteException;

	public void addLista(Eleicao eleicao, ArrayList<Pessoa> listaPessoas, Profissao tipoLista, String nome)
			throws java.rmi.RemoteException;

	public void removeLista(Eleicao eleicao, String nome) throws java.rmi.RemoteException;

	// Inserir headers dos métodos RMI aqui
}
