package network;

import java.io.IOException;


public class Test
{
	public static void main(String[] args)
	{
		System.out.println("Hello");
		Server server = null;
		try {
			server = new Server(8700, 8701, 8702);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		server.createServer();
		
		Client gui = new Client();
		Client passengers = new Client();
		
		gui.establishConnection("127.0.0.1", 8700);
		passengers.establishConnection("127.0.0.1", 8701);
		
		while(true)
		{
			gui.send(new String("Wiadomosc1"));
			passengers.send(new String("Wiadomosc2"));
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
