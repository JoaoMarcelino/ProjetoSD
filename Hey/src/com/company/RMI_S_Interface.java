package com.company;

import com.github.scribejava.core.model.OAuth2AccessToken;
import org.json.simple.JSONObject;

import java.rmi.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


public interface RMI_S_Interface extends Remote {

    String addPessoa(String nome, String password, Departamento departamento, String telefone, String morada, String numberCC, GregorianCalendar expireCCDate, Profissao profissao) throws RemoteException;

    String addPessoa(String nome, String password, Departamento departamento, String telefone, String morada, String numberCC, GregorianCalendar expireCCDate, Profissao profissao, boolean isAdmin) throws RemoteException;

    String editPessoa(String numeroCC, String novoNome, String novaMorada, String novoTelefone, GregorianCalendar novaValidade) throws RemoteException;

    String addEleicao(String titulo, String descricao, GregorianCalendar dataInicio, GregorianCalendar dataFim, CopyOnWriteArrayList<Profissao> profissoes, CopyOnWriteArrayList<Departamento> departamentos) throws RemoteException;

    String editEleicao(String tituloAntigo, String tituloNovo, String descricaoNova, GregorianCalendar dataInicio, GregorianCalendar dataFim) throws RemoteException;

    String addMesa(Departamento departamento, CopyOnWriteArrayList<Pessoa> membros, String ip, String port) throws RemoteException;

    String removeMesa(String nomeMesa) throws RemoteException;

    String addMesaEleicao(String nomeMesa, String nomeEleicao) throws RemoteException;

    String removeMesaEleicao(String nomeMesa, String nomeEleicao) throws RemoteException;

    String editMesa(String nomeMesa, String membro1, String membro2, String membro3) throws RemoteException;

    String addLista(String nomeEleicao, String nomeLista, CopyOnWriteArrayList<Pessoa> listaPessoas, Profissao tipoLista) throws RemoteException;

    String removeLista(String nomeEleicao, String nomeLista) throws RemoteException;

    String addVotoAntecipado(String numeroCC, String password, String nomeEleicao, String nomeLista) throws RemoteException;

    Voto getVoto(String numeroCC, String nomeEleicao) throws RemoteException;

    Resultado getResultados(String nomeEleicao) throws RemoteException;

    CopyOnWriteArrayList<Pessoa> listPessoas() throws RemoteException;

    Pessoa getPessoaByCC(String numberCC) throws RemoteException;

    boolean changeFacebookId(String numberCC, String id, OAuth2AccessToken accessToken) throws RemoteException;

    Pessoa getPessoaByFacebookId(String facebookId) throws RemoteException;

    CopyOnWriteArrayList<Eleicao> listEleicoes() throws RemoteException;

    CopyOnWriteArrayList<Eleicao> listEleicoes(MesaVoto mesaVoto) throws RemoteException;

    CopyOnWriteArrayList<Lista> listListas(String nomeEleicao) throws RemoteException;

    CopyOnWriteArrayList<MesaVoto> listMesas() throws RemoteException;

    String login(String cc, String password, boolean web) throws RemoteException;

    void logout(String cc, String password, boolean web) throws RemoteException;

    HashMap<Pessoa, String> getUsersLoggedIn() throws RemoteException;

    void ping() throws RemoteException;

    MesaVoto getMesaByMulticastGroup(String ip, String port) throws RemoteException;

    MesaVoto getMesaByDepartamento(String dep) throws RemoteException;

    String adicionarVoto(String eleicao, Voto voto, String lista, Departamento dep) throws RemoteException;

    Eleicao getEleicaoByName(String nome) throws RemoteException;

    void subscribe(RMI_C_Interface c) throws RemoteException;

    void unsubscribe(RMI_C_Interface c) throws RemoteException;

    void sendToAll(JSONObject data) throws RemoteException;

    void turnMesa(MesaVoto mesa, Boolean flag) throws RemoteException;

}
