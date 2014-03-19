package network;

import network.Client.MODULE;

public class Test
{
	public static void main(String[] args)
	{
		System.out.println("Hello");
		Server server = new Server();
		server.createServer();
		
		Client gui = new Client(MODULE.GUI);
		Client passengers = new Client(MODULE.PASSENGERS);
		Client management = new Client(MODULE.MANAGEMENT);
		
		gui.establishConnection("127.0.0.1");
		passengers.establishConnection("127.0.0.1");
		management.establishConnection("127.0.0.1");
		
		while(true)
		{
			gui.send(new String("Wiadomosc1"));
			passengers.send(new String("Wiadomosc2"));
			management.send(new String("Wiadomosc3"));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
