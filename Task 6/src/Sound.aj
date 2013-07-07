import java.awt.Toolkit;

public aspect Sound {
	pointcut sound():
		execution(* client.Client.fireAddLine(..));
	
	after() returning: sound() {
		Toolkit.getDefaultToolkit().beep();
	}

}
