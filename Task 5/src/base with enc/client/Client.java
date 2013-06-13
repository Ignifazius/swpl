/**
 * TODO description
 */
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

import common.TextMessage; /**
 * TODO description
 */
import java.awt.Toolkit; 

public 

class  Client  implements Runnable {
	
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
			System.out.println("Connecting to " + host + " (port " + port
					+ ")...");
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
	 private void  handleIncomingMessage__wrappee__Log  (Object msg) {
		if (msg instanceof TextMessage) {
			fireAddLine(((TextMessage) msg).getContent() + "\n");
		}
	}

	
	protected void handleIncomingMessage(Object msg) {
		handleIncomingMessage__wrappee__Log(msg);
		Toolkit.getDefaultToolkit().beep();
	}

	

	 private void  send__wrappee__Log  (String line) {
		send(new TextMessage(line));
	}

	
	
	 private void  send__wrappee__Sound  (String line) {
		send__wrappee__Log(line);
		Toolkit.getDefaultToolkit().beep();
	}

	
	public void send(String line) {
		String enc = encrypt(line);
		send__wrappee__Sound(enc);
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

	
	 private void  fireAddLine__wrappee__Sound  (String line) {
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
			ChatLineListener listener = (ChatLineListener) iterator.next();
			listener.newChatLine(line);
			logger(line);
		}
	}

	
	
	public void fireAddLine(String line) {
		fireAddLine__wrappee__Sound(encrypt(line));
	}

	

	public void stop() {
		thread = null;
	}

	

	public void logger(String text){
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log_client.txt", true)));
			out.println(text);
			out.close();
		} catch (IOException e) {

		}
	}

	
	
	
	private String encrypt(String in){
		String out = "";
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            if       (c >= 'a' && c <= 'm') c += 13;
            else if  (c >= 'A' && c <= 'M') c += 13;
            else if  (c >= 'n' && c <= 'z') c -= 13;
            else if  (c >= 'N' && c <= 'Z') c -= 13;
            out += c;
        }
        return out;
	}


}
