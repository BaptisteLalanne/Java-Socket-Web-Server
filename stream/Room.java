package stream;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Room data structure
 */
public class Room {

	public ServerSocket serversocket;
	public SenderServer multicast;
	public Integer port;

	private String password;
	private List<String> user = new ArrayList<String>();

	public Room(ServerSocket serverSocket, SenderServer multicast, String password, int port, List<String> user) {
		this.serversocket = serverSocket;
		this.port = port;
		this.user = user;
		this.multicast = multicast;
		this.password = password;
	}

	public Room(ServerSocket serverSocket, SenderServer multicast, String password, int port) {
		this.serversocket = serverSocket;
		this.port = port;
		this.multicast = multicast;
		this.password = password;
	}

	public Room(ServerSocket serverSocket, SenderServer multicast, int port) {
		this.serversocket = serverSocket;
		this.port = port;
		this.multicast = multicast;
	}

	/**
	 * Verify password
	 * 
	 * @param pass password input
	 * @return whether passwords match or not
	 */
	public boolean checkPassword(String pass) {
		return this.password == pass;
	}
}