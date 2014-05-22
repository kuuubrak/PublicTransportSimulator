package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Klasa reprezentujaca wlasciwosci sieciowe pojedynczego modulu
 */
class ModuleNetwork {
    /**
     * Socket klienta
     */
    private Socket socket;
    /**
     * Socket serwera
     */
    private ServerSocket serverSocket;
    /**
     * Lista odbiorcow, ktorym nalezy przekazywac pakiety
     */
    private List<ModuleNetwork> receivers;
    /**
     * Handle klasy roboczej. Potrzebny do symulowania awarii ~maciej168
     */
    private Listener currentListener;
    private Server server;
    private ExecutorService executor;

    private boolean connectingProgress;

    ModuleNetwork(final int port, final Server server) throws IOException {
        connectingProgress = false;
        this.server = server;
        receivers = new ArrayList<ModuleNetwork>();
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, "Błąd tworzenia ServerSocket'u. Pewnie port jest zajęty."); // dodałem ~maciej168
            throw e;
        }
        executor = Executors.newCachedThreadPool();
    }

    public Socket getSocket() {
        return socket;
    }

    public List<ModuleNetwork> getReceivers() {
        return receivers;
    }

    /**
     * Otwiera polaczenie i oczekuje na klienta.
     * Po ustanowieniu polaczenia uruchamia dla tego modulu watek odbierajacy pakiety.
     */
    public void connect() {
        Runnable connecting = new Runnable() {
            @Override
            public void run() {
                connectingProgress = true;
                try {
                    server.closeConnection(socket);
                    socket = serverSocket.accept();
                    //executor.execute(new Listener(module)); // zmiana ~maciej168
                    currentListener = new Listener(ModuleNetwork.this, server);
                    executor.execute(currentListener);
                    System.out.println("Podlaczyl sie klient - port: " + serverSocket.getLocalPort());
                } catch (IOException e) {
                    // e.printStackTrace(); //zmieniłem ~maciej168
                    Logger.getLogger(Server.class.getName()).log(Level.WARNING, "Błąd tworzenia Listener'a", e);
                    throw new RuntimeException();
                } finally {
                    connectingProgress = false;
                }
            }
        };
        executor.execute(connecting);
    }

    /**
     * Dodaje nowego odbiorce dla tego modulu.
     *
     * @param module
     */
    public void addReceiver(final ModuleNetwork module) {
        receivers.add(module);
    }

    /**
     * Metoda przyjmujaca zgloszenia o problemie z polaczeniem
     */
    public void reportConnectionProblem() {
        if (!connectingProgress) {
            connect();
        }
    }

    /**
     * Symulacja awarii modułu sieciowego.
     * ~maciej168
     */
    public void clog(boolean makeClogged) {
        currentListener.clog(makeClogged);
    }

}