package common;

import client.Client;
import client.Client_v2;
import client.Client_v3;

public class Starter {
	
	static Plugin plugin = new Client_v2();
	//static Plugin plugin = new Client_v3();
	
	public static void main(String[] args) {
//		if (args.length < 2) {
//			System.out.println("error");
//		} else {
			//Client client = new Client(args[0], Integer.parseInt(args[1]), plugin);
			Client client = new Client("localhost", 8081, plugin);
//		}
	}
}
