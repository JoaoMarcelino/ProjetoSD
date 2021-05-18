/**
 * Raul Barbosa 2014-11-07
 */
package hey.model;

import com.company.*;
import com.github.scribejava.core.model.OAuth2AccessToken;

import java.io.*;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.simple.JSONObject;

public class HeyBean {
    private final int totalTries;
    public RMI_S_Interface servidor;
    private final String pathToProperties = "../webapps/Hey/WEB-INF/classes/resources/config.properties";
    private String RMIHostIP;
    private int RMIHostPort;
    private String username;
    private String password;
    private boolean loggedInAsAdmin;
    private String facebookId = "";

    //FacebookBean
    private FacebookREST fb;
    private String authUrl;
    private String voteAppeal;
    private String shareResults;
    private String authCode;
    private String secretState;
    private OAuth2AccessToken accessToken;
    private boolean loginToken;
    private String name = "";

    public HeyBean() {
        totalTries = 3;
        try {
            loadProperties();

            Registry r = LocateRegistry.getRegistry(RMIHostIP, RMIHostPort);
            servidor = (RMI_S_Interface) r.lookup("ServidorRMI");

            this.fb = new FacebookREST();

        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace(); // what happens *after* we reach this line?
        }
    }

    public String addPessoa(String nome, String password, Departamento departamento, String telefone, String morada, String numberCC, GregorianCalendar expireCCDate, Profissao profissao, boolean isAdmin) {
        for (int i = 0; i < totalTries; i++) {
            try {
                String status = servidor.addPessoa(nome, password, departamento, telefone, morada, numberCC, expireCCDate, profissao, isAdmin);
                return status;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return "Servidor RMI indisponivel.";
            }
        }
        return "Servidor RMI indisponivel.";
    }

    public ArrayList<Pessoa> listPessoas() {
        for (int i = 0; i < totalTries; i++) {
            try {
                ArrayList<Pessoa> aux = new ArrayList<>(servidor.listPessoas());
                return aux;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return null;
            }
        }
        return null;
    }

    public ArrayList<Eleicao> listEleicoes() {
        for (int i = 0; i < totalTries; i++) {
            try {
                ArrayList<Eleicao> res = new ArrayList<>(servidor.listEleicoes());
                return res;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return null;
            }
        }
        return null;
    }

    public String addEleicao(String titulo, String descricao, GregorianCalendar dataInicio, GregorianCalendar dataFim, CopyOnWriteArrayList<Profissao> profissoes, CopyOnWriteArrayList<Departamento> departamentos) {
        for (int i = 0; i < totalTries; i++) {
            try {
                String status = servidor.addEleicao(titulo, descricao, dataInicio, dataFim, profissoes, departamentos);
                return status;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return "Servidor RMI indisponivel.";
            }
        }
        return "Servidor RMI indisponivel.";
    }

    public Eleicao getEleicaoByTitulo(String titulo) {
        for (int i = 0; i < totalTries; i++) {
            try {
                Eleicao eleicao = servidor.getEleicaoByName(titulo);
                return eleicao;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return null;
            }
        }
        return null;
    }

    public String editEleicao(String tituloAntigo, String tituloNovo, String descricaoNova, GregorianCalendar dataInicio, GregorianCalendar dataFim) {
        for (int i = 0; i < totalTries; i++) {
            try {
                String status = servidor.editEleicao(tituloAntigo, tituloNovo, descricaoNova, dataInicio, dataFim);
                return status;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return "Servidor RMI indisponivel.";
            }
        }
        return "Servidor RMI indisponivel.";
    }

    public String addMesaEleicao(String nomeMesa, String nomeEleicao) {
        for (int i = 0; i < totalTries; i++) {
            try {
                String status = servidor.addMesaEleicao(nomeMesa, nomeEleicao);
                return status;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return "Servidor RMI indisponivel.";
            }
        }
        return "Servidor RMI indisponivel.";
    }

    public String removeMesaEleicao(String nomeMesa, String nomeEleicao) {
        for (int i = 0; i < totalTries; i++) {
            try {
                String status = servidor.removeMesaEleicao(nomeMesa, nomeEleicao);
                return status;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return "Servidor RMI indisponivel.";
            }
        }
        return "Servidor RMI indisponivel.";
    }

    public String addMesa(Departamento departamento, CopyOnWriteArrayList<Pessoa> membros, String ip, String port) {
        for (int i = 0; i < totalTries; i++) {
            try {
                String status = servidor.addMesa(departamento, membros, ip, port);
                return status;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return "Servidor RMI indisponivel.";
            }
        }
        return "Servidor RMI indisponivel.";
    }

    public String removeMesa(String departamento) {
        for (int i = 0; i < totalTries; i++) {
            try {
                String status = servidor.removeMesa(departamento);
                return status;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return "Servidor RMI indisponivel.";
            }
        }
        return "Servidor RMI indisponivel.";
    }

    public String editMesa(String departamento, String membro1, String membro2, String membro3) {
        for (int i = 0; i < totalTries; i++) {
            try {
                String status = servidor.editMesa(departamento, membro1, membro2, membro3);
                return status;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return "Servidor RMI indisponivel.";
            }
        }
        return "Servidor RMI indisponivel.";
    }

    public ArrayList<MesaVoto> listMesas() {
        for (int i = 0; i < totalTries; i++) {
            try {
                ArrayList<MesaVoto> res = new ArrayList<>(servidor.listMesas());
                return res;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return null;
            }
        }
        return null;
    }

    public String addLista(String nomeEleicao, String nomeLista, CopyOnWriteArrayList<Pessoa> listaPessoas, Profissao tipoLista) {
        for (int i = 0; i < totalTries; i++) {
            try {
                String status = servidor.addLista(nomeEleicao, nomeLista, listaPessoas, tipoLista);
                return status;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return "Servidor RMI indisponivel.";
            }
        }
        return "Servidor RMI indisponivel.";
    }

    public String removeLista(String nomeEleicao, String nomeLista) {
        for (int i = 0; i < totalTries; i++) {
            try {
                String status = servidor.removeLista(nomeEleicao, nomeLista);
                return status;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return "Servidor RMI indisponivel.";
            }
        }
        return "Servidor RMI indisponivel.";
    }

    public ArrayList<Lista> listListas(String nomeEleicao) {
        for (int i = 0; i < totalTries; i++) {
            try {
                ArrayList<Lista> listas = new ArrayList<>(servidor.listListas(nomeEleicao));
                return listas;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return null;
            }
        }
        return null;
    }

    public Resultado getResultados(String nomeEleicao) {
        for (int i = 0; i < totalTries; i++) {
            try {
                Resultado res = servidor.getResultados(nomeEleicao);
                return res;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return null;
            }
        }
        return null;
    }

    public Voto getVoto(String numeroCC, String nomeEleicao) {
        for (int i = 0; i < totalTries; i++) {
            try {
                Voto v = servidor.getVoto(numeroCC, nomeEleicao);
                return v;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return null;
            }
        }
        return null;
    }

    public String votarAntecipado(String numeroCC, String password, String nomeEleicao, String nomeLista) {
        for (int i = 0; i < totalTries; i++) {
            try {
                String status = servidor.addVotoAntecipado(numeroCC, password, nomeEleicao, nomeLista);
                return status;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return "Servidor RMI indisponivel.";
            }
        }
        return "Servidor RMI indisponivel.";
    }

    public String votar(String numeroCC, String password, String nomeEleicao, String nomeLista) {
        for (int i = 0; i < totalTries; i++) {
            try {
                Pessoa pessoa = servidor.getPessoaByCC(numeroCC);
                Voto voto = new Voto(pessoa, new GregorianCalendar());
                String status = servidor.adicionarVoto(nomeEleicao, voto, nomeLista, null);
                return status;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return "Servidor RMI indisponivel.";
            }
        }
        return "Servidor RMI indisponivel.";
    }

    public String login(String numeroCC, String password) {
        for (int i = 0; i < totalTries; i++) {
            try {
                return servidor.login(numeroCC, password, true);
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return null;
            }
        }
        return null;
    }

    public void logout(String numeroCC, String password) {
        for (int i = 0; i < totalTries; i++) {
            try {
                this.username = null;
                this.password = null;
                this.accessToken = null;
                this.secretState = null;
                this.authCode = null;
                this.loggedInAsAdmin = false;
                this.loginToken = false;
                servidor.logout(numeroCC, password, true);
                return;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return;
            }
        }
        return;
    }

    public Pessoa getPessoaByCC(String numeroCC) {
        for (int i = 0; i < totalTries; i++) {
            try {
                return servidor.getPessoaByCC(numeroCC);
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return null;
            }
        }
        return null;
    }

    public boolean getLoggedInAsAdmin() {
        return loggedInAsAdmin;
    }

    public void setLoggedInAsAdmin(boolean loggedInAsAdmin) {
        this.loggedInAsAdmin = loggedInAsAdmin;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void loadProperties() {
        try {
            InputStream input = new FileInputStream(pathToProperties);
            Properties prop = new Properties();
            prop.load(input);
            RMIHostIP = prop.getProperty("RMIHostIP");
            RMIHostPort = Integer.parseInt(prop.getProperty("RMIHostPort"));
        } catch (FileNotFoundException e) {
            System.out.println("Ficheiro de configurações não encontrado");
        } catch (IOException e) {
            System.out.println("Erro na leitura de ficheiro de configurações");
        }
    }

    //Facebook Methods

    public boolean associateFacebookAccount() {

        //getId
        String facebookId = this.fb.getAccountId(this.accessToken);

        //associate with Pessoa in RMI
        for (int i = 0; i < totalTries; i++) {
            try {
                return servidor.changeFacebookId(this.username, facebookId);

            }catch(RemoteException e){
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return false;
            }

        }

        return false;
    }

    public boolean loginByFacebookId(){

        String facebookId = fb.getAccountId(this.accessToken);

        //find Pessoa in RMI
        for (int i = 0; i < totalTries; i++) {
            try {
                Pessoa pessoa = servidor.getPessoaByFacebookId(facebookId);

                if(pessoa != null){

                    this.username = pessoa.getNumberCC();
                    this.password = pessoa.getPassword();

                    this.setLoggedInAsAdmin(pessoa.isAdmin());

                    return true;
                }
                return false;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return false;
            }
        }
        return false;
    }

    public String removeFacebookId(String numberCC){
        //find Pessoa in RMI
        for (int i = 0; i < totalTries; i++) {
            try {

                if(servidor.changeFacebookId(numberCC, "null")){
                    return "success";
                }
                else
                    return "error";

            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1)
                    return "error";
            }
        }
        return "error";
    }

    public String getShareResults() {
        return this.fb.shareResults(this.accessToken);
    }



    public boolean getAccessToken() {
        if (this.accessToken != null)
            return true;

        this.accessToken = this.fb.getAccessToken(this.authCode, this.secretState);
        if (this.accessToken != null)
            return true;
        return false;
    }

    public boolean isLoginToken() {
        return getAccessToken();
    }


    public String getAuthUrl() {

        this.authUrl = this.fb.getAuthorizationURL();
        return authUrl;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getSecretState() {
        return secretState;
    }

    public void setSecretState(String secretState) {
        this.secretState = secretState;
    }


    public String getName() throws ParseException {
        return this.fb.getAccountName(this.accessToken);

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }
}
