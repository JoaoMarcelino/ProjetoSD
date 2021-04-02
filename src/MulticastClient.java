import java.net.*;
import java.io.*;
import java.util.*;

public class MulticastClient extends Thread {
    private final String address;
    private final int port;
    private int timeoutM =1000;
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
            InetAddress group = InetAddress.getByName(address);
            socket.joinGroup(group);

            while(this.id.equals("-1")){
                sendMessage("type:joinGroup | terminalId:-1");// send joinGroup msg to server
                Message msg = getMessage();
                msg = getMessage();
                if (msg.tipo.equals("set")){
                    joinGroup(msg);
                }
            }

            sendMessage("type:open | terminalId:" + this.id);

            while (true) {
                Message msg = getMessage(); //id={id,all}

                switch (msg.tipo) {
                    case ("set"):
                        joinGroup(msg);
                        break;
                    case ("free"):
                        free();
                        break;
                    case ("unlock"):
                        unlock();
                        break;
                    case ("reset"):
                        resetId(msg);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    private void joinGroup(Message msg){
        setId(msg);
        System.out.println("Joined multicast group successfully. Id:" + id);
    }

    private void free() throws Exception {
        if (this.free) {
            sendMessage("type:freeStatus | terminalId:" + id + " | success:yes");
        }
    }

    private void unlock() throws Exception{
        MulticastUser user = new MulticastUser(address, port, id,120);
        Watcher watcher=new Watcher(user);
        user.start();
        watcher.start();
        watcher.join();
        System.out.println("\nLocked");

        sendMessage("type:open | terminalId:" + this.id);
    }

    private void resetId(Message msg) throws Exception{
        sendMessage("type:resetStatus | terminalId:" + id);
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
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.setSoTimeout(this.timeoutM);
            try {
                socket.receive(packet);
            }
            catch (SocketTimeoutException e){
                //System.out.println("Timeout Reached");
                return new Message("type:null");
            }

            String message = new String(packet.getData(), 0, packet.getLength());
            msg = new Message(message);
        } while (!(msg.pares.get("terminalId").equals(this.id) || msg.pares.get("terminalId").equals("all")));

        return msg;
    }
}

class MulticastUser extends Thread {
    private final String address;
    private final int port;
    private int timeoutM = 1000;
    private final String id;
    private InetAddress group;
    private DatagramPacket packet;
    private MulticastSocket socket = null;
    private boolean isLogged = false;
    private String numeroCC;
    BufferedReader reader;

    public long lastTime;
    public long timeout;


    public MulticastUser(String address, int port, String id,long timeout) {
        super("User " + (long) (Math.random() * 1000));
        this.address=address;
        this.port=port;
        this.id = id;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.timeout=timeout;
        this.lastTime=(new Date()).getTime();
    }

     public void run() {
        try {
            socket = new MulticastSocket(port); // create socket and bind it
            group = InetAddress.getByName(address);
            socket.joinGroup(group);

            //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String escolha = "";
            boolean session = true;

            while (session) {
                System.out.print(printMenu());
                System.out.print("Opcao: ");
                escolha = reader.readLine();
                waitTimeout(true,false);

                switch (escolha) {
                    case "1":
                        login(reader);
                        break;
                    case "2":
                        listElections();
                        break;
                    case "3":
                        listCandidates(reader);
                        break;
                    case "4":
                        if (isLogged)
                            vote(reader);
                        break;
                    case "0":
                        isLogged = false;
                        sendMessage("type:open | terminalId:" + this.id);
                        waitTimeout(true,true);
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

        menu+= "0.Sair\n";
        return menu;
    }


    public void login(BufferedReader reader) throws Exception {
        System.out.print("numeroCC: ");
        String numeroCC = reader.readLine();
        System.out.print("password: ");
        String password = reader.readLine();

        sendMessage("type:login | terminalId:" + this.id + " | numeroCC:" + numeroCC +  " | password:" + password);

        Message message = getMessage(); // own message

        message = getMessage();
        if (message.tipo.equals("null")){
            System.out.println("Erro a dar login, tente outra vez");
            return ;
        }
        this.isLogged =  Boolean.parseBoolean(message.pares.get("success"));
        if(this.isLogged) this.numeroCC = numeroCC;

        String info = message.pares.get("msg");
        if (info != null)
            System.out.println(info);

    }

    public void listElections() throws Exception{
        int countItems = 0;
        sendMessage("type:listElections | terminalId:" + this.id);
        Message message;
        do{
            message = getMessage();
            if (message.tipo.equals("null")){
                System.out.println("Erro a dar listar Eleicoes, tente outra vez");
                return ;
            }
        }while(!message.tipo.equals("itemList"));

        String value = message.pares.get("item_count");
        countItems =  Integer.parseInt(value);

        System.out.println("Lista de Eleicoes");
        for (int i = 0; i < countItems ; i++){
            message = getMessage();
            if (message.tipo.equals("null")){
                System.out.println("Erro a dar listar Eleicoes, tente outra vez");
                return ;
            }
            String name = message.pares.get("item_name");
            String description = message.pares.get("item_description");
            System.out.println("Item " + i + ": "+ name + " -> " + description);
        }
    }

    public void listCandidates(BufferedReader reader) throws Exception{
        int countItems = 0;

        System.out.print("Eleicao: ");
        String election = reader.readLine();

        sendMessage("type:listCandidates | terminalId:" + this.id + "| election:" + election);
        Message message = getMessage(); //own message

        message = getMessage();
        if (message.tipo.equals("null")){
            System.out.println("Erro a lista candidatos, tente outra vez");
            return ;
        }

        String value = message.pares.get("item_count");
        countItems =  Integer.parseInt(value);

        System.out.println("Lista de Candidatos");
        for (int i = 0; i < countItems ; i++){
            message = getMessage();

            if (message.tipo.equals("null")){
                System.out.println("Erro a lista candidatos, tente outra vez");
                return ;
            }

            String name = message.pares.get("item_name");
            System.out.println("Item " + i + ": "+ name);
        }
    }

    public void vote(BufferedReader reader) throws Exception{
        System.out.print("Eleicao: ");
        String eleicao = reader.readLine();
        System.out.print("voto: ");
        String candidato = reader.readLine();

        sendMessage("type:vote | terminalId:"+ this.id +" | numeroCC:"+ this.numeroCC +" | election:"+ eleicao +" | candidate:" + candidato);

        Message message = getMessage(); // own message

        message = getMessage();
        if (message.tipo.equals("null")){
            System.out.println("Erro a votar, tente outra vez");
            return ;
        }
        String sucess = message.pares.get("sucess");
        String msg = message.pares.get("msg");

        System.out.println("Vote Status: " + msg);

    }

    synchronized public void waitTimeout(boolean flagNotify,boolean closeWathcer){
        //MulticastUser executa isto depois de cada readline();
        //Dá reset ao timer c/ o notifyAll()
        if(flagNotify){
            if(closeWathcer){
                lastTime=(new Date()).getTime()-timeout-1;
            }
            else{
                lastTime=(new Date()).getTime();
            }
            notifyAll();
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
            socket.setSoTimeout(this.timeoutM);
            try {
                socket.receive(packet);
            }
            catch (SocketTimeoutException e){
                //System.out.println("Timeout Reached");
                return new Message("type:null");
            }

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
        this.watched = watched;
    }

    public void run() {
        watched.waitTimeout(false,false);
        watched.stop();
    }
}