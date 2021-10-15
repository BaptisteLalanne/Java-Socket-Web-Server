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





    /**
    *  main method
    *  accepts a connection, receives a message from client then sends an echo to the client
    **/
    public static void main(String[] args) throws IOException {

        Socket echoSocket = null;
        MulticastSocket multicastSocket = null;
        PrintStream socOut = null;
        BufferedReader stdIn = null;
        BufferedReader socIn = null;
        ReceiverClientMulticast receiver = null;

        if (args.length != 2) {
            System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
            System.exit(1);
        }

        String ip_lo = "localhost";
        String ip_mul = args[0];
        Integer port = Integer.parseInt(args[1]);

        try {
      	    // creation socket ==> connexion
      	    echoSocket = new Socket(ip_lo,port);
	          socIn = new BufferedReader(
	    		          new InputStreamReader(echoSocket.getInputStream()));    
	          socOut= new PrintStream(echoSocket.getOutputStream());
	          stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to:"+ args[0]);
            System.exit(1);
        }
                             
        String line;
        // new GUI();
        while (true) {
        	line=stdIn.readLine();
            Logger.warning("EchoClient_run", "readed: " + line);
        	if (line.equals(".")) break;
        	    socOut.println(line);
            if(line.contains("joinserver")){

                // Close le thread de l'ancien multicast
                if(receiver != null){
                    receiver.close();
                }
                socOut.close();
                socIn.close();
                // stdIn.close();
                echoSocket.close();
                Integer wanted_port = Integer.parseInt(line.split(" ")[1]);
                echoSocket = new Socket(ip_lo, wanted_port);
                
                socIn = new BufferedReader(
	    		          new InputStreamReader(echoSocket.getInputStream()));    
                socOut= new PrintStream(echoSocket.getOutputStream());
                // stdIn = new BufferedReader(new InputStreamReader(System.in));
                multicastSocket = new MulticastSocket(wanted_port);
                multicastSocket.joinGroup(InetAddress.getByName(ip_mul));

                
                // Ouvre un thread pour écouter le multicast
                receiver = new ReceiverClientMulticast(multicastSocket);
                receiver.start();
                Logger.debug("EchoClient_run", "Socket: " + echoSocket.toString());
                Logger.debug("EchoClient_run", "MulticastSocket: " + multicastSocket.toString());

            } else {
                //Logger.debug("EchoClient_main", "input through Socket : " + socIn.readLine());
            }
        }
        socOut.close();
        socIn.close();
        stdIn.close();
        echoSocket.close();
    }
}


