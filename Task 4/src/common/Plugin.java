package common;

import client.Client;



public interface Plugin {
	boolean log = false;
	boolean encryption = false;
	boolean color = false;
	boolean auth = false;
	TextMessage send(String in);
	String customize(String in, String farbe);
	void createGUI(String host, int port, Client client);
}
