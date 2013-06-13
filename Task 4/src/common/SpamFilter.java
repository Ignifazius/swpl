package common;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class SpamFilter {
	public static String filter(String text){
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
