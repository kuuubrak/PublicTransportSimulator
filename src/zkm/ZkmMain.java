package zkm;

import event.BusReleasingFrequency;
import main.SimulatorConstants;
import mockup.Mockup;
import model.*;
import network.Client;
import view.SimulatorEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Klasa zarządzająca wypuszczaniem autobusów z pętli Symulatora aby zoptymalizować ich rozkłąd jazdy.
 * @author Rafal Jagielski
 */
public class ZkmMain extends Thread {

    private String host = ZkmConstants.host;
    private int port = ZkmConstants.port;
    private Client sc = new Client(host, port);
    /** zmienna przechowująca ilość kroków potrzebną do jednokrotnego przejazdu trasy
     * (nie zlicza czasu potrzebnego na wsiadanie i wysiadanie pasażerów)
     */
    private Integer loopTimeMinute = 0;
    /** zmienna potrzebna do zakończenia pracy modułu */
    private boolean cont = true;

    /**
     * Konstruktor jednocześnie inicjalizuje wartość paramteru loopTimeMinute korzystając z tablicy
     * na stałe zdefiniowanej w klasie main.SimulatorConstants.
     */
    public ZkmMain() {
        for (Pair<String, Integer> pair : SimulatorConstants.busStopSettings){
            loopTimeMinute += pair.getValue();
        }
    }

    public static void main(String[] args)
    {
        final ZkmMain zkmMain = new ZkmMain();

        //Wątek nasłuchujący sygnału z klawiatury. Gdy taki otrzyma kończy pracę programu.
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                boolean cont = true;
                while(cont)
                {
                    try
                    {
                        Thread.sleep(500);
                        if (System.in.available() != 0) {
                            zkmMain.interruptMe();
                            cont = false;
                        }
                    } catch (IOException e)
                    {
                        // Nic nie trzeba robić.
                        // Jednynym efektem będzie ponowna próba sprawdzenia System.in
                        e.printStackTrace();
                    }
                    catch (InterruptedException e)
                    {
                        // Nic nie trzeba robić.
                        // Jedynym efektem będzie szybsze sprawdzenie warunku.
                        e.printStackTrace();
                    }
                }
                System.out.println("ZkmMain is interrupted.");
            }
        }).start();

        zkmMain.mainLoop();
    }

    /**
     * Główna metoda programu zawierająca pętlę, która przetwarza kolejne makiety.
     * Oblicza z nich zgeneralizowane informacje, na podstawie których jest podejmowana decyzja.
     */
    public void mainLoop() {
        sc.connect();

        System.out.println("Press enter key to stop.");

        Mockup mockup;
        do {
            //Odbieranie makiety
            mockup = receiveMockup();
            ArrayList<Bus> buses = (ArrayList<Bus>) mockup.getBuses();
            ArrayList<BusStop> busStops = (ArrayList<BusStop>) mockup.getBusStops();

            //Wyliczanie współczynników
            Integer noOfBuses = 0;
            Integer freeSeatsNr = 0;
            Integer generalPeopleWaitingNr = 0;
            Long generalSumOfWaitingTime = 0L;

            for (Bus bus : buses) {
                System.out.println(bus.getState());
                if (!bus.getState().equals(BusState.READY_TO_GO) && !bus.getState().equals(BusState.HAVING_BREAK))
                //tylko autobusy nie będące w zajezdni
                {
                    noOfBuses += 1;
                    freeSeatsNr += bus.getNumberOfFreeSeats();
                }
            }

            for (BusStop busStop : busStops) {
                generalPeopleWaitingNr += busStop.getNumberOfPassengersWaiting();

                for(Passenger passenger : busStop.getPassengerQueue())
                {
                    generalSumOfWaitingTime += mockup.getCurrentTime() - passenger.getTIMESTAMP();
                }
            }

            //delegacja podjęcia decyzji do innej metody
            makeDecision(noOfBuses, freeSeatsNr, generalPeopleWaitingNr,
                    generalSumOfWaitingTime);

        } while (cont);
    }

    /**
     * Metoda odbierająca makietę z wątku klienta (network.Client) serwera. Jeśli w międzyczasie odebrane
     * zostało kilka makiet, to przekazywana jest tylko ta wysłana najpóźniej. Reszta jest ignorowana
     * (taka sytuacja może nastąpić, gdy zostanie utracone połączenie).
     * @return Zwracanym obiektem jest makieta nadana na serwerze.
     */
    private Mockup receiveMockup() {

        SimulatorEvent event = null;
        LinkedBlockingQueue<SimulatorEvent> queueOfOrders = sc.getEventsBlockingQueue();

        //Pętla powoduje, że pobierany jest zawsze ostatni event (makieta) jeśli jest ich kilka
        //(np. gdy było chwilowe rozłączenie), bądź następuje zawieszenie i oczekiwanie, jeśli nie było żadnego
        while (!queueOfOrders.isEmpty() || event == null)
        {
            try
            {
                event = queueOfOrders.take();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                throw new RuntimeException();
            }

            // Jeśli odebraną makietą jest sztucznie wytworzona makieta, która umożliwia wyciągnięcie programu
            // z zawieszenia powstałego poprzez metodę take() na BlockingQueue, to jednocześnie ustawiany jest parametr
            // kończący pracę programu. usi to być dokonane w tym miejscu ponieważ pusta makieta nie może zostać
            // przetworzona przez pętlę główną - wysłała by wtedy rozkaz do Symulatora niezgodny ze stanem faktycznym.
            if (!cont)
            {
                sc.closeConnection();
                System.out.println("ZKM has stopped.");
                System.exit(0);
            }
        }

        return event.getMockup();
    }

    /**
     * Metoda podejmująca decyzję i odsyłająca odpowiednią informację do Symulatora.
     * @param noOfBuses liczba autobusów jeżdżących (tzn. tych, które nie znajdują się aktualnie w pętli)
     * @param freeSeatsNr liczba wolnych miejsc w autobusach jeżdżących
     * @param generalPeopleWaitingNr liczba ludzi czekająca na przystankach
     * @param generalSumOfWaitingTime suma czasów spędzonych przez w/w ludzi na przystakach
     */
    private void makeDecision(Integer noOfBuses, Integer freeSeatsNr, Integer generalPeopleWaitingNr,
                              Long generalSumOfWaitingTime)
    {
        // W tym miejscu obliczana jest potrzebna liczba autobusów do zaspokojenia potrzeb Swiata
        int peopleInsideBuses = noOfBuses*SimulatorConstants.noOfSeatsInBus - freeSeatsNr;
        int peopleInTheWorld = peopleInsideBuses + generalPeopleWaitingNr;
        int howManyBuses = (int) Math.ceil(peopleInTheWorld / (double) SimulatorConstants.noOfSeatsInBus) ;

        System.out.println("HowManyBuses: " + howManyBuses + "; CurrentNumberOfBuses: " + noOfBuses +  "; freeSeatsNr: " + freeSeatsNr
                + "; generalPeopleWaitingNr: " + generalPeopleWaitingNr + "; generalSumOfWaitingTime: " + generalSumOfWaitingTime);

        if (howManyBuses == 0)
        {
            //Jeśli nie potrzeba żadnego autobusu, to zajezdnia ma zaprzestać wysyłanie autobusów
            sc.send(new BusReleasingFrequency(0));
        }
        else
        {
            // Obliczana jest częstotliwość wypuszczania autobusó, tak aby znajdowało się w obiegu dokładnie
            // tyle autobusów ile zostało wyliczonych
            int newFrequency = (loopTimeMinute * SimulatorConstants.loops) / (howManyBuses);
            sc.send(new BusReleasingFrequency(newFrequency));
        }

        /**
         * TODO: Potencjalne problemy:
         *  - gdy nagle zmieni się sytuacja na wymagającą większej ilości autobusów, wtedy Counter i tak będzie musiał
         *  zliczyć do zera zanim zmieni się jego wartość. Może to spowodować opóźnioną reakcję na zapotrzebowanie.
         *  - nie jest brany pod uwagę czas potrzebny na wysiadanie i wsiadanie. Jeśli będzie to proporcjojnalnie dużo
         *  w stosunku do czasu objazdu trasy, to trzeba sprawdzić efekty :-)
         */
    }

    /**
     * Metoda wywoływana przez wątek nasłuchujący klawiatury. Jej wykonanie polega na zmianie flagi wykonania
     * na false oraz dołożeniu pustej makiety do kolejki, aby odblokować wątek, który się zatrzymał na
     * BlockingQueue.
     */
    private void interruptMe()
    {
        cont = false;

        Mockup emptyMockup = new Mockup(new ArrayList<Bus>(), new ArrayList<BusStop>(), 10L);
        try
        {
            sc.getEventsBlockingQueue().put(emptyMockup);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
