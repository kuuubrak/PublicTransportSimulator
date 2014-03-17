package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.Message;

public class Server 
{
	private ModuleNetwork gui;
	private ModuleNetwork passengers;
	private ModuleNetwork management;
	
	private ExecutorService executor;
	
	public Server()
	{
		gui = new ModuleNetwork(8700);
		passengers = new ModuleNetwork(8701);
		management = new ModuleNetwork(8702);
		executor = Executors.newCachedThreadPool();
	}
	
	public void createServer()
	{
		gui.connect();
		passengers.connect();
		management.connect();
	}
	
	/**
	 * Zwraca lokalny adres IP serwera
	 */
	public String getLocalAddress()
	{
		Enumeration< NetworkInterface > interfaces = null;
		try 
		{
			interfaces = NetworkInterface.getNetworkInterfaces();
		} 
		catch ( SocketException e ) 
		{
			e.printStackTrace();
		}
		
		// Odczytanie adresu IP z pierwszego znalezionego interfejsu,
		// ktory jest podlaczony do sieci
		while ( interfaces.hasMoreElements() )
		{
		    NetworkInterface current = interfaces.nextElement();
		    try 
		    {
				if (!current.isUp() || current.isLoopback() || current.isVirtual()) 
					continue;
			} catch (SocketException e) 
			{
				e.printStackTrace();
			}
		    
		    Enumeration< InetAddress > addresses = current.getInetAddresses();
		    
		    while ( addresses.hasMoreElements() )
		    {
		        InetAddress current_addr = addresses.nextElement();
		        if ( current_addr.isLoopbackAddress() ) 
		        	continue;

		        if ( current_addr instanceof Inet4Address )
		        {
			    	return current_addr.getHostAddress();
		        }
		    }
		    
		}
		// W przypadku braku interfejsu podlaczonego do sieci 
		// funkcja zwraca adres lokalny komputera.
		return "127.0.0.1";
	}
	
	private class ModuleNetwork
	{
		private Socket socket;
		private ServerSocket serverSocket;
		private final int port;
		
		ModuleNetwork(final int port)
		{
			this.port = port;
			try 
			{
				this.serverSocket = new ServerSocket(port);
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public Socket getSocket()
		{
			return socket;
		}
		
		public void connect()
		{
			final ModuleNetwork module = this;
			Runnable connecting = new Runnable()
			{
				@Override
				public void run()
				{
					try 
					{
						socket = serverSocket.accept();
						executor.execute(new Listener(module));
					} 
					catch (IOException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			executor.execute(connecting);
		}
	}
	
	private class Listener implements Runnable
	{
		private ModuleNetwork module;
		
		public Listener(final ModuleNetwork module)
		{
			this.module = module;
		}
		
		@Override
		public void run()
		{
			ObjectInputStream ois;
			
			while (isConnected(module.getSocket()))
			{
				try
				{
					ois = new ObjectInputStream(module.getSocket().getInputStream());
					Object object = ois.readObject();
					
					if( object instanceof Message )
					{
						Message message = (Message)object;
						switch(((Message)object).getDestination())
						{
							case GUI:
								send(message, gui.getSocket());
								break;
							case PASSENGERS:
								send(message, passengers.getSocket());
								break;
							case MANAGEMENT:
								send(message, management.getSocket());
								break;
							default:
								break;
						}
					}
				}
				catch ( Exception ex ) 
				{
					module.connect();
					break;
				}
			}
		}
	}

	public boolean send(final Message message, final Socket socket)
	{
		if(isConnected(socket))
		{
			ObjectOutputStream oos;
			try 
			{
				oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(message);
				return true;
			} 
			catch( Exception e ) 
			{
				//TODO
				e.printStackTrace();
			}
		}
		return false;
	}
	
	//TODO?
	private void echo(){}
	
	private boolean isConnected(final Socket socket)
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

	@SuppressWarnings("unused")
	private void closeConnection(Socket socket)
	{
		if(isConnected(socket))
		{
			try 
			{
				socket.close();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
