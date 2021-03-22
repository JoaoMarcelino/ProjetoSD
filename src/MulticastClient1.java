import java.net.*;
import java.io.*;
import java.util.*;
/*
1.Iniciar MulticastClient
2.Juntar ao grupo multicast indicado por argumento
3.Receber Id dado pelo MulticastServer
4.Esperar por mensagem de desbloqueio
    4.1.Marcar MulticastClient como ocupado
    4.2.Esperar que MulticastUser feche
    4.3.Iniciar MulticastUser
    4.4.Esperar Pelo Voto
    4.5.Fechar
    4.6.Marcar MulticastClient como livre
5.ou mensagem do servidor descoberta mesas livres
*/
public class MulticastClient1 extends Thread {
    private String id = "-1";
    private long sleepTime = 5000;
    private boolean free=true;

    private String address = "224.0.224.0";
    private int port = 4321;
    private InetAddress group;
    private DatagramPacket packet;
    private MulticastSocket socket = null;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Bad Arguments.");
            System.out.println("Run \"java MulticastClient {address} {port}");
            System.exit(1);
        }

        MulticastClient1 client = new MulticastClient1(args[0], args[1]);
        client.start();
        
    }

    public MulticastClient1(String address, String port) {
        super("Server " + (long) (Math.random() * 1000));
        this.address = address;
        this.port = Integer.parseInt(port);
    }

    public void run() {
        try {
            socket = new MulticastSocket(port); // create socket and bind it
            group = InetAddress.getByName(address);
            socket.joinGroup(group);

            joinGroup();//send joinGroup msg to server and get id
            
            while (true) {
                // getMessage bloqueante e filtra mensagens que não se destinam a este terminal
                Message msg = getMessage();
                System.out.println(msg.pack());
                switch (msg.tipo) {
                    case ("free"):
                        free(msg);//answer to server as free or not to accpetn new user
                        break;
                    case ("unlock"):
                        unlock(msg);
                        break;
                    default:
                        System.out.println("Message outside established protocol.");
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

    private Message getMessage() throws Exception {
        Message msg = new Message("type:inutil");
        do {
            byte[] buffer = new byte[256];
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            String message = new String(packet.getData(), 0, packet.getLength());
            msg = new Message(message);
        } while (!msg.pares.get("terminalId").equals(this.id));

        return msg;
    }

    private void joinGroup() throws Exception{
        sendMessage("type:joinGroup | terminalId:{id}");
        Message msg=getMessage();
        setId(msg);
        System.out.println("Joined multicast group successfully. Id:"+id);
    }

    private void free(Message msg) throws Exception{
        if(this.free){
            sendMessage("type:freeStatus | terminalId:"+id+" | success:yes");
        }
    }

    private void unlock(Message msg) {
        MulticastUser1 user = new MulticastUser1(address,Integer.toString(port));
        user.start();
        free=false;
        try {
            user.join();
            free=true;
        } catch (InterruptedException e) {
            System.out.println("Client Main Thread Interrupted");
            user.interrupt();
        }
    }

    private void setId(Message msg) {
        this.id = msg.pares.get("newId");
    }

    private void votingSystem() throws IOException {
        try {
            System.out.println("Vote I Guess");
            try {
                sleep((long) (sleepTime));
            } catch (InterruptedException e) {
            }
            String message = "User " + this.id + " Vote Ended";
            System.out.println(message);
            this.sendMessage(message);
        } catch (IOException e) {
            throw e;
        }
    }

    private void printList(Message msg) {

    }

    

    private void sendMessage(String message) throws IOException {
        byte[] buffer = message.getBytes();
        InetAddress group = InetAddress.getByName(address);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
        socket.send(packet);
    }
}

class MulticastUser1 extends Thread {
    private String address = "224.0.224.0";
    private int port = 4321;
    private String id = "-1";
    private long sleepTime = 5000;

    private InetAddress group;
    private DatagramPacket packet;
    private MulticastSocket socket = null;

    private String nome="";
    private String password="";

    public MulticastUser1(String address, String port) {
        super("User " + (long) (Math.random() * 1000));
    }

    public void run() {
        try {
            group = InetAddress.getByName(address);
            socket = new MulticastSocket();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String escolha="";
            boolean voted=false;

            while (!voted) {
                System.out.println(printMenu());
                System.out.print("Opcao: ");
                escolha=reader.readLine();

                switch (escolha) {
                    case "1":
                        login();                       
                        break;
                    case "2":
                        listElections();                       
                        break;
                    case "3":
                        listCandidates();                       
                        break;
                    case "4":
                        voted=vote();                       
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
    private String printMenu() {
        String menu = "" +
                "OPCOES DISPONIVEIS. Digite 1 por exemplo.\n" +
                "___________________________________________\n" +
                "1.Login\n" +
                "2.Listar Eleicoes\n" +
                "3.Listar Listas Candidatas\n" +
                "4.Votar\n";
        return menu;
    }

    public void login() {
        
    }

    public void listElections() {
        
    }

    public void listCandidates() {
        
    }

    public boolean vote() {
        //vote
        return true;       
    }

    private void sendMessage(String message) throws IOException {
        byte[] buffer = message.getBytes();
        packet = new DatagramPacket(buffer, buffer.length, group, port);
        socket.send(packet);
    }

    private void startConnection() throws IOException {
        try {
            System.out.println(this.getName() + " running...");
            String message = "START";
            this.sendMessage(message);
        } catch (IOException e) {
            throw e;
        }
    }
}