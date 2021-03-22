import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.Locale;
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
    private String id = "-1";
    private long SLEEP_TIME = 5000;

    private InetAddress group;
    private DatagramPacket packet;
    private MulticastSocket socket = null;

    public static void main(String[] args) {
        MulticastClient client = new MulticastClient();
        client.start();
        MulticastUser user = new MulticastUser();
        user.start();
    }

    public MulticastClient() {
        super("Server " + (long) (Math.random() * 1000));
    }

    private void setId(String id) {
        this.id = id;
    }

    private void sendMessage(String message) throws IOException{
        try{
            byte[] buffer = message.getBytes();
            packet = new DatagramPacket(buffer, buffer.length, group, PORT);
            socket.send(packet);
        }
        catch(IOException e){
            throw e;
        }
    }

    private void votingSystem() throws IOException{
        try{
            System.out.println("Vote I Guess");
            try { sleep((long) (SLEEP_TIME)); } catch (InterruptedException e) { }
            String message= "User " + this.id + " Vote Ended";
            System.out.println(message);
            this.sendMessage(message);
        }
        catch(IOException e){
            throw e;
        }
    }

    public void run() {
        try {
            socket = new MulticastSocket(PORT);  // create socket and bind it
            group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            while (true) {
                byte[] buffer = new byte[256];
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                message = message.toUpperCase();
                String[]strArray = message.split(" ");

                if (strArray[0].equals("SET") && id.equals("-1")){
                    if (strArray.length >1)
                        setId(strArray[1]);
                    System.out.println(message);
                }
                else if (strArray[0].equals("VOTE")){
                    message = "FREE "+ this.id;
                    this.sendMessage(message);

                }else if (strArray[0].equals(this.id)) {
                    if (strArray[1].equals("WAKE")){
                        this.votingSystem();
                    }

                }else if (strArray[0].equals("SERVER")){
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
    private InetAddress group;
    private DatagramPacket packet;
    private MulticastSocket socket = null;

    public MulticastUser() {
        super("User " + (long) (Math.random() * 1000));
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

    private void startConnection() throws IOException{
        try {
            System.out.println(this.getName() + " running...");
            String message = "START";
            this.sendMessage(message);
        }
         catch(IOException e){
                throw e;
            }
    }

    public void run() {
        try {
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            Scanner keyboardScanner = new Scanner(System.in);

            this.startConnection();

            while (true) {
                String readKeyboard = keyboardScanner.nextLine();
                this.sendMessage(readKeyboard);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}