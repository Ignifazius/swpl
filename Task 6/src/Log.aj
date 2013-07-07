import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public aspect Log {
	
   pointcut log(String line) : call(* client.Client.fireAddLine(String)) && args(line);

	   before(String line) : log(line) {
		   logger(line);
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
