package controller;

import event.*;
import mockup.Mockup;
import model.Bus;
import model.BusState;
import model.Model;
import network.Client;
import order.FunctionalitySimulationModule;
import order.Order;
import order.sim.OrderParseMockup;
import simulator.SimulatorConstants;
import view.BusEvent;

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
public class Controller implements ActionListener, FunctionalitySimulationModule {
    private static Controller ourInstance = new Controller();

    private final Model model;
    private final Map<Class<? extends BusEvent>, MyStrategy> eventDictionaryMap;
    private final LinkedBlockingQueue<BusEvent> eventsBlockingQueue;
    private final LinkedBlockingQueue<Order> ordersBlockingQueue;
    private final Timer timer;
    private Mockup mockup;
    private Client networkClient = new Client();
    private String host;
    private int port;

    private Controller() {
        networkClient.establishConnection(host, port);
        this.eventsBlockingQueue = new LinkedBlockingQueue<BusEvent>();
        this.ordersBlockingQueue = networkClient.getOrdersBlockingQueue();
        this.eventDictionaryMap = getEventDictionaryMap();
        this.model = new Model(eventsBlockingQueue);
        this.mockup = createMockup();
//        this.view = new View(eventsBlockingQueue, mockup);
        this.timer = new Timer(SimulatorConstants.simulationSpeed, this);
        timer.start();
    }

    public static Controller getInstance() {
        return ourInstance;
    }

    private Map<Class<? extends BusEvent>, MyStrategy> getEventDictionaryMap() {
        final Map<Class<? extends BusEvent>, MyStrategy> resultMap = new HashMap<Class<? extends BusEvent>, MyStrategy>();
        resultMap.put(BusArrivesToBusStop.class, new BusArrivesToBusStopStrategy());
        resultMap.put(BusComeBackSignal.class, new BusComeBackSignalStrategy());
        resultMap.put(BusPutOutPassengers.class, new BusPutOutPassengersStrategy());
        resultMap.put(BusPutOutAll.class, new BusPutOutAllStrategy());
        resultMap.put(BusReadyToGo.class, new BusReadyToGoStrategy());
        resultMap.put(BusReturnedToDepot.class, new BusReturnedToDepotStrategy());
        resultMap.put(BusStartSignal.class, new BusStartSignalStrategy());
        resultMap.put(BusTookInPassengers.class, new BusTookInPassengersStrategy());
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
                BusEvent busEvent = null;
                try
                {
                    busEvent = eventsBlockingQueue.take();
                } catch (final InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                final MyStrategy myStrategy = eventDictionaryMap.get(busEvent.getClass());
                myStrategy.execute(busEvent.getBus());
            }
//            while (!eventsBlockingQueue.isEmpty()) {
//                BusEvent busEvent = eventsBlockingQueue.poll();
//                final MyStrategy myStrategy = eventDictionaryMap.get(busEvent.getClass());
//                myStrategy.execute(busEvent.getBus());
//            }
            /**
             * Priorytetowe zdarzenia -> rozkazy
             */
//            while (!ordersBlockingQueue.isEmpty()) {
//                Order<FunctionalitySimulationModule> order = ordersBlockingQueue.poll();
//                order.execute(this);
//            }
        }
    }

    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
        try {
            model.step();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mockup = createMockup();
        networkClient.send(new OrderParseMockup(mockup));
//        view.updateBoard(mockup);
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

    @Override
    public void runSimulation(boolean patataj) {

    }

    @Override
    public void stepSimulation(boolean goSlower) {

    }

    @Override
    public void passengerGenerationConfig(int minGen, int maxGen) {

    }

    @Override
    public void newPassenger(String busStopStart, String busStopStop) {

    }

    @Override
    public void releaseBus() {

    }

    @Override
    public void trapBus() {

    }

    @Override
    public void updateBus() {

    }

    abstract private class MyStrategy {
        abstract void execute(Bus bus);
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
        void execute(Bus bus) {
//            System.out.println("Wykonuje: BusArrivesToBusStopStrategy");
            bus.terminusCheck();
            if (bus.isFinished()) {
                if (bus.isEmpty()) {
                    bus.reachNextStop();
                }
                else {
                    if (bus.isNextStopOccupied()) {
                        bus.setState(BusState.WAITING);
                    } else {
                        bus.reachNextStop();
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
                    bus.reachNextStop();
                }
                /**
                 * Jeśli nikt na danym przystanku nie czeka i nie chce wysiadać, przystanek jest pomijany.
                 */
                else if (bus.isEmpty() && !bus.isGetOnRequestNow()) {
                    bus.reachNextStop();
                }
                else {
                    if (bus.isNextStopOccupied()) {
                        bus.setState(BusState.WAITING);
                    } else {
                        bus.reachNextStop();
                        System.out.println(bus.getCurrentBusStop().getNAME());
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
        void execute(Bus bus) {
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
        void execute(Bus bus) {
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
    private final class BusPutOutPassengersStrategy extends  MyStrategy {
        @Override
        void execute(Bus bus) {
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
        void execute(Bus bus) {
//            System.out.println("Wykonuje: BusReadyToGoStrategy");
            bus.setState(BusState.READY_TO_GO);
        }
    }

    /**
     * <b>Obsługa zdarzenia powrotu do zajezdni.</b>
     * Autobus, który wrócił do zajezdni ma przerwę.
     */
    private final class BusReturnedToDepotStrategy extends MyStrategy {
        @Override
        void execute(Bus bus) {
//            System.out.println("Wykonuje: BusReturnedToDepot");
            bus.reachDepot();
            bus.setState(BusState.HAVING_BREAK);
            System.out.println(bus.getCurrentBusStop().getNAME());
        }
    }

    /**
     * <b>Obsługa sygnału rozpoczęcia kursu.</b>
     * Autobus zaczyna jechać.
     */
    private final class BusStartSignalStrategy extends MyStrategy {
        @Override
        void execute(Bus bus) {
//            System.out.println("Wykonuje: BusStartSignalStrategy");
            bus.setState(BusState.RUNNING);
        }
    }

    /**
     * <b>Obsługa zdarzenia zabrania wszystkich pasażerów z przystanka.</b>
     * Autobus jedzie dalej.
     */
    private final class BusTookInPassengersStrategy extends  MyStrategy {
        @Override
        void execute(Bus bus) {
//            System.out.println("Wykonuje: BusTookInPassengersStrategy");
            bus.freeCurrentBusStop();
            bus.setState(BusState.RUNNING);
        }
    }
}
