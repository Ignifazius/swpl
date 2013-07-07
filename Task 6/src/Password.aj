import java.util.Scanner;

public aspect Password {
	pointcut password():
		execution(* client.Client.main(..));
	
	before() : password() {
		System.out.println("Bitte geben sie zuerst das Passwort ein! <DEBUG: password>");
		Scanner pw = new Scanner(System.in);
		String password = pw.nextLine();
		
		if (password.equals("password")){
			System.out.println("Korrektes Passwort. Starte Programm...");
		} else {
			System.out.println("Falsches Passwort. Beende Programm.");
			System.exit(0);
		}
		pw.close();
	}
}
