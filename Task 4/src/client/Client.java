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

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;

import common.Plugin;

import common.TextMessage;

/**
 * simple chat client
 */
public class Client implements Runnable {
	public static boolean log = false;
	public static boolean encryption = false;
	public static boolean color = false;
	public static boolean auth = false;
	public static boolean authBoolSent = false;
	public static boolean authBoolRec = false;
	public int ID = (int) (Math.random()*10000);
	Plugin plugin = null;
	String farbe = "schwarz";
	
	public static void main(String args[]) throws IOException {
		if (args.length == 2) {
			log = true;
			encryption = true;
			color = true;
			auth = true;
		} else if (args.length == 6){
			if (Integer.parseInt(args[2]) == 1){
				log = true;
			}
			if (Integer.parseInt(args[3]) == 1){
				encryption = true;
			}
			if (Integer.parseInt(args[4]) == 1){
				color = true;
			}
			if (Integer.parseInt(args[5]) == 1){
				auth = true;
			} else {
				authBoolRec = true;
			}
		} else {
			System.out.println("Syntax: ChatClient <host> <port>");
			System.out.println("oder");
			System.out.println("Syntax: ChatClient <host> <port> <log> <enryption> <color> <authentification>");
			System.out.println("Wobei die Argumente 3-6 optional sind und durch '1' -> 'aktiviert' und '0' -> 'deaktiviert' definiert sind");
			System.exit(0);
		}
		
		
		Client client = new Client(args[0], Integer.parseInt(args[1]), null);
		new Gui("Chat " + args[0] + ":" + args[1], client);
	}


	
	protected ObjectInputStream inputStream;

	protected ObjectOutputStream outputStream;

	protected Thread thread;

	public Client(String host, int port, Plugin plugin) {
		if (!auth) {
			authBoolRec = true;
		}
		try {
			System.out.println("Connecting to " + host + " (port " + port + ")...");
			Socket s = new Socket(host, port);
			this.outputStream = new ObjectOutputStream((s.getOutputStream()));
			this.inputStream = new ObjectInputStream((s.getInputStream()));
			this.plugin = plugin;
			thread = new Thread(this);
			thread.start();
			plugin.createGUI(host, port, this);
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
					if (authBoolSent != true){
						if (auth){
							sendAuthRequest();
						}
						authBoolSent = true;
					}
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

	
	public static boolean getLogBool(){
		return log;
	}
	
	public static boolean getAuthBool(){
		return auth;
	}
	
	public static boolean getEncBool(){
		return encryption;
	}
	
	public static boolean getColorBool(){
		return color;
	}
	
	/**
	 * decides what to do with incoming messages
	 * 
	 * @param msg
	 *            the message (Object) received from the sockets
	 */
	protected void handleIncomingMessage(Object msg) {
		if (msg instanceof TextMessage) {
			String text = decrypt(((TextMessage) msg).getContent());
			if (text.matches("(accepted,)(\\d*)")){
				String[] splitted = text.split(",");
				Integer recvID = Integer.parseInt(splitted[1]);
				if (recvID == ID) {
					authBoolRec = true;
					System.out.println("Authentification successful. Your ID is " + ID);
				}
			} else {
				fireAddLine(decrypt(((TextMessage) msg).getContent()) + "\n");
			}
		}
	}

	public void send(String line) {
		if (authBoolRec){
			send(new TextMessage(plugin.customize(line, farbe)));
		}else{
			System.out.println("No auth-token received. You can not send messages.");
		}
	}

	public void sendAuthRequest(){
		System.out.println("sending auth request");
		send(new TextMessage(encrypt("password," + ID)));
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
			
			line = plugin.customize(line, farbe);
			
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
		if (encryption){
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
		} else {
			return in;
		}
		
		
	}

	/**
	 * Methode zum entschlüsseln der Nachrichten
	 * @param in String
	 * @return verschlüsslter String
	 */
	public String encrypt(String in){
		if (encryption) {
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
						break;
					}
				}
			}
			
			
			//Text umdrehen
			for (int i = 0; i <= j-1; i++){
				chars3[j-1-i] = chars2[i];
			}
			return new String(chars3);
		} else {
			return in;
		}
	}
	
	public void logger(String text){
		if (log){
			try {
			    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log_client_" + ID + ".txt", true)));
			    out.println(text);
			    out.close();
			} catch (IOException e) {
	
			}
		}
	}
	

}
