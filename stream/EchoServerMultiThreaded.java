/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;




public class EchoServerMultiThreaded  {

	private static String ip_lo = "localhost";
	private static String ip_mul;
	private static Integer port;
	private static Integer maxPort = port;
	private static HashMap <String, Room> listeRoom = new HashMap<String, Room>();
  
 	/**
  	* main method
	* @param EchoServer port
  	* 
  	**/
	public static void main(String args[]){ 
        ServerSocket listenSocket;
        
		if (args.length != 2) {
			System.out.println("Usage: java EchoServer <EchoServer ip> <EchoServer port>");
			System.exit(1);
		}

		ip_mul = args[0];
		port = Integer.parseInt(args[1]);

		try {
			listenSocket = new ServerSocket(port); //port
			Logger.debug("EchoServerMultiThreaded_main", "Server ready...");
			while (true) {
				Socket clientSocket = listenSocket.accept();
				Logger.debug("EchoServerMultiThreaded_main", "Connexion from:" + clientSocket.getInetAddress());
				ClientThread ct = new ClientThread(clientSocket);
				ct.start();
			}
		} catch (Exception e) {
			Logger.error("EchoServerMultiThreaded_main", e.getMessage());
		}
	}

	public static String manageRoom(String username1, String username2){
		String output;
		if(listeRoom.containsKey(username1+"_"+username2)){
			output= connectRoom(username1,username2);
		} else {
			output = createRoom(username1,username2);
		}
		return output;
	}


	public static String connectRoom(String username1, String username2){
		String output= "";
		try{
			Room roomToJoin =  listeRoom.get(username1+"_"+username2);
			ServerSocket clientSocketToJoin = roomToJoin.serversocket;
			Socket clientSocket = clientSocketToJoin.accept();
			SenderServer clientMulticastToListen = roomToJoin.multicast;
			Logger.debug("EchoServerMultiThreaded_connectRoom", "Connexion from:" + clientSocket.getInetAddress() + " with port " + roomToJoin.port);
			ClientThread ct = new ClientThread(clientSocket, clientMulticastToListen);
			ct.start();
			output = "Server joined";
		} catch (Exception e) {
			Logger.error("EchoServerMultiThreaded_connectRoom", e.getMessage());
        }
		return output;
	}


	public static String createRoom(String username1, String username2){
		String output = "";
		try {
			boolean portToFind = true;
			int portDepart = EchoServerMultiThreaded.maxPort + 1 ;
			ServerSocket listenSocket = null;
			while(portToFind){
				try{
					listenSocket = new ServerSocket(portDepart);
					EchoServerMultiThreaded.maxPort = portDepart;
					Logger.debug("EchoServerMultiThreaded_connectRoom", "Server ready");
					portToFind = false;
				} catch( IOException e ){
					portDepart++;
				}
			}
			SenderServer listenMulticast = new SenderServer(ip_mul, port);
			String roomName = username1+"_"+username2;
			Room newRoom = new Room(listenSocket,listenMulticast,portDepart);
			listeRoom.put(roomName,newRoom);
			output = "Server created";
		} catch (Exception e) {
			Logger.error("EchoServerMultiThreaded_createRoom", e.getMessage());
		}
		return output;
	}

	public static String generateRandomPassword(int len) {
		String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghi"
		+"jklmnopqrstuvwxyz!@#$%&";
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(chars.charAt(rnd.nextInt(chars.length())));
		return sb.toString();
	}
}

  
