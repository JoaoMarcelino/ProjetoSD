import java.rmi.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public interface RMI_S_Interface extends Remote {
	public String sayHello() throws java.rmi.RemoteException;

	public String addPessoa(String nome, String password, Departamento departamento, String telefone, String morada, String numberCC, Calendar expireCCDate, Profissao profissao) throws java.rmi.RemoteException;

	public String addEleicao(String titulo, String descricao,Calendar dataInicio, Calendar dataFim, ArrayList<Profissao> profissoes, ArrayList<Departamento> departamentos) throws java.rmi.RemoteException;

	public String editEleicao(String tituloAntigo,String tituloNovo, String descricaoNova) throws java.rmi.RemoteException;

	public String addMesa(Departamento departamento, ArrayList<Pessoa> membros, String ip, String port) throws java.rmi.RemoteException;

	public String addLista(String nomeEleicao,String nomeLista, ArrayList<Pessoa> listaPessoas, Profissao tipoLista) throws RemoteException;

	public void removeLista(Eleicao eleicao, String nome) throws java.rmi.RemoteException;

	public ArrayList<Pessoa> listPessoas() throws  RemoteException;

	public ArrayList<Eleicao> listEleicoes() throws  RemoteException;

	public ArrayList<Lista> listListas(String nomeEleicao) throws  RemoteException;

	}
