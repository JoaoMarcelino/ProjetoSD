import java.net.*;
import java.io.*;
import java.util.*;

public class MulticastClient extends Thread {
    private String address;
    private int port;
    private long sleepTime = 5000;
    private InetAddress group;
    private DatagramPacket packet;
    private MulticastSocket socket = null;
    private String id = "-1";
    private boolean free = true;

    public MulticastClient(String address, String port) {
        super("Server " + (long) (Math.random() * 1000));
        this.address = address;
        this.port = Integer.parseInt(port);
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Bad Arguments.Run java MulticastClient {address} {port}");
            System.exit(1);
        }

        MulticastClient client = new MulticastClient(args[0], args[1]);
        client.start();
    }

    public void run() {
        try {
            socket = new MulticastSocket(port); // create socket and bind it
            group = InetAddress.getByName(address);
            socket.joinGroup(group);

            sendMessage("type:joinGroup | terminalId:-1");// send joinGroup msg to server

            while (true) {
                Message msg = getMessage(); //id={id,all}

                switch (msg.tipo) {
                    case ("set"):
                        joinGroup(msg);
                        break;
                    case ("free"):
                        free(msg);
                        break;
                    case ("unlock"):
                        unlock(msg);
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

    private void joinGroup(Message msg) throws Exception {
        setId(msg);
        System.out.println("Joined multicast group successfully. Id:" + id);
    }

    private void free(Message msg) throws Exception {
        if (this.free) {
            sendMessage("type:freeStatus | terminalId:" + id + " | success:yes");
        }
    }

    private void unlock(Message msg) throws Exception{
        MulticastUser user = new MulticastUser(address, port, id,5);
        Watcher watcher=new Watcher(user);
        user.start();
        watcher.start();

        user.join();
        watcher.join();
    }

    private void setId(Message msg) {
        this.id = msg.pares.get("newId");
    }

    private void sendMessage(String message) throws IOException {
        byte[] buffer = message.getBytes();
        InetAddress group = InetAddress.getByName(address);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
        socket.send(packet);
    }

    private Message getMessage() throws Exception {
        Message msg = new Message("type:inutil");
        do {
            byte[] buffer = new byte[256];
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            String message = new String(packet.getData(), 0, packet.getLength());
            msg = new Message(message);
        } while (!(msg.pares.get("terminalId").equals(this.id) || msg.pares.get("terminalId").equals("all")));

        return msg;
    }
}

class MulticastUser extends Thread {
    private String address;
    private int port;
    private String id;
    private long sleepTime = 5000;
    private String nome = "";
    private String password = "";
    private InetAddress group;
    private DatagramPacket packet;
    private MulticastSocket socket = null;
    private boolean isLogged = false;
    BufferedReader reader;

    public long lastTime;
    public long timeout;


    public MulticastUser(String address, int port,String id,long timeout) {
        super("User " + (long) (Math.random() * 1000));
        this.address=address;
        this.port=port;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.timeout=timeout;
        this.lastTime=(new Date()).getTime();
    }

     public void run() {
        try {
            group = InetAddress.getByName(address);
            socket = new MulticastSocket();

            //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String escolha = "";
            boolean voted = false;

            while (!voted) {
                System.out.print(printMenu());
                System.out.print("Opcao: ");
                escolha = reader.readLine();
                waitTimeout(1);

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
                        if (isLogged)
                            voted = vote();
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
        String menu =   "OPCOES DISPONIVEIS. Digite 1 por exemplo.\n"+
                        "___________________________________________\n"+
                        "1.Login\n"+
                        "2.Listar Eleicoes\n"+
                        "3.Listar Listas Candidatas\n";
        if (this.isLogged)
            menu += "4.Votar\n";
        return menu;
    }


    public void login() {
        this.isLogged = true;
    }

    public void listElections() {
    }

    public void listCandidates() {
    }

    public boolean vote(){
        return true;
    }
    synchronized public void waitTimeout(int flag){
        //MulticastUser executa isto depois de cada readline();
        //Dá reset ao timer c/ o notifyAll()
        if(flag==1){
            lastTime=(new Date()).getTime();
            notifyAll();
            return;
        }
        //Thread auxiliar criada ao mesmo tempo que MulticastUser executa isto
        //Depois disto, faz Thread.stop() ao MulticastUser e termina também
        else {
            long dif=0;
            while (dif < timeout) {
                try {
                    wait(timeout * 1000);
                } catch (InterruptedException e) {
                    System.out.println("interruptedException caught");
                }
                long thisTime = (new Date()).getTime();
                dif = (thisTime - lastTime) / 1000;
                System.out.println("\nWoke now.Have passed: "+dif);
            }
        }
    }


    private void sendMessage(String message) throws IOException {
        byte[] buffer = message.getBytes();
        packet = new DatagramPacket(buffer, buffer.length, group, port);
        socket.send(packet);
    }

    private Message getMessage() throws Exception {
        Message msg = new Message("type:inutil");
        do {
            byte[] buffer = new byte[256];
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            String message = new String(packet.getData(), 0, packet.getLength());
            msg = new Message(message);
        } while (!(msg.pares.get("terminalId").equals(this.id) || msg.pares.get("terminalId").equals("all")));

        return msg;
    }
}

class Watcher extends Thread{
    public MulticastUser watched;

    public Watcher(MulticastUser watched){
        super("Watcher " + (long) (Math.random() * 1000));
        this.watched=watched;
    }

    public void run() {
        watched.waitTimeout(0);
        System.out.printf("\nPassaram-se "+watched.timeout+"s sem atividade.Fechando Terminal.");
        watched.stop();
    }
}