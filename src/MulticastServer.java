import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;


public class MulticastServer extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;
    private DatagramPacket packet;
    private MulticastSocket socket = null;
    private InetAddress group;

    public static void main(String[] args) {
        MulticastServer server = new MulticastServer();
        server.start();
        MulticastReader reader = new MulticastReader();
        reader.start();
    }

    public MulticastServer() {
        super("Server " + (long) (Math.random() * 1000));
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
        boolean receivedFree = false;
            try{
                while(!receivedFree) {
                    byte[] buffer = new byte[256];
                    packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String message = new String(packet.getData(), 0, packet.getLength());

                    message = message.toUpperCase();
                    String[] strArray = message.split(" ");
                    if (strArray[0].equals("FREE")) {
                        message = strArray[1] + " WAKE";
                        receivedFree = true;
                        System.out.println("User " + strArray[1] + " Vote Started");
                        this.sendMessage(message);
                    }
                }

            } catch(IOException e){
                throw e;
            }


    }

    public void run() {
        System.out.println(this.getName() + " running...");

        try {
            socket = new MulticastSocket(PORT);  // create socket and bind it
            group = InetAddress.getByName(MULTICAST_ADDRESS);
            Scanner keyboardScanner = new Scanner(System.in);
            socket.joinGroup(group);
            while (true) {
                String readKeyboard = keyboardScanner.nextLine();
                this.sendMessage(readKeyboard);

                readKeyboard = readKeyboard.toUpperCase();
                if (readKeyboard.equals("VOTE")){
                    this.votingSystem();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}

class MulticastReader extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;
    private int counterID = 0;
    private DatagramPacket packet;
    private MulticastSocket socket = null;
    private InetAddress group;

    public MulticastReader() {
        super("User " + (long) (Math.random() * 1000));
    }

    private void startConnection() throws IOException{
        try {
            String message = "SET " + Integer.toString(counterID);
            counterID++;
            System.out.printf(message+ "\n");

            byte[] buffer = message.getBytes();
            packet = new DatagramPacket(buffer, buffer.length, group, PORT);
            socket.send(packet);
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

                if (strArray[0].equals("START")) {
                    this.startConnection();

                }else if (strArray[0].equals("USER")){
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
