package network;

import java.io.IOException;

public class Test {
	public static void main(String args[])
	{
		try {
			Server server = new Server(8123, 8124, 8125);
			server.createServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
