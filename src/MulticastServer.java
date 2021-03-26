import java.net.*;
import java.io.*;
import java.util.*;

public class MulticastServer extends Thread {
    private String address;
    private int port;
    private DatagramPacket packet;
    private MulticastSocket socket = null;
    private InetAddress group;

    public MulticastServer(String address, String port) {
        super("Server " + (long) (Math.random() * 1000));
        this.address = address;
        this.port = Integer.parseInt(port);
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Bad Arguments.Run java MulticastServer {address} {port}");
            System.exit(1);
        }
        MulticastServer server = new MulticastServer(args[0], args[1]);
        MulticastReader reader = new MulticastReader(args[0], args[1]);

        server.start();
        reader.start();
    }

    public void run() {
        try {
            socket = new MulticastSocket(port); // create socket and bind it
            group = InetAddress.getByName(address);
            socket.joinGroup(group);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String escolha = "";

            while (true) {
                System.out.print(printMenu());
                System.out.print("Opcao: ");
                escolha = reader.readLine();

                switch (escolha) {
                    case "1":
                        identify(reader);
                        break;
                    case "2":
                        listMembers();
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
        String menu = "OPCOES DISPONIVEIS. Digite 1 por exemplo.\n" +
                "___________________________________________\n" +
                "1.Identificar e Votar\n" +
                "2.Membros da Mesa\n";
        return menu;
    }

    private void identify(BufferedReader reader) throws Exception {
        System.out.print("Nome:");
        String nome = reader.readLine();

        sendMessage("type:free | terminalId:all");
        Message resposta = getMessage();
        while (!resposta.tipo.equals("freeStatus")) {// descartar mensagens nao importantes
            resposta = getMessage();
        }

        String freeTerminal = resposta.pares.get("terminalId");
        sendMessage("type: unlock | terminalId:" + freeTerminal);
        System.out.println("Dirija-se ao terminal " + freeTerminal + " para votar.");
        System.out.println("Carregue Enter para sair.");
        reader.readLine();
    }

    public void listMembers() {

    }

    private void sendMessage(String message) throws Exception {
        byte[] buffer = message.getBytes();
        packet = new DatagramPacket(buffer, buffer.length, group, port);
        socket.send(packet);
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
    private String address;
    private int port;
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

    private void startConnection() throws Exception {
        counterID++;
        String newId = Integer.toString(counterID);
        sendMessage("type:set | terminalId:-1 | newId:" + newId);
    }

    private void sendMessage(String message) throws Exception {
        byte[] buffer = message.getBytes();
        packet = new DatagramPacket(buffer, buffer.length, group, port);
        socket.send(packet);
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
