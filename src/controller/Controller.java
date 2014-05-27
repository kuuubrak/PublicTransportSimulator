package controller;

import event.BusReleasingFrequency;
import event.BusStartSignal;
import event.TrapBus;
import event.busevents.*;
import event.guievents.ContinuousSimulationEvent;
import event.guievents.NewPassengerEvent;
import event.guievents.PassengerGenerationInterval;
import main.SimulatorConstants;
import mockup.Mockup;
import model.*;
import network.Client;
import view.SimulatorEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ppeczek on 2014-05-21.
 */
public class Controller implements ActionListener {
    private static Controller ourInstance = new Controller();

    private final Model model;

    private final Map<Class<? extends SimulatorEvent>, MyStrategy> eventDictionaryMap;
    private final LinkedBlockingQueue<SimulatorEvent> eventsBlockingQueue;
    private final Timer timer;
    private Mockup mockup;
    private Client networkClient;
    private String host;
    private int port;

    private Controller() {
        setNetData(SimulatorConstants.simulatorHostAddress, SimulatorConstants.simulatorPort);
        this.eventsBlockingQueue = new LinkedBlockingQueue<SimulatorEvent>();
        this.eventDictionaryMap = getEventDictionaryMap();
        this.model = new Model(eventsBlockingQueue);
        this.mockup = createMockup();
//        this.view = new View(eventsBlockingQueue, mockup);
        this.timer = new Timer(SimulatorConstants.simulationSpeed, this);
        timer.start();
        networkClient = new Client(host, port);
        networkClient.connect();
        networkClient.setEventsBlockingQueue(eventsBlockingQueue);
    }

    public static Controller getInstance() {
        return ourInstance;
    }

    private Map<Class<? extends SimulatorEvent>, MyStrategy> getEventDictionaryMap() {
        final Map<Class<? extends SimulatorEvent>, MyStrategy> resultMap = new HashMap<Class<? extends SimulatorEvent>, MyStrategy>();
        /**
         * Zdarzenia generowane cyklicznie przez symulator
         */
        resultMap.put(BusStartSignal.class, new BusStartSignalStrategy());
        /**
         * Zdarzenia generowane przez autobus
         */
        resultMap.put(BusArrivesToBusStop.class, new BusArrivesToBusStopStrategy());
        resultMap.put(BusComeBackSignal.class, new BusComeBackSignalStrategy());
        resultMap.put(BusPutOutPassengers.class, new BusPutOutPassengersStrategy());
        resultMap.put(BusPutOutAll.class, new BusPutOutAllStrategy());
        resultMap.put(BusReadyToGo.class, new BusReadyToGoStrategy());
        resultMap.put(BusReturnedToDepot.class, new BusReturnedToDepotStrategy());
        resultMap.put(BusTookInPassengers.class, new BusTookInPassengersStrategy());
        /**
         * Rozkazy użytkownika
         */
        resultMap.put(NewPassengerEvent.class, new NewPassengerStrategy());
        resultMap.put(PassengerGenerationInterval.class, new PassengerGenerationIntervalStrategy());
        resultMap.put(ContinuousSimulationEvent.class, new ContinousSimulationStrategy());
        resultMap.put(BusReleasingFrequency.class, new BusReleasingFrequencyStrategy());
        resultMap.put(TrapBus.class, new TrapBusStrategy());
        return Collections.unmodifiableMap(resultMap);
    }

    public Mockup createMockup() {
        return model.createMockup();
    }

    public void work() {
//        view.showGUI();
        while (true) {
            /**
             * Zwykłe zdarzenia i sygnały
             */
            while (true)
            {
                SimulatorEvent simulatorEvent = null;
                try
                {
                    simulatorEvent = eventsBlockingQueue.take();
                    System.out.println(simulatorEvent.getClass());
                } catch (final InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                final MyStrategy myStrategy = eventDictionaryMap.get(simulatorEvent.getClass());
                myStrategy.execute(simulatorEvent);
            }
        }
    }

    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
        try {
            model.step();
        } catch (Exception e) {
            e.printStackTrace();
        }
       // networkClient.send(createMockup());
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                setView();
            }
        });
    }

    public void setView() {
//        view.repaint();
    }

    public void setNetData(String host, int port) {
        this.host = host;
        this.port = port;
    }

    abstract private class MyStrategy {
        abstract void execute(SimulatorEvent simulatorEvent);
    }

    /**
     * <b>Obsługa sygnału rozpoczęcia kursu.</b>
     * Autobus zaczyna jechać.
     */
    private final class BusStartSignalStrategy extends MyStrategy {
        @Override
        void execute(SimulatorEvent simulatorEvent) {
            BusDepot busDepot = BusDepot.getInstance();
            Bus bus = busDepot.getBusQueue().poll();
            if (bus != null) {
                System.out.println("Wykonuje: BusStartSignalStrategy");
                bus.setState(BusState.RUNNING);
            }
        }
    }

    /**
     * <b>Obsługa zdarzenia dojazdu autobusu na przystanek.</b>
     * Autobus sprawdza, czy przystanek nie jest zajęty.
     * Jeśli autobus dojechał do pętli, to sprawdza, czy nie kończy kursu.
     * Jeśli nie dojechał do pętli i są pasażerowie, którzy chcą wysiąść, to wysiadają.
     * Jeśli takich nie ma, to wsiadają ci, którzy chcą wsiąść.
     * Jeśli nie ma ani jednych, ani drugich, to autobus jedzie dalej.
     */
    private final class BusArrivesToBusStopStrategy extends MyStrategy {

        @Override
        void execute(SimulatorEvent busEvent) {
            Bus bus = busEvent.getBus();
//            System.out.println("Wykonuje: BusArrivesToBusStopStrategy");
            bus.terminusCheck();
            if (bus.isFinished()) {
                if (bus.isEmpty()) {
                    System.out.println(bus + " nie zatrzymuje się na: " + bus.getCurrentBusStop().getNAME());
                    bus.reachNextStop();
                }
                else {
                    if (bus.isNextStopOccupied()) {
                        bus.setState(BusState.WAITING);
                    } else {
                        bus.reachNextStop();
                        System.out.println(bus + ": " + bus.getCurrentBusStop().getNAME());
                        bus.occupyCurrentBusStop();
                        bus.setState(BusState.PUT_OUT_ALL);
                    }
                }
            }
            if (!bus.isFinished()) {
                /**
                 * Jeśli autobus jest pełny i nikt na danym przystanku nie wysiada, przystanek jest pomijany.
                 */
                if (bus.isFull() && !bus.isGetOffRequestNow()) {
                    System.out.println(bus + " nie zatrzymuje się na: " + bus.getCurrentBusStop().getNAME());
                    bus.reachNextStop();
                }
                /**
                 * Jeśli nikt na danym przystanku nie czeka i nie chce wysiadać, przystanek jest pomijany.
                 */
                else if (bus.isEmpty() && !bus.isGetOnRequestNow()) {
                    System.out.println(bus + " nie zatrzymuje się na: " + bus.getCurrentBusStop().getNAME());
                    bus.reachNextStop();
                }
                else {
                    if (bus.isNextStopOccupied()) {
                        bus.setState(BusState.WAITING);
                    } else {
                        bus.reachNextStop();
                        System.out.println(bus + ": " + bus.getCurrentBusStop().getNAME());
                        /**
                         * Po zatrzymaniu się autobusu na przystanku najpierw opuszczają go pasażerowie,
                         * dla których jest to przystanek docelowy, a następnie wsiadają do niego
                         * kolejne osoby w miarę wolnych miejsc.
                         */
                        if (!bus.isFinished()) {
                            if (bus.isGetOffRequestNow()) {
                                bus.occupyCurrentBusStop();
                                bus.setState(BusState.PUT_OUT);
                            } else if (bus.isGetOnRequestNow()) {
                                bus.occupyCurrentBusStop();
                                bus.setState(BusState.TAKE_IN);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * <b>Obsługa sygnału powrotu do zajezdni.</b>
     * Jeśli autobus ma pasażerów, to ich wszystkich wypuszcza.
     * W przeciwnym wypadku wraca do zajezdni.
     */
    private final class BusComeBackSignalStrategy extends MyStrategy {
        @Override
        void execute(SimulatorEvent busEvent) {
            Bus bus = busEvent.getBus();
//            System.out.println("Wykonuje: BusComeBackSignalStrategy");
            bus.comeback();
        }
    }

    /**
     * <b>Obsługa zdarzenia zakończenia wysadzania wszystkich pasażerów z autobusu.</b>
     * Autobus wraca do zajezdni.
     */
    private final class BusPutOutAllStrategy extends MyStrategy {
        @Override
        void execute(SimulatorEvent busEvent) {
            Bus bus = busEvent.getBus();
//            System.out.println("Wykonuje: BusPutOutAllStrategy");
            bus.freeCurrentBusStop();
            bus.comeback();
            System.out.println("Następny:" + bus.getToNextStop().getValue());
        }
    }

    /**
     * <b>Obsługa zdarzenia zakończenia wysadzania pasażerów z autobusu.</b>
     * Jeśli jacyś pasażerowie chcą wsiąść, to autobus ich zabiera.
     * W przeciwnym wypadku autobus wyrusza dalej.
     */
    private final class BusPutOutPassengersStrategy extends MyStrategy {
        @Override
        void execute(SimulatorEvent busEvent) {
            Bus bus = busEvent.getBus();
//            System.out.println("Wykonuje: BusPutOutPassengersStrategy");
            if (bus.isGetOnRequestNow()) {
                bus.setState(BusState.TAKE_IN);
            }
            else {
                bus.freeCurrentBusStop();
                bus.setState(BusState.RUNNING);
            }
        }
    }

    /**
     * <b>Obsługa zdarzenia gotowości do jazdy.</b>
     * Autobus jest gotowy do jazdy.
     */
    private final class BusReadyToGoStrategy extends MyStrategy {
        @Override
        void execute(SimulatorEvent busEvent) {
//            System.out.println("Wykonuje: BusReadyToGoStrategy");
            Bus bus = busEvent.getBus();
            BusDepot.getInstance().getBusQueue().add(bus);
            bus.setState(BusState.READY_TO_GO);
        }
    }

    /**
     * <b>Obsługa zdarzenia powrotu do zajezdni.</b>
     * Autobus, który wrócił do zajezdni ma przerwę.
     */
    private final class BusReturnedToDepotStrategy extends MyStrategy {
        @Override
        void execute(SimulatorEvent busEvent) {
            Bus bus = busEvent.getBus();
//            System.out.println("Wykonuje: BusReturnedToDepot");
            BusDepot busDepot = BusDepot.getInstance();
            busDepot.getBusQueue().add(bus);
            bus.reachDepot();
            bus.setState(BusState.HAVING_BREAK);
            System.out.println(bus + ": " + bus.getCurrentBusStop().getNAME());
        }
    }

    /**
     * <b>Obsługa zdarzenia zabrania wszystkich pasażerów z przystanka.</b>
     * Autobus jedzie dalej.
     */
    private final class BusTookInPassengersStrategy extends MyStrategy {
        @Override
        void execute(SimulatorEvent busEvent) {
            Bus bus = busEvent.getBus();
//            System.out.println("Wykonuje: BusTookInPassengersStrategy");
            bus.freeCurrentBusStop();
            bus.setState(BusState.RUNNING);
        }
    }

    private final class NewPassengerStrategy extends MyStrategy {
        @Override
        void execute(SimulatorEvent simulatorEvent) {
            BusStop from = Schedule.getInstance().findBusStop(simulatorEvent.getFrom());
            BusStop to = Schedule.getInstance().findBusStop(simulatorEvent.getTo());
            model.generateSpecificPassenger(from, to);
        }
    }

    private final class PassengerGenerationIntervalStrategy extends MyStrategy {
        @Override
        void execute(SimulatorEvent simulatorEvent) {
            int lowerBound = simulatorEvent.getMin();
            int upperBound = simulatorEvent.getMax();
            model.setNewPassengerCountersBound(lowerBound, upperBound);
        }
    }

    private final class ContinousSimulationStrategy extends MyStrategy {
        @Override
        void execute(SimulatorEvent simulatorEvent) {
            /**
             * TODO: włączyć obsługę guzika GUI
             */
            System.out.println("abc");
            if (simulatorEvent.isContinuous()) {
                timer.setRepeats(true);
                timer.start();

            } else {
                timer.stop();
                timer.setRepeats(false);
                timer.start();
            }
        }
    }

    private final class BusReleasingFrequencyStrategy extends MyStrategy {
        @Override
        void execute(SimulatorEvent simulatorEvent) {
            BusReleasingFrequency event = (BusReleasingFrequency) simulatorEvent;
            if (event.getFrequency() == 0)
            {
                //Zatrzymaj wypuszczanie autobusów
                model.busReleaseCounterState(false);
            }
            else
            {
                //Uruchamianie countera na wypadek jakby był wcześniej zatrzymany.
                model.busReleaseCounterState(true);
                model.setBusReleaseCounterValue(event.getFrequency());
            }
        }
    }

    private final class TrapBusStrategy extends MyStrategy {
        @Override
        void execute(SimulatorEvent simulatorEvent) {
            //TODO zmniejszanie liczby okrążeń autobusu, który jest najbliżej zajezdni
        }
    }

}
