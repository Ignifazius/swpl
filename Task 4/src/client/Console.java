package client;


import java.util.Scanner;

import client.ChatLineListener;

/**
 * TODO description
 */
public class Console implements ChatLineListener{
	private Client chatClient;
	
	Console(Client chatClient) {
		chatClient.addLineListener(this);
		this.chatClient = chatClient;
		
		while (true) {
			Scanner scan = new Scanner(System.in);
			System.out.print(">");
			String line = scan.nextLine();
			
			chatClient.send(line);			
		}
	}
	
	
	
	/**
	 * this method gets called every time a new message is received (observer
	 * pattern)
	 */
	public void newChatLine(String line) {
		System.out.println(line);
	}
}