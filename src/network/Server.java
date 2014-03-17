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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.Message;

public class Server 
{
	private final int guiPort;
	private final int passangersPort;
	private final int managementPort;
	private ServerSocket guiServerSocket;
	private ServerSocket passangersServerSocket;
	private ServerSocket managementServerSocket;
	private Socket guiSocket;
	private Socket passengersSocket;
	private Socket managementSocket;
	private ExecutorService executor;
	
	public Server()
	{
		guiPort = 8700;
		passangersPort = 8701;
		managementPort = 8702;
		executor = Executors.newCachedThreadPool();
	}
	
	public void createServer()
	{
		try 
		{
			guiServerSocket = new ServerSocket(guiPort);
			passangersServerSocket = new ServerSocket(passangersPort);
			managementServerSocket = new ServerSocket(managementPort);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connectGUI();
		connectPassengersModule();
		connectManagementModule();
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
	
	
	private void connectGUI()
	{
		closeConnection(guiSocket);
		Runnable connecting = new Runnable()
		{
			@Override
			public void run()
			{
				try 
				{
					guiSocket = guiServerSocket.accept();
					executor.execute(listenGUI);
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

	private void connectPassengersModule()
	{
		closeConnection(passengersSocket);
		Runnable connecting = new Runnable()
		{
			@Override
			public void run()
			{
				try 
				{
					passengersSocket = passangersServerSocket.accept();
					executor.execute(listenPassengersModule);
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

	private void connectManagementModule()
	{
		closeConnection(managementSocket);
		Runnable connecting = new Runnable()
		{
			@Override
			public void run()
			{
				try 
				{
					managementSocket = managementServerSocket.accept();
					executor.execute(listenManagementModule);
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
	
	private Runnable listenGUI = new Runnable()
	{
		@Override
		public void run()
		{
			ObjectInputStream ois;
			
			while ( isConnected(guiSocket) )
			{
				try
				{
					ois = new ObjectInputStream(guiSocket.getInputStream());
					Object object = ois.readObject();
					
					if( object instanceof Message )
					{
						Message message = (Message)object;
						switch(((Message)object).getDestination())
						{
							case PASSENGERS_MODULE:
								send(message, passengersSocket);
								break;
							case MANAGEMENT_MODULE:
								send(message, managementSocket);
								break;
							default:
								break;
						}
					}
				}
				catch ( Exception ex ) 
				{
					connectGUI();
					break;
				}
			}
		}
	};

	private Runnable listenPassengersModule = new Runnable()
	{
		@Override
		public void run()
		{
			ObjectInputStream ois;
			while ( isConnected(passengersSocket) )
			{
				try
				{
					ois = new ObjectInputStream(passengersSocket.getInputStream());
					Object object = ois.readObject();
					
					if( object instanceof Message )
					{
						Message message = (Message)object;
						switch(((Message)object).getDestination())
						{
							case GUI:
								send(message, guiSocket);
								break;
							case MANAGEMENT_MODULE:
								send(message, managementSocket);
								break;
							default:
								break;
						}
					}
				}
				catch ( Exception ex ) 
				{
					connectPassengersModule();
					break;
				}
			}
		}
	};

	private Runnable listenManagementModule = new Runnable()
	{
		@Override
		public void run()
		{
			ObjectInputStream ois;
			
			while ( isConnected(managementSocket) )
			{
				try
				{
					ois = new ObjectInputStream(managementSocket.getInputStream());
					Object object = ois.readObject();
					
					if( object instanceof Message )
					{
						Message message = (Message)object;
						switch(((Message)object).getDestination())
						{
							case GUI:
								send(message, guiSocket);
								break;
							case PASSENGERS_MODULE:
								send(message, passengersSocket);
								break;
							default:
								break;
						}
					}
				}
				catch ( Exception ex ) 
				{
					connectManagementModule();
					break;
				}
			}
		}
	};

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
