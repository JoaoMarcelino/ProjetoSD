import java.lang.reflect.Array;
import java.net.*;
import java.io.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;
import java.rmi.registry.LocateRegistry;

public class MulticastServer extends Thread {
    private String address;
    private int port;
    private DatagramPacket packet;
    private MulticastSocket socket = null;
    private InetAddress group;
    private RMI_S_Interface servidor;
    private MesaVoto mesa = null;

    public MulticastServer(String address, String port, RMI_S_Interface servidor) {
        super("Server " + (long) (Math.random() * 1000));
        this.address = address;
        this.port = Integer.parseInt(port);
        this.servidor = servidor;
        try{
            this.mesa = servidor.getMesaByMulticastGroup(address, port);
        }
        catch (RemoteException e){
            System.out.println("Exception in RMIServer: " + e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Bad Arguments. Run java MulticastServer {address} {port} {rmi_adress}");
            System.exit(1);
        }
        RMI_S_Interface servidor = null;
        try {
            servidor = (RMI_S_Interface) LocateRegistry.getRegistry(args[2],7040).lookup("ServidorRMI");
            ;
            String message = servidor.sayHello();
            System.out.println("HelloClient: " + message);
        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
            e.printStackTrace();
        }

        MulticastServer server = new MulticastServer(args[0], args[1], servidor);
        MulticastReader reader = new MulticastReader(args[0], args[1], servidor);

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
        String menu = "OPCOES DISPONIVEIS. Digite 1 por exemplo.\n" + "___________________________________________\n"
                + "1.Identificar e Votar\n" + "2.Membros da Mesa\n";
        return menu;
    }

    private void identify(BufferedReader reader) throws Exception {
        System.out.print("NumeroCC:");
        String numeroCC = reader.readLine();

        Pessoa pessoa = servidor.getPessoaByCC(numeroCC);

        if (pessoa != null){
            sendMessage("type:free | terminalId:all");
            Message resposta = getMessage();
            while (!resposta.tipo.equals("freeStatus")) {// descartar mensagens nao importantes
                resposta = getMessage();
            }

            String freeTerminal = resposta.pares.get("terminalId");
            sendMessage("type: unlock | terminalId:" + freeTerminal);
            System.out.println("Dirija-se ao terminal " + freeTerminal + " para votar.");
        }
        else{
            System.out.println("Numero não existente");
        }


        System.out.println("Carregue Enter para sair.");
        reader.readLine();
    }

    public void listMembers() {
        if (this.mesa != null){
            ArrayList<Pessoa> membros =  this.mesa.getMembros();
            System.out.println("Membros:");
            for(Pessoa membro: membros){
                System.out.println("- " + membro);
            }
        }
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
    private RMI_S_Interface servidor;
    private MesaVoto mesa;

    public MulticastReader(String address, String port, RMI_S_Interface servidor) {
        super("User " + (long) (Math.random() * 1000));
        this.address = address;
        this.port = Integer.parseInt(port);
        this.servidor = servidor;
        try{
            this.mesa = servidor.getMesaByMulticastGroup(address, port);
        }
        catch (RemoteException e){
            System.out.println("Exception in RMIServer: " + e);
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            socket = new MulticastSocket(port); // create socket and bind it
            group = InetAddress.getByName(address);
            socket.joinGroup(group);

            Message msg = verifyIds();

            while (true) {
                switch (msg.tipo) {
                case "joinGroup":
                    startConnection();
                    break;
                case "vote":
                    vote(msg);
                    break;
                case "listElections":
                    listElections(msg);
                    break;
                case "listCandidates":
                    listCandidates(msg);
                    break;
                case "login":
                    login(msg);
                    break;
                }

                msg = getMessage();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    private Message verifyIds() throws Exception{

        sendMessage("type:reset | terminalId:all");
        Message message = getMessage(); //own

        message = getMessage();
        while(message.tipo.equals("reset")){
            String value = message.pares.get("terminalId");
            int count = Integer.parseInt(value);
            if (count> counterID){
                counterID = count;
            }
            message = getMessage();
        }

        return message;
    }

    private void startConnection() throws Exception {
        counterID++;
        String newId = Integer.toString(counterID);
        sendMessage("type:set | terminalId:-1 | newId:" + newId);
    }

    private void vote(Message message) throws Exception{
        String id = message.pares.get("terminalId");
        String numeroCC = message.pares.get("numeroCC");
        String eleicao = message.pares.get("election");
        String escolha = message.pares.get("candidate");

        //Create Voto
        Pessoa pessoa = servidor.getPessoaByCC(numeroCC);
        GregorianCalendar data = new GregorianCalendar();
        Voto voto = new Voto(pessoa, data, this.mesa);

        //create String
        String mensagem = servidor.adicionarVoto(eleicao, voto, escolha);
        String[] msg = mensagem.split("\\|");

        sendMessage("type:voteStatus | terminalId:" + id + " | success:" + msg[0].trim() + " | msg:" + msg[1].trim());

    }

    private void listElections(Message message) throws Exception {
        String id = message.pares.get("terminalId");
        //get all elections para esta mesa de voto
        ArrayList<Eleicao> eleicoes = servidor.listEleicoes(this.mesa);
        //length de eleicoes
        int length = eleicoes.size();

        sendMessage("type:itemList | terminalId:" + id + " | item_count:" + length);

        for(Eleicao eleicao : eleicoes){
            String name = eleicao.getTitulo(); // get election name
            String description = eleicao.getDescricao(); //get election description;
            System.out.println("type:itemList | terminalId:" + id + " | item_name:" + name + " | item_description:" + description);
            sendMessage("type:itemList | terminalId:" + id + " | item_name:" + name + " | item_description:" + description);
        }
    }

    private void listCandidates(Message message) throws Exception {
        String id = message.pares.get("terminalId");
        String electionName = message.pares.get("election");
        //get all candidates para esta eleicao
        ArrayList<Lista> candidaturas = servidor.listListas(electionName);

        //length de candidatos
        int length = candidaturas.size();
        sendMessage("type:itemList | terminalId:" + id + " | item_count:" + length);

        for(Lista candidatura: candidaturas){
            String name = candidatura.getNome(); // get election name
            System.out.println("type:itemList | terminalId:" + id + " | item_name:" + name);
            sendMessage("type:itemList | terminalId:" + id + " | item_name:" + name);
        }
    }

    private void login(Message message) throws Exception {
        String id = message.pares.get("terminalId");
        String numeroCC = message.pares.get("numeroCC");
        String password = message.pares.get("password");

        //verify login status
        String mensagem = servidor.login(numeroCC, password);
        String[] msg = mensagem.split("\\|");

        sendMessage("type:loginStatus | terminalId:" + id + " | success:" + msg[0].trim() + " | msg:" + msg[1].trim());
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
