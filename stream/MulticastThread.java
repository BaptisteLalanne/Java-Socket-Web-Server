package stream;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class MulticastThread extends Thread {
    private MulticastSocket multicast = null;
    private GUI gui = null;

    /**
     * Default constructor
     * 
     * @param multicast multicast socket
     */
    public MulticastThread(MulticastSocket multicast, GUI gui) {
        this.multicast = multicast;
        this.gui = gui;
    }

    public MulticastThread(MulticastSocket multicast) {
        this.multicast = multicast;
        this.gui = null;
    }

    /**
     * Infinite loop waiting and printing received messages.
     */
    public void run() {
        String line = "start";
        while (line != null) {
            try {
                line = getMessage();
                Logger.debug("MulticastThread_run", "line: " + line);

                if (line.contains("NEWCONNECTION")) {
                    Logger.debug("MulticastThread_run", "NEWCONNECTION command");
                    String new_user = line.split(" ")[1].trim();
                    gui.addConnectedUser(new_user);
                    gui.refreshUsers();
                }

                else if (line.contains("DISCONNECTION")) {
                    Logger.debug("MulticastThread_run", "DISCONNECTION command");
                    String old_user = line.split(" ")[1].trim();
                    gui.removeConnectedUser(old_user);
                    gui.refreshUsers();
                }
                
                else {
                    // message received
                    Logger.debug("MulticastThread_run", "message received: " + line);
                    gui.showMessage(line + "\n");
                }

            } catch (IOException e) {
                // Logger.error("MulticastThread_run", e.getMessage());
                break;
            }
        }
    }

    /**
     * Waiting next message, and print it once received.
     * 
     * @throws IOException
     */
    public String getMessage() throws IOException {

        // make datagram packet to receive
        byte[] message = new byte[256];
        DatagramPacket packet = new DatagramPacket(message, message.length);

        // recieve the packet
        multicast.receive(packet);

        return new String(packet.getData());
    }

    /**
     * Close multicast socket.
     */
    public void close() {
        multicast.close();
    }
}
