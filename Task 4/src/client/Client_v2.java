package client;

import common.Plugin;
import common.SpamFilter;
import common.TextMessage;

public class Client_v2 implements Plugin{
	boolean log = false;
	boolean encryption = false;
	boolean color = false;
	boolean auth = false;
	boolean filter = true;
	@Override
	public TextMessage send(String in) {
		return new TextMessage(customize(in, "invis"));
	}

	@Override
	public String customize(String in, String farbe) {
		if (filter) {
			in = SpamFilter.filter(in);
		}
		return encrypt(farbe  + " | "+ in);
	}

	@Override
	public void createGUI(String host, int port, Client client) {
		new Gui("Chat " + host + ":" + port, client);	
	}

	public String decrypt(String in) {
		if (encryption){
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
			return new String(chars3);
		} else {
			return in;
		}
		
		
	}

	/**
	 * Methode zum entschlüsseln der Nachrichten
	 * @param in String
	 * @return verschlüsslter String
	 */
	public String encrypt(String in){
		if (encryption) {
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
			return new String(chars3);
		} else {
			return in;
		}
	}	
}
