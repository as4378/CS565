import java.net.*;
import java.io.*;
import java.util.*; 

public class Client implements Runnable {
	private BufferedReader inp;
	
    private static ArrayList<String> ports = new ArrayList<String>(){{add("3000"); add("3001"); add("3002");}};
	
	public boolean Connect(int port, String action, String message) {
		Socket newSocket = null;
		DataOutputStream outNew = null;
		BufferedReader servInpNew = null;
		try {
			newSocket = new Socket("127.0.0.1", port);
			outNew = new DataOutputStream(newSocket.getOutputStream());
			servInpNew = new BufferedReader(new InputStreamReader(newSocket.getInputStream()));
			if(action.contentEquals("Join")){
				String toSend = ChatNode.myId + "#Join";
				outNew.writeBytes(toSend + "\n");
				String response = servInpNew.readLine();
				System.out.println(response);
				String[] parts = response.split("#");
				ChatNode.succPort = parts[2];
			}
		}
		catch(UnknownHostException e) {
			System.out.println(e);
			return false;
		}
		catch(IOException e) {
			System.out.println(e);
			return false;
		}
		try {
			outNew.close();
			servInpNew.close();
			newSocket.close();
		}
		catch(IOException e) {
			System.out.println(e);
			return false;
		}
		return true;
	}
	
	public boolean Join() {
		System.out.println(ChatNode.myPort);
		for(String p: ports) {
			if(p != ChatNode.myPort) {
				if(Connect(Integer.parseInt(p), "Join", "")) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void CloseConnection() {
		try {
			inp.close();
		}
		catch(IOException e) {
			System.out.println(e);
		}
	}
	
	public void Forward(String message) {
		System.out.println(ChatNode.myPort);
		try {
			String toSend = ChatNode.myId + "#Broad#" + message;
			if(!ChatNode.succPort.contentEquals("")){
				Socket socket = new Socket("127.0.0.1", Integer.parseInt(ChatNode.succPort));
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				
				BufferedReader servInp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out.writeBytes(toSend);
				
				out.close();
				servInp.close();
				socket.close();
			}
		}
		catch(IOException e) {
			System.out.println(e);
		}
	}
	
	public void run() {
		this.inp = new BufferedReader(new InputStreamReader(System.in));
		String line = "";
		while(!line.contentEquals("quit")) {
			try {
				line = inp.readLine();
				if(line.contentEquals("Join")){
					if(!Join()) {
						System.out.println("You're the first one here");
					}
					else {
						System.out.println("Joined the chat with other guests");
					}
				}
				else if(!line.contentEquals("quit")) {
					Forward(line);
				}		
			}
			catch(IOException e) {
				System.out.println(e);
			}
		}
		CloseConnection();
	}
}
