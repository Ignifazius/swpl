package server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;

/**
 * server's main class. accepts incoming connections and allows broadcasting
 */
public class Server {

	public static void main(String args[]) throws IOException {
		if (args.length != 1)
			throw new RuntimeException("Syntax: ChatServer <port>");
		new Server(Integer.parseInt(args[0]));
	}

	/**
	 * awaits incoming connections and creates Connection objects accordingly.
	 * 
	 * @param port
	 *            port to listen on
	 */
	public Server(int port) throws IOException {
		ServerSocket server = new ServerSocket(port);
		while (true) {
			System.out.println("Waiting for Connections...");
			Socket client = server.accept();
			System.out.println("Accepted from " + client.getInetAddress());
			Connection c = connectTo(client);
			c.start();

		}
	}

	/**
	 * creates a new connection for a socket
	 * 
	 * @param socket
	 *            socket
	 * @return the Connection object that handles all further communication with
	 *         this socket
	 */
	public Connection connectTo(Socket socket) {
		Connection connection = new Connection(socket, this);
		connections.add(connection);
		return connection;
	}

	/**
	 * list of all known connections
	 */
	protected HashSet connections = new HashSet();

	/**
	 * send a message to all known connections
	 * 
	 * @param text
	 *            content of the message
	 */
	public void broadcast(String text) {
		synchronized (connections) {
			for (Iterator iterator = connections.iterator(); iterator.hasNext();) {
				Connection connection = (Connection) iterator.next();
				connection.send(text);
				logger(text);
			}
		}
	}

	/**
	 * remove a connection so that broadcasts are no longer sent there.
	 * 
	 * @param connection
	 *            connection to remove
	 */
	public void removeConnection(Connection connection) {
		connections.remove(connection);
	}
	
	public void logger(String text){
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log_server.txt", true)));
		    out.println(decrypt(text));
		    out.close();
		} catch (IOException e) {

		}
	}
	
	/**
	 * Methode zum verschlüsseln der Nachrichten
	 * @param in verschlüsselter String
	 * @return entschlüsslter String
	 */
	public String decrypt(String in) {
		char[] chars = in.toCharArray();
		int j = chars.length;
		char[] chars2 = new char[j];
		char[] chars3 = new char[j];
		//Text umdrehen
		for (int i = 0; i <= j-1; i++){
			chars2[j-1-i] = chars[i];
		}
		
		//Rot13
		String alphabet = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
		int x = alphabet.length();
		for (int i = 0; i <= j-1; i++){
			for (int y = 0; y < x; y++){
				//System.out.println("[" + alphabet.charAt(y) + "] [" + chars[i] + "]");
				if (alphabet.charAt(y) == chars2[i]){
					chars3[i] = alphabet.charAt((y+x-13)%x);
					break;
				}
			}
		}
		
		
		return new String(chars3);
	}

}
