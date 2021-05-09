/**
 * Raul Barbosa 2014-11-07
 */
package hey.model;

import com.company.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.concurrent.CopyOnWriteArrayList;


public class HeyBean {
    private final String RMIHostIP;
    private final int RMIHostPort;
    private final int totalTries;
    public RMI_S_Interface servidor;
    private String username;
    private String password;
    private String message;


    public HeyBean() {
        //RMIHostIP = "192.168.1.69";
        RMIHostIP = "127.0.0.1";
        RMIHostPort = 8000;
        totalTries = 3;
        try {
            Registry r = LocateRegistry.getRegistry(RMIHostIP, RMIHostPort);
            servidor = (RMI_S_Interface) r.lookup("ServidorRMI");
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace(); // what happens *after* we reach this line?
        }
    }

    public String addPessoa(String nome, String password, Departamento departamento, String telefone, String morada, String numberCC, GregorianCalendar expireCCDate, Profissao profissao) {
        for (int i = 0; i < totalTries; i++) {
            try {
                String status = servidor.addPessoa(nome, password, departamento, telefone, morada, numberCC, expireCCDate, profissao);
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

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
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
}
