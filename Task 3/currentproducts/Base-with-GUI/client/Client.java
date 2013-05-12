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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * simple chat client
 */
public class Client implements Runnable {
	public int ID = (int) (Math.random()*10000);
	



	
	public static void main(String args[]) throws IOException {
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
			









				fireAddLine(decrypt(((TextMessage) msg).getContent()) + "\n");
			


		}
	}

	public void send(String line) {
		


			send(new TextMessage(encrypt(line)));
		




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
		























			return out;
	}

	/**
	 * Methode zum entschlüsseln der Nachrichten
	 * @param in String
	 * @return verschlüsslter String
	 */
	public String encrypt(String in){
		String out = in;
		

























			return out;
		
	}
	
	public void logger(String text){
		








	}
	

}