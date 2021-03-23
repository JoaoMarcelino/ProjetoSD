import java.net.*;
import java.io.*;
import java.util.*;

public class MulticastServer extends Thread {
    private String address = "224.0.224.0";
    private int port = 4321;
    private DatagramPacket packet;
    private MulticastSocket socket = null;
    private InetAddress group;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Bad Arguments.");
            System.out.println("Run \"java MulticastServer {address} {port}");
            System.exit(1);
        }

        MulticastServer server = new MulticastServer(args[0], args[1]);
        MulticastReader reader = new MulticastReader(args[0], args[1]);

        server.start();
        reader.start();
    }

    public MulticastServer(String address, String port) {
        super("Server " + (long) (Math.random() * 1000));
        this.address = address;
        this.port = Integer.parseInt(port);
    }

    public void run() {
        System.out.println(this.getName() + " running...");

        try {
            socket = new MulticastSocket(port); // create socket and bind it
            group = InetAddress.getByName(address);
            socket.joinGroup(group);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String escolha = "";

            while (true) {
                System.out.println(printMenu());
                System.out.print("Opcao: ");
                escolha = reader.readLine();

                switch (escolha) {
                case "1":
                    identify(reader);
                    break;
                default:
                    System.out.println("Escolha invalida.Tente 1, por exemplo.");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    private String printMenu() {
        String menu = "" + "OPCOES DISPONIVEIS. Digite 1 por exemplo.\n"
                + "___________________________________________\n" + "1.Identificar e Votar\n";
        return menu;
    }

    private void identify(BufferedReader reader) throws Exception {
        System.out.print("Nome:");
        String nome = reader.readLine();
        // uni.searchPerson(nome);
        sendMessage("type:free | terminalId:all");
        Message resposta = getMessage();// descartar mensagem dele proprio
        resposta = getMessage();
        String freeTerminal = resposta.pares.get("terminalId");
        sendMessage("type:unlock | terminalId:" + freeTerminal);
        System.out.println("Dirija-se ao terminal " + freeTerminal + " para votar.");
    }

    private void sendMessage(String message) throws Exception {
        byte[] buffer = message.getBytes();
        packet = new DatagramPacket(buffer, buffer.length, group, port);
        socket.send(packet);
        Message inutil = getMessage();// descartar a msg que le proprio enviou
    }

    private Message getMessage() throws Exception {

        byte[] buffer = new byte[256];
        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        String message = new String(packet.getData(), 0, packet.getLength());
        Message msg = new Message(message);
        return msg;
    }

}

class MulticastReader extends Thread {
    private String address = "224.0.224.0";
    private int port = 4321;
    private int counterID = 0;
    private DatagramPacket packet;
    private MulticastSocket socket = null;
    private InetAddress group;

    public MulticastReader(String address, String port) {
        super("User " + (long) (Math.random() * 1000));
        this.address = address;
        this.port = Integer.parseInt(port);
    }

    public void run() {
        try {
            socket = new MulticastSocket(port); // create socket and bind it
            group = InetAddress.getByName(address);
            socket.joinGroup(group);

            while (true) {
                Message msg = getMessage();
                System.out.println(msg.pack());

                switch (msg.tipo) {
                case "joinGroup":
                    startConnection();
                    break;
                case "vote":
                    break;
                case "listElections":
                    break;
                case "listCandidates":
                    break;
                case "login":
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    private void sendMessage(String message) throws Exception {
        byte[] buffer = message.getBytes();
        packet = new DatagramPacket(buffer, buffer.length, group, port);
        socket.send(packet);
        Message inutil = getMessage();// descartar a msg que le proprio enviou
    }

    private Message getMessage() throws Exception {
        byte[] buffer = new byte[256];
        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        String message = new String(packet.getData(), 0, packet.getLength());
        Message msg = new Message(message);
        return msg;
    }

    private void startConnection() throws Exception {
        counterID++;
        String newId = Integer.toString(counterID);
        sendMessage("type:set | terminalId:-1 | newId:" + newId);
    }
}
