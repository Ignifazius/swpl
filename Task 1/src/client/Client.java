package client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import common.TextMessage;

/**
 * simple chat client
 */
public class Client implements Runnable {
	public static void main(String args[]) throws IOException {
		if (args.length != 2)
			throw new RuntimeException("Syntax: ChatClient <host> <port>");

		Client client = new Client(args[0], Integer.parseInt(args[1]));
		new Gui("Chat " + args[0] + ":" + args[1], client);
	}

	protected ObjectInputStream inputStream;

	protected ObjectOutputStream outputStream;

	protected Thread thread;

	public Client(String host, int port) {
		try {
			System.out.println("Connecting to " + host + " (port " + port + ")...");
			Socket s = new Socket(host, port);
			this.outputStream = new ObjectOutputStream((s.getOutputStream()));
			this.inputStream = new ObjectInputStream((s.getInputStream()));
			thread = new Thread(this);
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * main method. waits for incoming messages.
	 */
	public void run() {
		try {
			Thread thisthread = Thread.currentThread();
			while (thread == thisthread) {
				try {
					Object msg = inputStream.readObject();
					handleIncomingMessage(msg);
				} catch (EOFException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			thread = null;
			try {
				outputStream.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * decides what to do with incoming messages
	 * 
	 * @param msg
	 *            the message (Object) received from the sockets
	 */
	protected void handleIncomingMessage(Object msg) {
		if (msg instanceof TextMessage) {
			fireAddLine(decrypt(((TextMessage) msg).getContent()) + "\n");
		}
	}

	public void send(String line) {
		send(new TextMessage(encrypt(line)));
	}

	public void send(TextMessage msg) {
		try {
			outputStream.writeObject(msg);
			outputStream.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
			this.stop();
		}
	}

	/**
	 * listener-list for the observer pattern
	 */
	private ArrayList listeners = new ArrayList();

	/**
	 * addListner method for the observer pattern
	 */
	public void addLineListener(ChatLineListener listener) {
		listeners.add(listener);
	}

	/**
	 * removeListner method for the observer pattern
	 */
	public void removeLineListener(ChatLineListener listner) {
		listeners.remove(listner);
	}

	/**
	 * fire Listner method for the observer pattern
	 */
	public void fireAddLine(String line) {
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
			ChatLineListener listener = (ChatLineListener) iterator.next();
			listener.newChatLine(line);
			logger(line);
		}
	}

	public void stop() {
		thread = null;
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
		for (int i = 0; i <= j-1; i++){
			chars2[j-1-i] = chars[i];
		}
		
		String alphabet = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
		
		int x = alphabet.length();
		for (int i = 0; i <= j-1; i++){
			for (int y = 0; y < x; y++){
				//System.out.println("[" + alphabet.charAt(y) + "] [" + chars[i] + "]");
				if (alphabet.charAt(y) == chars2[i]){
					chars3[i] = alphabet.charAt((y+x-13)%x);
					System.out.println(chars3[i]);
					break;
				}
			}
		}
		
		return new String(chars3);
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
					System.out.println(chars3[i]);
					break;
				}
			}
		}
		
		
		return new String(chars3);
	}

	/**
	 * Methode zum entschlüsseln der Nachrichten
	 * @param in String
	 * @return verschlüsslter String
	 */
	public String encrypt(String in){
		char[] chars = in.toCharArray();
		int j = chars.length;
		char[] chars2 = new char[j];
		char[] chars3 = new char[j];
		
		//Rot13
		String alphabet = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
		int x = alphabet.length();
		for (int i = 0; i <= j-1; i++){
			for (int y = 0; y < x; y++){
				//System.out.println("[" + alphabet.charAt(y) + "] [" + chars[i] + "]");
				if (alphabet.charAt(y) == chars[i]){
					chars2[i] = alphabet.charAt((y+x+13)%x);
					System.out.println(chars2[i]);
					break;
				}
			}
		}
		
		
		//Text umdrehen
		for (int i = 0; i <= j-1; i++){
			chars3[j-1-i] = chars2[i];
		}
		return new String(chars3);
	}
}
