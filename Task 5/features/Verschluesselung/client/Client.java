/**
 * TODO description
 */
package client;

class Client{
	public void send(String line) {
		String enc = encrypt(line);
		original(enc);
	}
	
	public void fireAddLine(String line) {
		original(encrypt(line));
	}
	
	
	private String encrypt(String in){
		String out = "";
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            if       (c >= 'a' && c <= 'm') c += 13;
            else if  (c >= 'A' && c <= 'M') c += 13;
            else if  (c >= 'n' && c <= 'z') c -= 13;
            else if  (c >= 'N' && c <= 'Z') c -= 13;
            out += c;
        }
        return out;
	}
}
