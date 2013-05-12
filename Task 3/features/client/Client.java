package client;

import java.io.IOException;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import common.TextMessage;

/**
 * simple chat client
 */
public class Client implements Runnable {
	public int ID = (int) (Math.random()*10000);
	/*if[Authentification]*/
	boolean authBoolSent = false;
	boolean authBoolRec = false;
	/*end[Authentification]*/
	
	public static void main(String args[]) throws IOException {

		/*if[GUI]*/
			Client client = new Client(args[0], Integer.parseInt(args[1]));
			new Gui("Chat " + args[0] + ":" + args[1], client);
		/*end[GUI]*/
		/*if[Konsole]*/
		//TODO
		/*end[Konsole]*/
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
		} catch (ConnectException ce) {
			System.out.println("Could not connect. Is the Server running?");
		}
		catch (Exception e) {
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
					/*if[Authentification]*/
					if (authBoolSent != true){
						sendAuthRequest();
						authBoolSent = true;
					}
					/*end[Authentification]*/
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
			String text = decrypt(((TextMessage) msg).getContent());
			/*if[Authentification]*/
			if (text.matches("(accepted,)(\\d*)")){
				String[] splitted = text.split(",");
				Integer recvID = Integer.parseInt(splitted[1]);
				if (recvID == ID) {					
					authBoolRec = true;
					System.out.println("Authentification successful. Your ID is " + ID);
				}
			} else {
			/*end[Authentification]*/
				fireAddLine(decrypt(((TextMessage) msg).getContent()) + "\n");
			/*if[Authentification]*/
				}
			/*end[Authentification]*/
		}
	}

	public void send(String line) {
		/*if[Authentification]*/
		if (authBoolRec){
		/*end[Authentification]*/
			send(new TextMessage(encrypt(line)));
		/*if[Authentification]*/
		}else{
			System.out.println("No auth-token received. You can not send messages.");
		}
		/*end[Authentification]*/
	}

	public void sendAuthRequest(){
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
		String out = in;
		/*if[Encryption]*/
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
			out = new String(chars3);			
		/*end[Encryption]*/
			return out;
	}

	/**
	 * Methode zum entschlüsseln der Nachrichten
	 * @param in String
	 * @return verschlüsslter String
	 */
	public String encrypt(String in){
		String out = in;
		/*if[Encryption]*/
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
			out = new String(chars3);
		/*end[Encryption]*/
			return out;
		
	}
	
	public void logger(String text){
		/*if[Log]*/
			try {
			    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log_client_" + ID + ".txt", true)));
			    out.println(text);
			    out.close();
			} catch (IOException e) {
	
			}
		/*end[Log]*/
	}
	

}