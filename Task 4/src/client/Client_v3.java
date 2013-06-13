package client;

import common.Plugin;
import common.SpamFilter;
import common.TextMessage;

public class Client_v3 implements Plugin {

	@Override
	public TextMessage send(String in) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String customize(String in, String farbe) {
		in = SpamFilter.filter(in);
		return in;
	}

	@Override
	public void createGUI(String host, int port, Client client) {
		new Console(client);
	}

}
