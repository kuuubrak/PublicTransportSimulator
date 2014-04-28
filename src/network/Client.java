package network;

import Order.Order;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klient przesylajacy i odbierajacy obiekty.
 * @author Maciej Korpalski
 */
public class Client
{
	/** Socket dla serwera */
	private Socket socket;

    private BlockingQueue<Order> ordersQueue;
	
	public Client()
	{
		socket = new Socket();
        ordersQueue = new LinkedBlockingQueue<Order>();
	}
	
	/**
	 * Laczy z serwerem i uruchamia watek odbierajacy obiekty.
	 * @param address - adres ip serwera
	 * @return boolean - polaczenie udane/nieudane
	 */
	public boolean establishConnection(final String address, int serverPort)
	{
		try
		{
			closeConnection();
			socket.connect(new InetSocketAddress(address, serverPort), 5000);
			new Thread(receive).start();
			return true;
		}
		catch(Exception e) 
		{
                        Logger.getLogger(Client.class.getName()).log(Level.FINE, "Błąd łączenia z serwerem"); // dodałem ~maciej168
			closeConnection();
			return false; // buuu, Macieju. buuuuuuuuuuuuu. Toż to aktywne oczekiwanie wyjdzie w użytkowaniu.
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
			ObjectOutputStream oos = null;
			try 
			{
				oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(object);
				return true;
			}
			catch(IOException e) 
			{
                Logger.getLogger(Client.class.getName()).log(Level.WARNING, "Błąd wysyłania do serwera"); // dodałem ~maciej168
				e.printStackTrace();
                closeConnection();
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Zamkniecie polaczenia z serwerem
	 */
	public void closeConnection()
	{
		if(isConnected())
		{
			try
			{
				socket.close();
			}
			catch(IOException e)
			{
				socket = new Socket();
			}
		}
		else
		{
			socket = new Socket();
		}
	}
	
	/**
	 * Informuje czy z socketem jest polaczony jakis serwer.
	 * @param socket
	 * @return
	 */
	private boolean isConnected()
	{
		if(socket == null)
		{
			return false;
		}
		else
		{
			return socket.isBound();
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
			while(!socket.isClosed() && ois != null)
			{
				try 
				{
					ois = new ObjectInputStream(socket.getInputStream());
					Object object = ois.readObject();

					System.out.println("Dostalem: " + object.getClass());
					if(object instanceof String)
					{
                        ordersQueue.add((Order)object);
					//	System.out.println(socket.getPort() + ":   " + (String)object);
					}
					//if( object instanceof Order )
					//{
					//	send order
					//}
				} 
				catch(IOException e)
				{
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Błąd odbierania z serwera", e); // dodałem ~maciej168
					closeConnection();
					throw new RuntimeException();
				}
				catch(ClassNotFoundException e) 
				{
					// Nierozpoznane klasy sa ignorowane
					//e.printStackTrace(); //zmieniłem ~maciej168
                                        Logger.getLogger(Client.class.getName()).log(Level.WARNING, "Ignorowanie nieznanej klasy", e);
				}	
			}
		}
	};


    public final BlockingQueue<Order> getOrdersQueue()
    {
        return ordersQueue;
    }

}
