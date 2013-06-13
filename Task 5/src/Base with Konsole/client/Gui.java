package client; 

import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import javax.swing.JFrame; 
import javax.swing.JTextArea; 
import javax.swing.JTextField; /**
 * TODO description
 */
import java.util.Scanner; 

public 

class  Gui  extends JFrame  implements ChatLineListener {
	

	private static final long serialVersionUID = 1L;

	

	protected JTextArea outputTextbox;

	
	protected JTextField inputField;

	

	private static int rowstextarea = 20;

	
	private static int colstextarea = 60;

	
	private Client chatClient  ;

	
	
	public Gui  (String title, Client chatClient) {
		
	
		System.out.println("starting console...");
		chatClient.addLineListener(this);
		this.chatClient = chatClient;
		
		while (true) {
			Scanner scan = new Scanner(System.in);
			System.out.print(">");
			String line = scan.nextLine();
			
			chatClient.send(line);
		}
	}

	
	
	private ActionListener getInput() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chatClient.send(modString((String) inputField.getText()));
				inputField.setText("");
			}
		};
	}

	
	
	public String modString(String in){
		return in;
	}

	
	
	public void newChatLine  (String line) {
		System.out.print(line);
	}


}
