package ws;

import com.company.RMI_S_Interface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import javax.lang.model.type.ArrayType;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;

import com.company.*;

@ServerEndpoint(value = "/ws")
public class WebSocketAnnotation extends UnicastRemoteObject implements RMI_C_Interface {
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private static RMI_S_Interface servidor;
    private final String username;
    private final String RMIHostIP = "127.0.0.1";
    private final int RMIHostPort = 8000;
    private Session session;


    public WebSocketAnnotation() throws RemoteException {
        super();
        username = "User" + sequence.getAndIncrement();
        try {
            Registry r = LocateRegistry.getRegistry(RMIHostIP, RMIHostPort);
            servidor = (RMI_S_Interface) r.lookup("ServidorRMI");
            servidor.subscribe(this);
        } catch (RemoteException | NotBoundException e) {
        }
    }

    @OnOpen
    public void start(Session session) {
        this.session = session;
    }

    @OnClose
    public void end() {
        try {
            servidor.unsubscribe(this);
        } catch (RemoteException e) {
        }

    }

    @OnMessage
    public void receiveMessage(String message) {
        // one should never trust the client, and sensitive HTML
        // characters should be replaced with &lt; &gt; &quot; &amp;
        String upperCaseMessage = message.toUpperCase();
        sendMessage("[" + username + "] " + upperCaseMessage);
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
}
