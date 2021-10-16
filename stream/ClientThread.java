/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;

public class ClientThread extends Thread {

	private Socket clientSocket;
	private SenderServer multicast;
	private String wanted_username;
	private String roomName;
	private long thread_id;

	ClientThread(Socket s, SenderServer multicast, String wanted_username, String roomName) {
		this.clientSocket = s;
		this.wanted_username = wanted_username;
		this.roomName = roomName;
		this.multicast = multicast;
		this.thread_id = Thread.currentThread().getId();
		showThreadInfos();
	}

	ClientThread(Socket s) {
		this.clientSocket = s;
		this.thread_id = Thread.currentThread().getId();
		showThreadInfos();
		this.multicast = null;
	}

	/**
	 * Debug method to print thread instance informations
	 */
	private void showThreadInfos() {
		Logger.debug("ClientThread_construct", "Thread name: " + Thread.currentThread().getName());
		Logger.debug("ClientThread_construct", "Thread ID: " + this.thread_id);
	}

	/**
	 * receives a request from client then sends an echo to the client
	 * 
	 * @param clientSocket the client socket
	 **/
	public void run() {
		try {

			// init buffers
			BufferedReader socIn = null;
			String output = null;
			socIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintStream socOut = new PrintStream(clientSocket.getOutputStream());

			String line = "init";

			while (true && line != null) {

				// waiting and reading client input
				Logger.warning("ClientThread_run", thread_id + " waiting for a line ");
				line = socIn.readLine();
				Logger.warning("ClientThread_run", thread_id + " line received  \" " + line + "\"");

				// analyzing client input
				if (line != null) {

					if (line.contains("UsernameIs")) {
						Logger.debug("ClientThread_run", "USERNAMEIS command");
						Logger.debug("ClientThread_run", "input: " + line);
						wanted_username = line.split(" ")[1];
						Boolean status = EchoServerMultiThreaded.addUser(wanted_username, clientSocket);
						output = "error";
						if (status) {
							output = "success";
							EchoServerMultiThreaded.notifyConnection(wanted_username);
						}
						Logger.debug("ClientThread_run", "output through Socket: " + output);
						socOut.println(output);

					} else if (line.contains("GetUsers")) {
						output = EchoServerMultiThreaded.getConnectedUsers();
						socOut.println(output);

					} else if (line.contains("ConnectRoom")) {
						Logger.debug("ClientThread_run", "CONNECT ROOM command");
						Logger.debug("ClientThread_run", "input: " + line);
						String wantedReceiver = line.split(" ")[1];
						output = EchoServerMultiThreaded.connectRoom(wanted_username, wantedReceiver);
						// Break ? todo : on garde le thread du main ou non?
						break;

					} else if (line.contains("joinConversation")) {
						Logger.debug("ClientThread_run", "JOINCONVERSATION command");
						Logger.debug("ClientThread_run", "input: " + line);
						String wantedReceiver = line.split(" ")[1];
						output = EchoServerMultiThreaded.manageRoom(wanted_username, wantedReceiver);
						// On renvoie le port
						socOut.println(output);
						Logger.debug("ClientThread_run", "output through Socket: " + output);
						// socOut.println(output);

					} else {
						Logger.debug("ClientThread_run", "MESSAGE command");
						Logger.debug("ClientThread_run", "input + output through MulticastSocket: " + line);

						Logger.debug("ClientThread_run", "t_id: " + thread_id);

						if (multicast == null) {
							Logger.warning("ClientThread_run", "multicast is null!");
						} else {
							LocalDateTime now = LocalDateTime.now();
							// Utilisation de la classe Message qui s'occupe de la mise en forme
							// et de la persistance des messages
							Message message = new Message(now,line,wanted_username,roomName);
							multicast.send(message.getMessage());
							message.saveMessage();
						}
						// socOut.println(line);
					}
				}
			}

			Logger.warning("ClientThread_run", "Loop in thread " + thread_id + " ended");

		} catch (Exception e) {
			Logger.error("ClientThread_run: thread_id " + thread_id, e.getMessage());
		}
	}

}
