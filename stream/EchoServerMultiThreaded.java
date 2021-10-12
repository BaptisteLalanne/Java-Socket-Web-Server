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

	private static String ip = "localhost";
	private static HashMap <Integer, ServerSocket> listServerSocket = new HashMap<Integer, ServerSocket>();
	private static HashMap <Integer, SenderServer> listServerMulticast = new HashMap<Integer, SenderServer>();
  
 	/**
  	* main method
	* @param EchoServer port
  	* 
  	**/
       public static void main(String args[]){ 
        ServerSocket listenSocket;
        
  	if (args.length != 1) {
          System.out.println("Usage: java EchoServer <EchoServer port>");
          System.exit(1);
  	}
	try {
		listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
		listServerSocket.put(Integer.parseInt(args[0]), listenSocket);
		System.out.println("Server ready..."); 
		while (true) {
			Socket clientSocket = listenSocket.accept();
			System.out.println("Connexion from:" + clientSocket.getInetAddress());
			ClientThread ct = new ClientThread(clientSocket);
			ct.start();
		}
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
      }

	public static void connectRoom(int port){
		if(listServerSocket.containsKey(port)){
		try{
			ServerSocket clientSocketToJoin = listServerSocket.get(port);
			Socket clientSocket = clientSocketToJoin.accept();
			System.out.println("Connexion from:" + clientSocket.getInetAddress() + " with port " + port);
			ClientThread ct = new ClientThread(clientSocket);
			ct.start();
		} catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
		}else {
			try {
				ServerSocket listenSocket = new ServerSocket(port); //port
				System.out.println("Server ready..."); 
				Socket clientSocket = listenSocket.accept();
				listServerSocket.put(port, listenSocket);
				SenderServer listenMulticast = new SenderServer(ip, port);
				listServerMulticast.put(port,listenMulticast);
				System.out.println("Connexion from:" + clientSocket.getInetAddress());
				ClientThread ct = new ClientThread(clientSocket,listenMulticast);
				ct.start();
			} catch (Exception e) {
				System.err.println("Error in EchoServer:" + e);
			}
		}
	}
  }

  
