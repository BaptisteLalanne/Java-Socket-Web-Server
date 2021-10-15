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
	private static HashMap <String, Socket> listeUtilisateur = new HashMap<String, Socket>();
	private static SenderServer generalNotificationsMulticast;
	private static Integer maxPort;
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
		maxPort = port;

		initGeneralNotifications();

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


	private static void initGeneralNotifications() {
		try {
			generalNotificationsMulticast = new SenderServer(ip_mul, port);
		} catch (SocketException e) {
			Logger.error("EchoServerMultiThreaded_initGeneralNotifications", e.getMessage());
		} catch (IOException e) {
			Logger.error("EchoServerMultiThreaded_initGeneralNotifications", e.getMessage());
		}
	}

	public static void notifyConnection(String connected_username) throws IOException {
		generalNotificationsMulticast.send(connected_username + " est connecté");
	}


	public static String manageRoom(String username1, String username2){
		String output;
		/**
		 * 
		 * Output doit renvoyer le port de connexion pour la conversation
		 * Ce n'est qu'ensuite que le client pourra se connecter à la room
		 */
		
		if(!listeRoom.containsKey(username1+"_"+username2)){
			output = createRoom(username1,username2);
		}else {
			Room roomToJoin =  listeRoom.get(username1+"_"+username2);
			output = Integer.toString(roomToJoin.port);
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
			output = "Room joinned !";
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
			output = Integer.toString(portDepart);
		} catch (Exception e) {
			Logger.error("EchoServerMultiThreaded_createRoom", e.getMessage());
		}
		return output;
	}


	public static boolean addUser(String name, Socket socket){
		boolean output = false;
		if(!listeUtilisateur.containsKey(name)){
			listeUtilisateur.put(name, socket);
			output = true;
		}
		return output;
	}

	public static String getConnectedUsers() {
		String output = String.join("_;_", listeUtilisateur.keySet());
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

  
