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

public class EchoServerMultiThreaded  {

	private static String ip_lo = "localhost";
	private static String ip_mul;
	private static Integer port;
	private static HashMap <Integer, ServerSocket> listServerSocket = new HashMap<Integer, ServerSocket>();
	private static HashMap <Integer, SenderServer> listServerMulticast = new HashMap<Integer, SenderServer>();
	private static HashMap <String, Socket> listeUtilisateur = new HashMap<String, Socket>();

	private static SenderServer generalNotificationsMulticast;

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

		initGeneralNotifications();

		try {
			listenSocket = new ServerSocket(port); //port
			listServerSocket.put(port, listenSocket);
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

	public static String manageRoom(int port){
		String output;
		if(listServerSocket.containsKey(port)){
			output= "Ready to connect";
		} else {
			output = createRoom(port);
		}
		return output;
	}


	public static String connectRoom(int _port){
		String output= "";
		try{
			ServerSocket clientSocketToJoin = listServerSocket.get(_port);
			Socket clientSocket = clientSocketToJoin.accept();
			SenderServer clientMulticastToListen = listServerMulticast.get(_port);
			Logger.debug("EchoServerMultiThreaded_connectRoom", "Connexion from:" + clientSocket.getInetAddress() + " with port " + _port);
			ClientThread ct = new ClientThread(clientSocket, clientMulticastToListen);
			ct.start();
			output = "Server joined";
			//ct.run();
		} catch (Exception e) {
			Logger.error("EchoServerMultiThreaded_connectRoom", e.getMessage());
        }
		return output;
	}


	public static String createRoom(int port){
		String output = "";
		try {
			ServerSocket listenSocket = new ServerSocket(port); //port
			Logger.debug("EchoServerMultiThreaded_connectRoom", "Server ready");
			listServerSocket.put(port, listenSocket);
			SenderServer listenMulticast = new SenderServer(ip_mul, port);
			listServerMulticast.put(port,listenMulticast);
			output = "Server created";
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

  }

  
