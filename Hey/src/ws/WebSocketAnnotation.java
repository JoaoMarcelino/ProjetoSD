package ws;

import com.company.RMI_S_Interface;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.lang.model.type.ArrayType;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;

import com.company.*;
import org.json.simple.JSONObject;

@ServerEndpoint(value = "/ws")
public class WebSocketAnnotation extends UnicastRemoteObject implements RMI_C_Interface {
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private static RMI_S_Interface servidor;
    private final String username;
    private final String pathToProperties = "../webapps/Hey/WEB-INF/classes/resources/config.properties";
    private HashMap<Pessoa, String> usersLoggedIn = new HashMap<>();
    private String RMIHostIP;
    private int RMIHostPort;
    private Session session;
    private String nomeEleicao;

    public WebSocketAnnotation() throws RemoteException {
        super();
        username = "User" + sequence.getAndIncrement();
        try {
            loadProperties();
            Registry r = LocateRegistry.getRegistry(RMIHostIP, RMIHostPort);
            servidor = (RMI_S_Interface) r.lookup("ServidorRMI");
            servidor.subscribe(this);
        } catch (RemoteException | NotBoundException e) {
        }
    }

    @OnOpen
    public void start(Session session) {
        this.session = session;
        try {
            usersLoggedIn = servidor.getUsersLoggedIn();
            for (Pessoa pessoa : usersLoggedIn.keySet()) {
                JSONObject data = new JSONObject();
                data.put("tipo", "utilizador");
                data.put("nome", pessoa.getNome());
                data.put("cc", pessoa.getNumberCC());
                data.put("estado", "login");
                if (usersLoggedIn.get(pessoa).equals("web")) {
                    data.put("ligacao", "web");
                }
                else {
                    data.put("ligacao", "multicast");
                }
                data.put("data", printGregorianCalendar(new GregorianCalendar(), true));
                StringWriter out = new StringWriter();
                try {
                    data.writeJSONString(out);
                    String jsonText = out.toString();
                    sendMessage(jsonText);
                } catch (Exception ignored) {
                }
            }

            ArrayList<MesaVoto> mesas = new ArrayList<>(servidor.listMesas());
            for (MesaVoto m : mesas) {
                JSONObject data = new JSONObject();
                data.put("tipo", "mesa");
                data.put("mesa", m.getDepartamento().name());
                if (m.getStatus())
                    data.put("estado", "ligado");
                else
                    data.put("estado", "desligado");
                data.put("data", printGregorianCalendar(new GregorianCalendar(), true));
                StringWriter out = new StringWriter();
                try {
                    data.writeJSONString(out);
                    String jsonText = out.toString();
                    sendMessage(jsonText);
                } catch (Exception ignored) {
                }
            }

        } catch (RemoteException ignored) {

        }
    }

    public String printGregorianCalendar(GregorianCalendar data, boolean flagHora) {
        int minuto = data.get(Calendar.MINUTE);
        int hora = data.get(Calendar.HOUR_OF_DAY);
        int dia = data.get(Calendar.DATE);
        int mes = data.get(Calendar.MONTH) + 1;
        int ano = data.get(Calendar.YEAR);
        if (flagHora)
            return dia + "/" + mes + "/" + ano + " " + hora + ":" + minuto;
        else
            return dia + "/" + mes + "/" + ano;
    }

    @OnClose
    public void end() {
        try {
            servidor.unsubscribe(this);
        } catch (RemoteException ignored) {
        }

    }

    @OnMessage
    public void receiveMessage(String message) {
        this.nomeEleicao = message;
        try {
            ArrayList<Voto> votos = new ArrayList<>(servidor.getEleicaoByName(this.nomeEleicao).getVotos());
            for (Voto v : votos) {
                JSONObject data = new JSONObject();
                data.put("tipo", "voto");
                data.put("nome", v.getPessoa().getNome());
                data.put("profissao", v.getPessoa().getProfissao().name());
                data.put("eleicao", this.nomeEleicao);
                if (v.getMesa() == null)
                    data.put("mesa", "Web");
                else
                    data.put("mesa", v.getMesa().getDepartamento().name());
                data.put("data", printGregorianCalendar(new GregorianCalendar(), true));
                StringWriter out = new StringWriter();
                data.writeJSONString(out);
                String jsonText = out.toString();
                sendMessage(jsonText);
                System.out.println(jsonText);
            }
        } catch (IOException ignored) {
        }
    }

    @OnError
    public void handleError(Throwable t) {
        t.printStackTrace();
    }

    private void sendMessage(String text) {
        // uses *this* object's session to call sendText()
        try {
            this.session.getBasicRemote().sendText(text);
        } catch (IOException e) {
            // clean up once the WebSocket connection is closed
            this.end();

        }
    }

    public void printOnClient(String s) throws RemoteException {
        sendMessage(s);
    }

    public void loadProperties() {
        try {
            InputStream input = new FileInputStream(pathToProperties);
            Properties prop = new Properties();
            prop.load(input);
            RMIHostIP = prop.getProperty("RMIHostIP");
            RMIHostPort = Integer.parseInt(prop.getProperty("RMIHostPort"));
        } catch (FileNotFoundException e) {
            System.out.println("Ficheiro de configura????es n??o encontrado");
        } catch (IOException e) {
            System.out.println("Erro na leitura de ficheiro de configura????es");
        }
    }
}
