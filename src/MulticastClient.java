import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.Scanner;

/**
 * The MulticastClient class joins a multicast group and loops receiving
 * messages from that group. The client also runs a MulticastUser thread that
 * loops reading a string from the keyboard and multicasting it to the group.
 * <p>
 * The example IPv4 address chosen may require you to use a VM option to
 * prefer IPv4 (if your operating system uses IPv6 sockets by default).
 * <p>
 * Usage: java -Djava.net.preferIPv4Stack=true MulticastClient
 *
 * @author Raul Barbosa
 * @version 1.0
 */
public class MulticastClient extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;
    private String id;
    private long SLEEP_TIME = 5000;

    public static void main(String[] args) {
        String fixedId = "2";
        MulticastClient client = new MulticastClient(fixedId);
        client.start();
        MulticastUser user = new MulticastUser(fixedId);
        user.start();
    }

    public MulticastClient(String id) {
        super("Server " + (long) (Math.random() * 1000));
        this.id = id;
    }

    public void run() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(PORT);  // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                String[]strArray = message.split(" ");
                if (strArray[0].equals(this.id)){
                    System.out.println("Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " with message:");
                    System.out.println(message);

                    if (strArray[1].equals("Vote")){
                        try { sleep((long) (SLEEP_TIME)); } catch (InterruptedException e) { }
                        message= "User " + this.id + " Vote Ended";
                        System.out.println(message);
                        buffer = message.getBytes();
                        packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                        socket.send(packet);
                    }

                }else if (strArray[0].equals("Server")){
                    System.out.println("Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " with message:");
                    System.out.println(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}

class MulticastUser extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;
    private String id;
    private byte[] buffer;
    private InetAddress group;
    private DatagramPacket packet;
    private MulticastSocket socket = null;

    public MulticastUser(String id) {
        super("User " + (long) (Math.random() * 1000));
        this.id = id;
    }

    private void sendMessage(String message) throws IOException {
        try {
        byte[] buffer = message.getBytes();
        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
        socket.send(packet);
        } catch (IOException e) {
            throw e;
        }
    }

    public void run() {
        try {
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            Scanner keyboardScanner = new Scanner(System.in);

            System.out.println(this.getName() + " running...");
            String message = this.getName() + " ready..." + this.id;
            sendMessage(message);

            while (true) {
                String readKeyboard = keyboardScanner.nextLine();
                sendMessage(readKeyboard);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}