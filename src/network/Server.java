package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa serwera.
 * Zajmuje sie rozsylaniem otrzymanych obiektow do odpowiednich modulow.
 * 
 * @author Maciej Korpalski
 */
// Dodałem symulację awarii ~maciej168
// Dorzuciłem Loggery. Czemu? Bo można je zajebiście filtrować. W kodzie używającym tej klasy wystarczy że walniesz
// Logger.getLogger(Server.class .getName()).setLevel(Level.OFF) i już nie widać żadnych komunikatów. Cudny mechanizm, polecam
// ~maciej168
// TODO Odfiltrować polecenie z GUI o symulacji awarii i wykonać.
// TODO? Zmienić nazwę wszystkich Loggerów na wspólną dla całego serwera
public class Server 
{
	/** Obiekt reprezentujacy modul GUI */
	private ModuleNetwork gui;
	/** Obiekt reprezentujacy modul obslugi pasazerow */
	private ModuleNetwork passengers;
	/** Obiekt reprezentujacy modul zarzadzania komunikacja miejska */
	private ModuleNetwork management;
	
	private ExecutorService executor;
	
	/** 
	 * Tworzy moduly i przypisuje im odpowiednie porty sieciowe. 
	 * @throws IOException 
	 */
	public Server(int guiPort, int passengersPort, int managementPort) throws IOException
	{
		try 
		{
			gui = new ModuleNetwork(guiPort);
			passengers = new ModuleNetwork(passengersPort);
			management = new ModuleNetwork(managementPort);
		} 
		catch(IOException e) 
		{
                        Logger.getLogger(ModuleNetwork.class.getName()).log(Level.SEVERE, "Błąd tworzenia Serwera"); // dodałem ~maciej168
			throw e;
		}
		
		executor = Executors.newCachedThreadPool();
	}
	
	/**
	 * Otwiera polaczenia dla wszystkich modulow,
	 * przypisuje kazdemu modulowi odpowiadajacych mu odbiorcow.
	 */
	public void createServer()
	{
		gui.connect();
		passengers.connect();
		management.connect();
		
		gui.addReceiver(passengers);
		passengers.addReceiver(gui);
		passengers.addReceiver(management);
		management.addReceiver(passengers);
	}
	
	/**
	 * Klasa reprezentujaca wlasciwosci sieciowe pojedynczego modulu
	 */
	private class ModuleNetwork
	{
		/** Socket klienta */
		private Socket socket;
		/** Socket serwera */
		private ServerSocket serverSocket;
		/** Lista odbiorcow, ktorym nalezy przekazywac pakiety */
		private List<ModuleNetwork> receivers;
		/** Strumien wyjsciowy dla tego modulu */
		private ObjectOutputStream oos;
                /** Handle klasy roboczej. Potrzebny do symulowania awarii ~maciej168 */
                private Listener currentListener;
		
		ModuleNetwork(final int port) throws IOException
		{
			oos = null;
			receivers = new ArrayList<ModuleNetwork>();
			try 
			{
				this.serverSocket = new ServerSocket(port);
			}
			catch(IOException e) 
			{
                                Logger.getLogger(ModuleNetwork.class.getName()).log(Level.SEVERE, "Błąd tworzenia ServerSocket'u. Pewnie port jest zajęty."); // dodałem ~maciej168
				throw e;
			}
		}
				
		public Socket getSocket()
		{
			return socket;
		}
		
		public List<ModuleNetwork> getReceivers()
		{
			return receivers;
		}
		
		public ObjectOutputStream getObjectOutputStream()
		{
			return oos;
		}
		
		/**
		 * Otwiera polaczenie i oczekuje na klienta.
		 * Po ustanowieniu polaczenia uruchamia dla tego modulu watek odbierajacy pakiety.
		 */
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
						closeConnection(socket);
						socket = serverSocket.accept();
						oos = new ObjectOutputStream(socket.getOutputStream());
						//executor.execute(new Listener(module)); // zmiana ~maciej168
                                                currentListener = new Listener(module);
						executor.execute(currentListener);
					} 
					catch(IOException e) 
					{
						// e.printStackTrace(); //zmieniłem ~maciej168
                                                Logger.getLogger(ModuleNetwork.class.getName()).log(Level.WARNING, "Błąd tworzenia Listener'a", e);
						throw new RuntimeException();
					}
				}
			};
			executor.execute(connecting);
		}
		
		/**
		 * Dodaje nowego odbiorce dla tego modulu.
		 * @param module
		 */
		public void addReceiver(final ModuleNetwork module)
		{
			receivers.add(module);
		}
                
                /**
                 * Symulacja awarii modułu sieciowego.
                 * ~maciej168
                 */
                public void clog(boolean makeClogged)
                {
                    currentListener.clog(makeClogged);
                }
                
	}

	/**
	 * Klasa reprezentujaca watek odbierajacy pakiety i przekazujacy je do odpowiednich modulow.
	 */
	private class Listener implements Runnable
	{
		private ModuleNetwork module;
		private ObjectInputStream ois;
                
                /** Flaga symulowanej awarii ~maciej168 */
                private boolean clogged = false;
		
		public Listener(final ModuleNetwork module)
		{
			try 
			{
				ois = new ObjectInputStream(module.getSocket().getInputStream());
			} 
			catch(IOException e) 
			{
				//e.printStackTrace();//zmieniłem ~maciej168
                                Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, "Błąd tworzenia strumienia", e);
				throw new RuntimeException();
			}
			this.module = module;
		}
		
		@Override
		public void run()
		{
			while(isConnected(module.getSocket()))
			{
				try
				{
					Object object = ois.readObject();
					System.out.println("Serwer: " + (String)object); // Nie wiem czy to o reprezentację obiektu Ci Maćku
                                                                                         // chodziło czy nazwę klasy która przyszła.
                                                                                         // Jeśli o to drugie to zmień na object.getClass().getName()
                                                                                         // czy jakieś inne Name. Moja propozycja zakomentowana poniżej. ~maciej168
                                        //Logger.getLogger(Server.class.getName()).log(Level.FINEST, "Serwer: " + object.getClass().getName());
                                        
					// Rozsyla obiekt do wszystkich odbiorcow danego modulu.
                                        if(!clogged)// Jak nie ma symulowanej awarii. ~maciej168
                                        {
                                            for(ModuleNetwork receiver : module.getReceivers())
                                            {
                                                    send(object, receiver);
                                            }
                                        }
				} 
				catch(IOException e) 
				{
                                        Logger.getLogger(Listener.class.getName()).log(Level.FINER, "Ponownie łączenie", e);
					module.connect();
					break;
				}
				catch(ClassNotFoundException e)
				{
					// Nierozpoznane klasy sa ignorowane
					//e.printStackTrace(); //zmieniłem ~maciej168
                                        Logger.getLogger(Listener.class.getName()).log(Level.WARNING, "Ignorowanie nieznanej klasy", e);
				}
			}
		}
                
                /**
                 * Symuluje awarię (nie przesyła rozkazów/wiadomości do odbiorców)
                 * ~maciej168
                 */
                public void clog(boolean makeClogged)
                {
                    clogged = makeClogged;
                }
	}

	/**
	 * Wysyla obiekt do konkretnego socketa
	 * @param object
	 * @param socket
	 * @return true if sent successfully, false if not
	 * @throws IOException 
	 */
	private synchronized void send(final Object object, ModuleNetwork receiver) throws IOException
	{
		if(isConnected(receiver.getSocket()))
		{
			try 
			{
				receiver.getObjectOutputStream().writeObject(object);
			} 
			catch( IOException e ) 
			{
                                Logger.getLogger(Server.class.getName()).log(Level.WARNING, "Błąd wysyłania"); // dodałem ~maciej168
				throw e;
			}
		}
	}

	/**
	 * Informuje czy z socketem jest polaczony jakis klient.
	 * @param socket
	 * @return
	 */
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

	/**
	 * Rozlacza klienta z konkretnego socketa.
	 * @param socket
	 */
	private void closeConnection(Socket socket)
	{
		if(isConnected(socket))
		{
			try 
			{
				socket.close();
			} 
			catch(IOException e) 
			{
				//e.printStackTrace(); //zmieniłem ~maciej168
                                Logger.getLogger(Server.class.getName()).log(Level.WARNING, "Błąd przy zamykaniu połączenia",e);
				throw new RuntimeException();
			}
		}
	}
}
