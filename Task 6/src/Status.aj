import java.util.HashMap;
import java.util.Iterator;

public aspect Status {

	HashMap<Integer,String> chatList = new HashMap<Integer,String>();
	
	   pointcut catchStatus(String line) : call(* client.Client.fireAddLine(String)) && args(line);

	   
	   before(String line) : catchStatus(line) {
			if (line.contains("\\setStatus")){
				String[] lineSplit = line.split(" ");
				int ID = Integer.parseInt(lineSplit[2]);
				String status = lineSplit[4];
				System.out.println("Changing Status of Client " + ID + " to " + status);
				chatList.put(ID, status);
			}
			if (line.contains("\\getStatus")){
				getStatus();
			}
	   }
	   
	   public void getStatus(){
			Iterator<Integer> iterator = chatList.keySet().iterator();  
			   
			while (iterator.hasNext()) {  
			   int key = iterator.next();  
			   String value = chatList.get(key); 
			   
			   System.out.println(key + " " + value);  
			}  
	   }
}
