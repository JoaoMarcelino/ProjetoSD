import java.net.*;
import java.io.*;
import java.util.*;

public class MulticastClient1 extends Thread {
    private String address = "224.0.224.0";
    private int port = 4321;
    private String id = "-1";
    private long sleepTime = 5000;

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
        MulticastUser1 user = new MulticastUser1(args[0], args[1]);

        client.start();
        user.start();
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
            while (true) {

                // getMessage bloqueante e filtra mensagens que não se destinam a este terminal
                Message msg = getMessage();
                System.out.println(msg.pack());

                // getID after joining
                switch (msg.tipo) {
                case ("set"):
                    setId(msg);
                    break;
                case ("itemList"):
                    printList(msg);
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

    private void unlock(Message msg) {

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

    public MulticastUser1(String address, String port) {
        super("User " + (long) (Math.random() * 1000));
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

    public void run() {
        try {
            group = InetAddress.getByName(address);
            socket = new MulticastSocket();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String linha = reader.readLine();
                this.sendMessage(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}