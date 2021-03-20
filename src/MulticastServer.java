import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.Scanner;

public class MulticastServer extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;

    public static void main(String[] args) {
        MulticastServer server = new MulticastServer();
        server.start();
        MulticastReader reader = new MulticastReader();
        reader.start();}

    public MulticastServer() {
        super("Server " + (long) (Math.random() * 1000));
    }

    public void run() {
        MulticastSocket socket = null;
        System.out.println(this.getName() + " running...");
        try {
            socket = new MulticastSocket(PORT);  // create socket and bind it
            Scanner keyboardScanner = new Scanner(System.in);
            while (true) {
                String readKeyboard = keyboardScanner.nextLine();
                byte[] buffer = readKeyboard.getBytes();

                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                socket.send(packet);
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

    public MulticastReader() {
        super("User " + (long) (Math.random() * 1000));
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
                if (strArray[0].equals("User")){
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
