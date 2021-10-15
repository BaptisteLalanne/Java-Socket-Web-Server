package stream;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class ReceiverClientMulticast extends Thread {
    private MulticastSocket multicast = null;
    
    /**
     * Default constructor
     * @param multicast multicast socket
     */
    public ReceiverClientMulticast(MulticastSocket multicast){
        this.multicast = multicast;
    }
    
    /**
     * Infinite loop waiting and printing received messages.
     */
    public void run() {
        while(true){
            try{
                printMessage();
            } catch(IOException e){
                Logger.error("ReceiverClientMulticast", e.getMessage());
            }
        }
    }

    /**
     * Waiting next message, and print it once received.
     * @throws IOException
     */
    public void printMessage() throws IOException{

        // make datagram packet to receive
        byte[] message = new byte[256];
        DatagramPacket packet = new DatagramPacket(message, message.length);
        
        // recieve the packet
        multicast.receive(packet);
        System.out.println(new String(packet.getData()));
    }

    /**
     * Close multicast socket.
     */
    public void close(){
        multicast.close();
    }
}
