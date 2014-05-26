package zkm;

import event.BusStartSignal;
import event.BusReleasingFrequency;
import event.TrapBus;
import mockup.Mockup;
import model.Bus;
import model.BusStop;
import model.Passenger;
import network.Client;
import view.SimulatorEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * @author Rafal Jagielski
 */
public class ZkmMain {

    private String host;
    private int port;
    private Client sc = new Client();
    private Integer lowerBound;
    private Integer upperBound;
    private Integer loopTimeMinute; // TODO


    public ZkmMain(String host, int port, Integer lowerBound, Integer upperBound, Integer loopTimeMinute) {
        this.host = host;
        this.port = port;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.loopTimeMinute = loopTimeMinute;
    }

    public static void main(String[] args) {
        //if (args.length != 2)
        //{
        //    System.out.println("Wrong number of arguments. Program is stopping.");
        //    return;
        //}
        String host = "127.0.0.1";
        int port = 8125;
        Integer lowerBound = 10;//Integer.parseInt(args[0]);
        Integer upperBound = 20;//Integer.parseInt(args[1]);
        Integer loopTimeMinute = 0;
        if (lowerBound > upperBound) {
            System.out.println("Lower bound (first arg) cannot be higher than upper bound (second argument)\n"
                    + "Program is stopping.");
            return;
        }

        ZkmMain zkmMain = new ZkmMain(host, port, lowerBound, upperBound, loopTimeMinute);
        zkmMain.mainLoop();
    }

    public void mainLoop() {
        sc.establishConnection(host, port);

        System.out.println("Press enter key to stop.");

        Mockup mockup = null;
        do {
            mockup = receiveMockup();
            //TODO: jaką ja mam pewność, że te autobusy są poukładane?
            ArrayList<Bus> buses = (ArrayList<Bus>) mockup.getBuses();
            ArrayList<BusStop> busStops = (ArrayList<BusStop>) mockup.getBusStops();

            Integer noOfBuses = 0;
            Integer freeSeatsNr = 0;
            Integer generalPeopleWaitingNr = 0;
            Long generalSumOfWaitingTime = 0L;
            Integer noOfPeopleWithoutPlaceInBus = 0;
            Long sumOfWaitingTimeWithoutPlaceInBus = 0L;

            for (Bus bus : buses) {
                noOfBuses += 1;
                freeSeatsNr += bus.getNumberOfFreeSeats();
            }

            for (BusStop busStop : busStops) {
                generalPeopleWaitingNr += busStop.getNumberOfPassengersWaiting();

                for(Passenger passenger : busStop.getPassengerQueue())
                {
                    generalSumOfWaitingTime +=  passenger.getWaitingTime();
                }
            }

            makeDecision(noOfBuses, freeSeatsNr, generalPeopleWaitingNr,
                    generalSumOfWaitingTime, noOfPeopleWithoutPlaceInBus,
                    sumOfWaitingTimeWithoutPlaceInBus);

        } while (endLoop());

        System.out.println("ZKM has stopped.");
    }

    private Mockup receiveMockup() {

        SimulatorEvent event = null;
        BlockingQueue<SimulatorEvent> queueOfOrders = sc.getEventsBlockingQueue();

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
                //TODO skąd się bierze ten wyjątek i co z nim zrobić?
                e.printStackTrace();
            }
        }

        /**
         * A tak, bo mamy tylko 1 event dla ZKM.
         * Należy pobrać tylko ostatni element z kolejki.
         * Jeśli nie ma żadnego to należy się zawiesić w oczekiwaniu.
         */
        return event.getMockup();
    }

    private boolean endLoop() {
        try {
            if (System.in.available() != 0) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void makeDecision(Integer noOfBuses, Integer freeSeatsNr, Integer generalPeopleWaitingNr,
                              Long generalSumOfWaitingTime, Integer noOfPeopleWithoutPlaceInBus,
                              Long sumOfWaitingTimeWithoutPlaceInBus)
    {
        Integer minimumSumOfWaitingTime = 200;
        Integer numberOfPassengersWaitingBorder = 10;


        if (generalPeopleWaitingNr < numberOfPassengersWaitingBorder) //Liczba pasażerów jest mała - wysyłanie autobusów doraźnie
        {
            sc.send(new BusReleasingFrequency(0));

            if (noOfPeopleWithoutPlaceInBus > 0
                && sumOfWaitingTimeWithoutPlaceInBus > minimumSumOfWaitingTime)
            {
                sc.send(new BusStartSignal());
            }
            else if (noOfPeopleWithoutPlaceInBus.equals(0))
            {
                sc.send(new TrapBus()); //Zmniejsza liczbę okrążeń najbliższego autobusu do ostatniego
            }
        }
        else
        {
            //TODO: Obliczyć potrzebną częstotliwość
            sc.send(new BusReleasingFrequency(10));
        }
    }
}
