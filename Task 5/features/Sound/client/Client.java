/**
 * TODO description
 */
import java.awt.Toolkit;

class Client {
	protected void handleIncomingMessage(Object msg) {
		original(msg);
		Toolkit.getDefaultToolkit().beep();
	}
	
	public void send(String line) {
		original(line);
		Toolkit.getDefaultToolkit().beep();
	}
}
