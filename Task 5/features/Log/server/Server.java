/**
 * TODO description
 */
class Server {
	public void broadcast(String text) {
		original(text);
		logger(text);
	}
	
	public void logger(String text){
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log_server.txt", true)));
			out.println(text);
			out.close();
		} catch (IOException e) {

		}
	}
}
