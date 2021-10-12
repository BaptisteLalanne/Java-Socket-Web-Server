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

	public static String manageRoom(int port){
		String output;
		if(listServerSocket.containsKey(port)){
			output= "Ready to connect";
		} else {
			output = createRoom(port);
		}
		return output;
	}


	public static String connectRoom(int port){
		String output= "";
		try{
			ServerSocket clientSocketToJoin = listServerSocket.get(port);
			Socket clientSocket = clientSocketToJoin.accept();
			SenderServer clientMulticastToListen = listServerMulticast.get(port);
			Logger.debug("EchoServerMultiThreaded_connectRoom", "Connexion from:" + clientSocket.getInetAddress() + " with port " + port);
			ClientThread ct = new ClientThread(clientSocket, clientMulticastToListen);
			ct.start();
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

  }

  
