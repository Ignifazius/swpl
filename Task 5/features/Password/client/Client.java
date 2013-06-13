package client;

import java.io.IOException;
import java.util.Scanner;

/**
 * TODO description
 */
public class Client {
	public static void main(String args[]) throws IOException {
		if (args.length != 2)
			throw new RuntimeException("Syntax: ChatClient <host> <port>");
		
		System.out.println("Bitte geben sie zuerst das Passwort ein! <DEBUG: password>");
		Scanner pw = new Scanner(System.in);
		String password = pw.nextLine();
		
		if (password.equals("password")){
			Client client = new Client(args[0], Integer.parseInt(args[1]));
			new Gui("Chat " + args[0] + ":" + args[1], client);
		} else {
			System.out.println("Falsches Passwort. Beende Programm.");
		}
	}
}