package client;

import java.awt.Toolkit;
import java.util.Scanner;
import java.io.File;

/**
 * TODO description
 */
public class Client {
	
	public void send(String line) {
		String filteredLine = filter(line);
		original(filteredLine);
		
	}
	
	public void fireAddLine(String line) {
		String filteredLine = filter(line);
		original(filteredLine);
	}
	
	public String filter(String text){
		ArrayList<String> filterlist = new ArrayList<String>();
		try {
			Scanner scanner = new Scanner(new File("sperrliste.txt"));
			while (scanner.hasNextLine()) {
				filterlist.add(scanner.nextLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (String word : filterlist){
			text = text.replace(word, "****");
		}
		return text;
	}
}