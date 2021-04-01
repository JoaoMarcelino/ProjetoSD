import java.rmi.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


public interface RMI_S_Interface extends Remote {

	String addPessoa(String nome, String password, Departamento departamento, String telefone, String morada, String numberCC, GregorianCalendar expireCCDate, Profissao profissao) throws RemoteException;

	String addEleicao(String titulo, String descricao,GregorianCalendar dataInicio, GregorianCalendar dataFim, CopyOnWriteArrayList<Profissao> profissoes, CopyOnWriteArrayList<Departamento> departamentos) throws RemoteException;

	String editEleicao(String tituloAntigo,String tituloNovo, String descricaoNova,GregorianCalendar dataInicio,GregorianCalendar dataFim) throws java.rmi.RemoteException;

	String addMesa(Departamento departamento, CopyOnWriteArrayList<Pessoa> membros, String ip, String port) throws java.rmi.RemoteException;

	String removeMesa(String nomeMesa) throws java.rmi.RemoteException;

	String addMesaEleicao(String nomeMesa,String nomeEleicao) throws java.rmi.RemoteException;

	String removeMesaEleicao(String nomeMesa,String nomeEleicao) throws  RemoteException;

	String editMesa(String nomeMesa,String membro1,String membro2,String membro3) throws RemoteException;

	String addLista(String nomeEleicao,String nomeLista, CopyOnWriteArrayList<Pessoa> listaPessoas, Profissao tipoLista) throws java.rmi.RemoteException;

	String addVotoAntecipado(String numeroCC,String password,String nomeEleicao,String nomeLista) throws java.rmi.RemoteException;

	Voto getVoto(String numeroCC, String nomeEleicao) throws java.rmi.RemoteException;

	Resultado getResultados(String nomeEleicao) throws RemoteException;

	CopyOnWriteArrayList<Pessoa> listPessoas() throws  java.rmi.RemoteException;

	Pessoa getPessoaByCC(String numberCC) throws java.rmi.RemoteException;

	CopyOnWriteArrayList<Eleicao> listEleicoes() throws  java.rmi.RemoteException;

	CopyOnWriteArrayList<Eleicao> listEleicoes(MesaVoto mesaVoto) throws  RemoteException;

	CopyOnWriteArrayList<Lista> listListas(String nomeEleicao) throws  java.rmi.RemoteException;

	CopyOnWriteArrayList<MesaVoto> listMesas() throws  java.rmi.RemoteException;

	String login(String cc, String password) throws java.rmi.RemoteException;

	void ping() throws java.rmi.RemoteException;

	MesaVoto getMesaByMulticastGroup(String ip,String port) throws java.rmi.RemoteException;

	MesaVoto getMesaByDepartamento(String dep) throws java.rmi.RemoteException;

	String adicionarVoto(String eleicao, Voto voto, String lista, Departamento dep) throws RemoteException;

	Eleicao getEleicaoByName(String nome)  throws java.rmi.RemoteException;

	void subscribe(RMI_C_Interface c) throws java.rmi.RemoteException;

	void unsubscribe(RMI_C_Interface c) throws RemoteException;

	void sendToAll(String s) throws java.rmi.RemoteException;

	void turnMesa(MesaVoto mesa, Boolean flag) throws java.rmi.RemoteException;

	Resultado getResultados(String nomeEleicao) throws RemoteException;
}
