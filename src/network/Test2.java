package network;


public class Test2
{
	public static void main(String[] args)
	{
		System.out.println("Hello2");

		Client management = new Client();
		
		management.establishConnection("127.0.0.1", 8702);
		
		while(true)
		{
			management.send(new String("Wiadomosc3"));
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
