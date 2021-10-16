/***
 * EchoClient
 * Example of a TCP client 
 * Date: 10/01/04
 * Authors:
 */
package stream;

import java.io.*;
import java.net.*;

public class EchoClient {

    public static Socket echoSocket = null;
    public static MulticastSocket multicast_private = null;
    public static MulticastSocket multicast_public = null;

    public static PrintStream socOut = null;
    public static BufferedReader stdIn = null;
    public static BufferedReader socIn = null;

    public static MulticastThread th_receiver = null;
    public static MulticastThread th_general = null;

    public static String username = null;
    public static String ip_lo = "localhost";
    public static String ip_mul = null;
    public static Integer port = null;

    /**
     * main method
     **/
    public static void main(String[] args) throws IOException {

        // sanity check
        if (args.length != 2) {
            System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
            System.exit(1);
        }

        // get network informations from arguments
        ip_mul = args[0];
        port = Integer.parseInt(args[1]);

        // create socket to communicate with server's main port
        initSocket();

        // manage connection by username
        connectUser();

        // friendly message <3
        System.out.println("Bonjour " + username);
        showConnectedUsers();

        // init multicast socket for general notifications
        multicast_public = new MulticastSocket(port);
        multicast_public.joinGroup(InetAddress.getByName(ip_mul));

        // create thread for general notifications
        th_general = new MulticastThread(multicast_public);
        th_general.start();

        String line;

        // main loop of program
        while (true) {

            // get user input
            line = stdIn.readLine();
            Logger.warning("EchoClient_run", "readed: " + line);

            // check if user wants to exit
            if (line.equals("."))
                break;

            // send user input (it's a command)
            socOut.println(line);

            if (line.contains("joinConversation")) {
                String wanted_receiver = line.split(" ")[1];
                joinConversation(wanted_receiver);
            }
        }

        // closing buffers
        socOut.close();
        socIn.close();
        stdIn.close();

        // closing sockets
        echoSocket.close();
        multicast_public.close();
    }

    /**
     * Join a conversation (a room)
     */
    public static void joinConversation(String receiver) throws IOException {
        /**
         * : 1. Récupérer le port 2. Se connecter au socket du port
         */

        // Reçoit le nouveau port donné par le serveur pour la conversation
        int wanted_port = Integer.parseInt(socIn.readLine());

        // Envoie la commande de connexion
        Logger.warning("EchoClient_run", "readed: " + "ConnectRoom " + receiver);
        socOut.println("ConnectRoom " + receiver);

        // Close le thread de l'ancien multicast
        if (th_receiver != null) {
            th_receiver.close();
        }
        socOut.close();
        socIn.close();
        // stdIn.close();
        echoSocket.close();
        echoSocket = new Socket(ip_lo, wanted_port);

        socIn = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        socOut = new PrintStream(echoSocket.getOutputStream());
        // stdIn = new BufferedReader(new InputStreamReader(System.in));
        multicast_private = new MulticastSocket(wanted_port);
        multicast_private.joinGroup(InetAddress.getByName(ip_mul));

        // Ouvre un thread pour écouter le multicast
        th_receiver = new MulticastThread(multicast_private);
        th_receiver.start();
        Logger.debug("EchoClient_run", "Socket: " + echoSocket.toString());
        Logger.debug("EchoClient_run", "MulticastSocket: " + multicast_private.toString());
    }

    /**
     * Initialize socket to communicate with server's main port
     */
    public static void initSocket() {

        // create socket to communicate with main server port
        try {
            echoSocket = new Socket(ip_lo, port);
            socIn = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            socOut = new PrintStream(echoSocket.getOutputStream());
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + ip_mul);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for " + "the connection to:" + ip_mul);
            System.exit(1);
        }
    }

    /**
     * Ask user's username
     * 
     * @throws IOException
     */
    public static void connectUser() throws IOException {
        String command = null;
        String response = null;
        boolean connected = false;

        while (!connected) {
            // ask client username in stdin
            System.out.print("Votre nom d'utilisateur : ");
            username = stdIn.readLine();
            command = "UsernameIs " + username;

            // send username to stdin
            try {
                socOut.println(command);
            } catch (Exception e) {
                Logger.error("EchoClient_main", "while sending: " + e.getMessage());
            }

            // get response from server
            try {
                response = socIn.readLine();
                Logger.debug("EchoClient_main", "username_response: " + response);
            } catch (IOException e) {
                Logger.error("EchoClient_main", "while receiving: " + e.getMessage());
            }

            // analyse response (OK or KO)
            if (response.equals("success"))
                connected = true;
            else
                System.out.println("Ce nom d'utilisateur est incorrect ou deja utilise.");
        }
    }

    /**
     * Print all connected users (in any room)
     */
    public static void showConnectedUsers() {
        try {
            // send command to server
            String command = "GetUsers";
            socOut.println(command);

            // get response (users list as serialized string)
            String response = socIn.readLine();
            String[] users = response.split("_;_");

            // print users or error message
            if (users.length > 0) {
                System.out.println("Liste des utilisateurs connectés :");
                for (String u : users) {
                    System.out.println("- " + u);
                }
                System.out.println(); // spacing
            } else {
                System.out.println("Aucun utilisateur n'est connecté");
            }

        } catch (IOException e) {
            Logger.error("EchoClient_showConnectedUsers", e.getMessage());
        }
    }
}
