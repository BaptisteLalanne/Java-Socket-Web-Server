package stream;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class SenderServer {
    
    private DatagramSocket serverSocket;

    private String ip;
    
    private int port;

	public SenderServer(String ip, int port) throws SocketException, IOException{
		this.ip = ip;
        this.port = port;
        // socket used to send
        serverSocket = new DatagramSocket();
	}


    public void send(String argument) throws IOException{
        // make datagram packet
        byte[] message = argument.getBytes();
        DatagramPacket packet = new DatagramPacket(message, message.length, 
            InetAddress.getByName(ip), port);
        // send packet
        serverSocket.send(packet);
    }
    
    public void close(){
        serverSocket.close();
    }
}
