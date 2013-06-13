/**
 * TODO description
 */
class Client {
	public void fireAddLine(String line) {
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
			ChatLineListener listener = (ChatLineListener) iterator.next();
			listener.newChatLine(line);
			logger(line);
		}
	}

	public void logger(String text){
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log_client.txt", true)));
			out.println(text);
			out.close();
		} catch (IOException e) {

		}
	}
}
