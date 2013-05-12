package client;

import java.io.IOException;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import common.TextMessage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * simple chat client
 */
public class Client implements Runnable {
	public int ID = (int) (Math.random()*10000);
	
	boolean authBoolSent = false;
	boolean authBoolRec = false;
	
	
	public static void main(String args[]) throws IOException {
		Client client = new Client(args[0], Integer.parseInt(args[1]));
		


		
			new Console(client);
		
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
			Scanner scan = new Scanner(System.in);
			while (thread == thisthread) {
				try {
					Object msg = inputStream.readObject();
					handleIncomingMessage(msg);
					
					if (authBoolSent != true){
						sendAuthRequest();
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
			
				fireAddLine(text + "\n");
				//System.out.println("<" + text);
			
				}
			
		}
	}

	public void send(String line) {
		
		if (authBoolRec){
		
			send(new TextMessage(encrypt(line)));
		
		}else{
			System.out.println("No auth-token received. You can not send messages.");
		}
		
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
			


			
			System.out.print(line);	
			System.out.print(">");	
			
			logger(line);
		}
	}

	public void stop() {
		thread = null;
	}
	
	/**
	 * Methode zum verschl�sseln der Nachrichten
	 * @param in verschl�sselter String
	 * @return entschl�sslter String
	 */
	public String decrypt(String in) {
		String out = in;
		
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
		
			return out;
	}

	/**
	 * Methode zum entschl�sseln der Nachrichten
	 * @param in String
	 * @return verschl�sslter String
	 */
	public String encrypt(String in){
		String out = in;
		
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
		
			return out;
		
	}
	
	
	public void newChatLine(String line) {
		System.out.println(" < " + line);
		//System.out.print(">");
	}
	
	public void logger(String text){
		
			try {
			    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log_client_" + ID + ".txt", true)));
			    out.println(text);
			    out.close();
			} catch (IOException e) {
	
			}
		
	}
	
	

//	class ListenFromServer extends Thread {
//		public void run() {
//			while(true) {
//				try {
//					String msg = (String) inputStream.readObject();
//					System.out.println(msg);
//					System.out.print("> ");
//				}
//				catch(IOException e) {
//					System.out.println("Server has close the connection: " + e);
//				}
//				// can't happen with a String object but need the catch anyhow
//				catch(ClassNotFoundException e2) {
//				}
//			}
//		}
//	}

}

