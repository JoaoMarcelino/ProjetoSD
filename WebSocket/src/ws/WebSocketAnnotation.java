package ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import javax.lang.model.type.ArrayType;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;

@ServerEndpoint(value = "/ws")
public class WebSocketAnnotation {
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private final String username;
    private Session session;
    private static ArrayList<WebSocketAnnotation> users=new ArrayList<>();

    public WebSocketAnnotation() {
        username = "User" + sequence.getAndIncrement();
    }

    @OnOpen
    public void start(Session session) {
        this.session = session;
        users.add(this);
    }

    @OnClose
    public void end() {
        users.remove(this);
    	// clean up once the WebSocket connection is closed
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
    	    for(WebSocketAnnotation usr:users){
    	        usr.session.getBasicRemote().sendText(text);
            }
		} catch (IOException e) {
			// clean up once the WebSocket connection is closed
			try {
				this.session.close();
				users.remove(this);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
    }
}
