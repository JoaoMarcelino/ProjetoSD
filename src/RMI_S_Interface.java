import java.rmi.*;
import java.util.*;


public interface RMI_S_Interface extends Remote {
	public String sayHello() throws java.rmi.RemoteException;

	public String addPessoa(String nome, String password, Departamento departamento, String telefone, String morada, String numberCC, GregorianCalendar expireCCDate, Profissao profissao) throws RemoteException;
	
	public String addEleicao(String titulo, String descricao,GregorianCalendar dataInicio, GregorianCalendar dataFim, ArrayList<Profissao> profissoes, ArrayList<Departamento> departamentos) throws RemoteException;

	public String editEleicao(String tituloAntigo,String tituloNovo, String descricaoNova) throws java.rmi.RemoteException;

	public String addMesa(Departamento departamento, ArrayList<Pessoa> membros, String ip, String port) throws java.rmi.RemoteException;

	public String addLista(String nomeEleicao,String nomeLista, ArrayList<Pessoa> listaPessoas, Profissao tipoLista) throws java.rmi.RemoteException;

	public void removeLista(Eleicao eleicao, String nome) throws java.rmi.RemoteException;

	public String addVotoAntecipado(String numeroCC,String password,String nomeEleicao,String nomeLista) throws java.rmi.RemoteException;

	public Resultado getResultados(String nomeEleicao) throws java.rmi.RemoteException;

	public Voto getVoto(String numeroCC, String nomeEleicao) throws java.rmi.RemoteException;

	public ArrayList<Pessoa> listPessoas() throws  java.rmi.RemoteException;

	public ArrayList<Eleicao> listEleicoes() throws  java.rmi.RemoteException;

	public ArrayList<Lista> listListas(String nomeEleicao) throws  java.rmi.RemoteException;

	public void setDataInicio(String nomeEleicao, GregorianCalendar dataInicio) throws java.rmi.RemoteException;

	public void setDataFim(String nomeEleicao, GregorianCalendar dataFim) throws java.rmi.RemoteException;

	public boolean login(String cc, String password) throws java.rmi.RemoteException;

	public Pessoa identificar(String cc) throws java.rmi.RemoteException;

	public void ping() throws java.rmi.RemoteException;
}
