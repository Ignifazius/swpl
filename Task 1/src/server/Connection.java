package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import common.TextMessage;

/**
 * class for an individual connection to a client. allows to send messages to
 * this client and handles incoming messages.
 */
public class Connection extends Thread {
	protected Socket socket;
	protected ObjectInputStream inputStream;
	protected ObjectOutputStream outputStream;
	private Server server;

	public Connection(Socket s, Server server) {
		this.socket = s;
		try {
			inputStream = new ObjectInputStream((s.getInputStream()));
			outputStream = new ObjectOutputStream((s.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.server = server;
	}

	/**
	 * waits for incoming messages from the socket
	 */
	public void run() {
		String clientName = socket.getInetAddress().toString();
		try {
			server.broadcast(clientName + " has joined.");
			Object msg = null;
			while ((msg = inputStream.readObject()) != null) {
				handleIncomingMessage(clientName, msg);
			}
		} catch (SocketException e) {
		} catch (EOFException e) {
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			server.removeConnection(this);
			server.broadcast(clientName + " has left.");
			try {
				socket.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * decides what to do with incoming messages
	 * 
	 * @param name
	 *            name of the client
	 * @param msg
	 *            received message
	 */
	private void handleIncomingMessage(String name, Object msg) {
		if (msg instanceof TextMessage)
			server.broadcast(encrypt(name + " - " + decrypt(((TextMessage) msg).getContent())));
	}

	/**
	 * sends a message to the client
	 * 
	 * @param line
	 *            text of the message
	 */
	public void send(String line) {
		send(new TextMessage(line));
	}

	public void send(TextMessage msg) {
		try {
			synchronized (outputStream) {
				outputStream.writeObject(msg);
			}
			outputStream.flush();
		} catch (IOException ex) {
		}
	}
	
	/**
	 * Methode zum verschl�sseln der Nachrichten
	 * @param in verschl�sselter String
	 * @return entschl�sslter String
	 */
	public String decrypt(String in) {
		char[] chars = in.toCharArray();
		int j = chars.length;
		char[] chars2 = new char[j];
		for (int i = 0; i <= j-1; i++){
			chars2[j-1-i] = chars[i];
		}
		
		return new String(chars2);
	}

	/**
	 * Methode zum entschl�sseln der Nachrichten
	 * @param in String
	 * @return verschl�sslter String
	 */
	public String encrypt(String in){
		char[] chars = in.toCharArray();
		int j = chars.length;
		char[] chars2 = new char[j];
		for (int i = 0; i <= j-1; i++){
			chars2[j-1-i] = chars[i];
		}
		return new String(chars2);
	}
}