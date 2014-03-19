package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Klient przesylajacy i odbierajacy obiekty.
 * @author Maciej Korpalski
 */
public class Client
{
	/** Port serwera */
	private final int serverPort;
	/** Socket dla serwera */
	private Socket socket;
	/** Strumien wyjsciowy */
	private ObjectOutputStream oos;
	
	public enum MODULE
	{
		GUI,
		PASSENGERS,
		MANAGEMENT;
		
		public int getPort()
		{
			return ordinal() + 8700;
		}
	}
	
	public Client(final MODULE module)
	{
		serverPort = module.getPort();
		socket = new Socket();
	}
	
	/**
	 * Laczy z serwerem i uruchamia watek odbierajacy obiekty.
	 * @param address - adres ip serwera
	 * @return boolean - polaczenie udane/nieudane
	 */
	public boolean establishConnection(final String address)
	{
		try
		{
			socket = new Socket();
			socket.connect(new InetSocketAddress(address, serverPort), 5000);
			oos = new ObjectOutputStream(socket.getOutputStream());
			new Thread(receive).start();
			return true;
		}
		catch (Exception e) 
		{
			closeConnection();
			return false;
		}
	}
	
	/**
	 * Wysyla obiekt do serwera
	 * @param message
	 * @return
	 */
	public boolean send(final Object object)
	{
		if(socket.isBound())
		{
			try 
			{
				oos.writeObject(object);
				return true;
			} 
			catch(Exception e) 
			{
				//TODO
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * Zamkniecie polaczenie z serwerem
	 */
	public void closeConnection()
	{
		if(socket.isBound())
		{
			try
			{
				socket.close();
			}
			catch(IOException e)
			{
				//TODO
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Implementacja watku odbierajacego obiekty od serwera
	 */
	private Runnable receive = new Runnable()
	{
		@Override
		public void run()
		{
			ObjectInputStream ois = null;
			try 
			{
				ois = new ObjectInputStream(socket.getInputStream());
			}
			catch (IOException e) 
			{
				// TODO
				e.printStackTrace();
				throw new RuntimeException();
			}
			while (!socket.isClosed() && ois != null)
			{
				try 
				{
					Object object = ois.readObject();
					if(object instanceof String)
					{
						System.out.println(serverPort + ":   " + (String)object);
					}
					//if( object instanceof Order )
					//{
					//	send order
					//}
				} 
				catch (ClassNotFoundException | IOException e) 
				{
					//TODO
					e.printStackTrace();
					throw new RuntimeException();
				}	
			}
		}
	};
}
