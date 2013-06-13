/**
 * TODO description
 */
class Gui {
	private Client chatClient;
	
	Gui(String title, Client chatClient) {
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
	
	public void newChatLine(String line) {
		System.out.println(line);
	}
}
