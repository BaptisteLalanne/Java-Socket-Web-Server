package stream;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


public class Room {
	public ServerSocket serversocket;
	public SenderServer multicast;
	private String password;
	public Integer port;
	private List<String> user = new ArrayList<String>();

	public Room (ServerSocket serverSocket, SenderServer multicast ,String password, int port, List<String> user){
		this.serversocket = serverSocket;
		this.port = port;
		this.user = user;
		this.multicast = multicast;
		this.password = password;
	}

	public Room (ServerSocket serverSocket, SenderServer multicast ,String password, int port){
		this.serversocket = serverSocket;
		this.port = port;
		this.multicast = multicast;
		this.password = password;
	}

	public Room (ServerSocket serverSocket, SenderServer multicast, int port){
		this.serversocket = serverSocket;
		this.port = port;
		this.multicast = multicast;
	}

	public boolean checkPassword(String pass){
		return this.password == pass;
	}
}