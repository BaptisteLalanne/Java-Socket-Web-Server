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

	private long thread_id;
	
	ClientThread(Socket s, SenderServer multicast) {
		this.clientSocket = s;
		this.multicast = multicast;
		this.thread_id = Thread.currentThread().getId();
		showThreadInfos();
	}

	ClientThread(Socket s) {
		this.clientSocket = s;
		this.multicast = null;
		this.thread_id = Thread.currentThread().getId();
		showThreadInfos();
	}

	private void showThreadInfos() {
		Logger.debug("ClientThread_construct", "Thread name: " + Thread.currentThread().getName());
		Logger.debug("ClientThread_construct", "Thread ID: " + this.thread_id);
	}

 	/**
  	* receives a request from client then sends an echo to the client
  	* @param clientSocket the client socket
  	**/
	public void run() {
		try {
    		BufferedReader socIn = null;
			String output = null;
    		socIn = new BufferedReader(
    			new InputStreamReader(clientSocket.getInputStream()));    
    		PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
    		while (true) {
				Logger.warning("ClientThread_run", thread_id + " waiting for a line ");
    			String line = socIn.readLine();
				Logger.warning("ClientThread_run", thread_id + " line received  \" " + line + "\"");
				if (line != null){
					
					if(line.contains("UsernameIs")){
						Logger.debug("ClientThread_run", "USERNAMEIS command");
						Logger.debug("ClientThread_run", "input: " + line);
						String wanted_username = line.split(" ")[1];
						Boolean status = EchoServerMultiThreaded.addUser(wanted_username, clientSocket);
						output = "error";
						if(status) {
							output = "success";
							EchoServerMultiThreaded.notifyConnection(wanted_username);
						}
						Logger.debug("ClientThread_run", "output through Socket: " + output);
						socOut.println(output);

					} else if (line.contains("GetUsers")) {
						output = EchoServerMultiThreaded.getConnectedUsers();
						socOut.println(output);

					} else if(line.contains("checkserver")){
						Logger.debug("ClientThread_run", "CHECKSERVER command");
						Logger.debug("ClientThread_run", "input: " + line);
						output = EchoServerMultiThreaded.manageRoom(Integer.parseInt(line.split(" ")[1]));
						Logger.debug("ClientThread_run", "output through Socket: " + output);
						socOut.println(output);

					} else if (line.contains("joinserver")){
						Logger.debug("ClientThread_run", "JOINSERVER command");
						Logger.debug("ClientThread_run", "input: " + line);
						output = EchoServerMultiThreaded.connectRoom(Integer.parseInt(line.split(" ")[1]));
						Logger.debug("ClientThread_run", "output through Socket: " + output);
						// socOut.println(output);
						break;

					} else {
						Logger.debug("ClientThread_run", "MESSAGE command");
						Logger.debug("ClientThread_run", "input + output through MulticastSocket: " + line);
						
						Logger.debug("ClientThread_run", "t_id: " + thread_id);

						if (multicast == null){
							Logger.warning("ClientThread_run", "multicast is null!");
						} else {
							multicast.send(line);
						}
						// socOut.println(line);
					}
				}
    		}

			Logger.warning("ClientThread_run", "Loop in thread " + thread_id + " ended");

    	} catch (Exception e) {
        	Logger.error("ClientThread_run: thread_id "+ thread_id, e.getMessage());
        }
	}
  
  }

  
