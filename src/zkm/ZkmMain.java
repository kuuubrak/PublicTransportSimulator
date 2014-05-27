package zkm;

import event.BusStartSignal;
import event.BusReleasingFrequency;
import event.TrapBus;
import javafx.util.Pair;
import main.Simulator;
import main.SimulatorConstants;
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

    private String host = ZkmConstants.host;
    private int port = ZkmConstants.port;
    private Client sc = new Client(host, port);
    private Integer loopTimeMinute = 0; // TODO


    public ZkmMain() {
        for (Pair<String, Integer> pair : SimulatorConstants.busStopSettings){
            loopTimeMinute += pair.getValue();
        }
    }

    public static void main(String[] args) {
        ZkmMain zkmMain = new ZkmMain();
        zkmMain.mainLoop();
    }

    public void mainLoop() {
        sc.connect();

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
            Integer noOfPeopleWithoutPlaceInBus = 0; //??
            Long sumOfWaitingTimeWithoutPlaceInBus = 0L; //??

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
        int peopleInsideBuses = noOfBuses*SimulatorConstants.noOfSeatsInBus - freeSeatsNr;
        int peopleInTheWorld = peopleInsideBuses + generalPeopleWaitingNr;
        int howManyBuses = (int) Math.ceil(peopleInTheWorld / (double) SimulatorConstants.noOfSeatsInBus) ;
        if (howManyBuses == 0)
        {
            sc.send(new BusReleasingFrequency(0));
        }
        else
        {
            int newFrequency = (loopTimeMinute * SimulatorConstants.loops) / (howManyBuses);
            sc.send(new BusReleasingFrequency(newFrequency));
        }
    }
}
