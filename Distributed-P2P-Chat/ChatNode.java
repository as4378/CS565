public class ChatNode {
	public static String myPort = "";
	public static String succPort = "";
	public static String myId = "";
	
	public static void main(String[] args) {
		System.out.println(args[0]);
		if(args[0].contentEquals("0")) {
			myId = "0";
			myPort = "3000";
		}
		else if(args[0].contentEquals("1")) {
			myId = "1";
			myPort = "3001";
		}
		else if(args[0].contentEquals("2")) {
			myId = "2";
			myPort = "3002";
		}
		System.out.println(ChatNode.myPort);
		Thread server = new Thread(new Server());
		server.start();
		Thread client = new Thread(new Client());
		client.start();
	}
}
