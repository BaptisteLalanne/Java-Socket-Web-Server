/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;

public class ClientThread
	extends Thread {
	
	private Socket clientSocket;
	private SenderServer multicast;
	
	ClientThread(Socket s, SenderServer multicast) {
		this.clientSocket = s;
		this.multicast = multicast;
	}

	ClientThread(Socket s) {
		this.clientSocket = s;
		this.multicast = null;
	}

 	/**
  	* receives a request from client then sends an echo to the client
  	* @param clientSocket the client socket
  	**/
	public void run() {
    	  try {
    		BufferedReader socIn = null;
    		socIn = new BufferedReader(
    			new InputStreamReader(clientSocket.getInputStream()));    
    		PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
    		while (true) {
    			String line = socIn.readLine();
				if(this.multicast != null){
					multicast.send(line);
				}
				if(line.contains("createserver")){
					EchoServerMultiThreaded.connectRoom(Integer.parseInt(line.split(" ")[1]));
					socOut.println("server created");
				}
    			socOut.println(line);
    		}
    	} catch (Exception e) {
        	System.err.println("Error in EchoServer:" + e); 
        }
       }
  
  }

  
