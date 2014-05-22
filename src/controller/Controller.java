package controller;

import event.*;
import mockup.Mockup;
import model.*;
import network.Client;
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
public class Controller implements ActionListener {
    private static Controller ourInstance = new Controller();

    private final Model model;
    private final Map<Class<? extends BusEvent>, MyStrategy> eventDictionaryMap;
    private final LinkedBlockingQueue<BusEvent> blockingQueue;
    private final Timer timer;
    private Mockup mockup;
    private Client networkClient = new Client();
    private String host;
    private int port;

    private Controller() {
        networkClient.establishConnection(host, port);
        this.blockingQueue = new LinkedBlockingQueue<BusEvent>();
        this.eventDictionaryMap = getEventDictionaryMap();
        this.model = new Model(this.blockingQueue);
        this.mockup = createMockup();
//        this.view = new View(blockingQueue, mockup);
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
        resultMap.put(BusReadyToGo.class, new BusReadyToGoStrategy());
        resultMap.put(BusReturnedToDepot.class, new BusReturnedToDepotStrategy());
        resultMap.put(BusStartSignal.class, new BusStartSignalStrategy());
        resultMap.put(BusWaitsBeforeStop.class, new BusStopOccupiedStrategy());
        return Collections.unmodifiableMap(resultMap);
    }

    public Mockup createMockup() {
        return model.createMockup();
    }

    public void work() {
//        view.showGUI();
        while (true) {
            BusEvent busEvent = null;
            try {
                busEvent = blockingQueue.take();
            } catch (final InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            final MyStrategy myStrategy = eventDictionaryMap.get(busEvent.getClass());
            myStrategy.execute(busEvent.getBus());
        }
    }

    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
        model.step();
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

    abstract private class MyStrategy {
        abstract void execute(Bus bus);
    }

    /**
     * Obsługa zdarzenia przyjazdu autobusu na przystanek.
     * Dojazd na przystanek powoduje zmianę stanu autobusu na autobus stojący na przystanku.
     * Jeśli dojechał do pętli, to sprawdzana jest dokonana liczba kursów. Jeśli został osiągnięty
     * limit, to autobus wzbudza sam dla siebie sygnał powrotu do zajezdni.
     */
    private final class BusArrivesToBusStopStrategy extends MyStrategy {
        @Override
        void execute(Bus bus) {
            bus.reachNextStop();
            if (bus.getCurrentBusStop() instanceof BusTerminus) {
                bus.getLoopsToFinish().countdown();
                if (bus.areLoopsFinished()) {
                    try
                    {
                        blockingQueue.put(new BusComeBackSignal(bus));
                    } catch (final InterruptedException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            bus.setState(BusState.ON_STOP);
        }
    }

    /**
     * Strategia sygnału powrotu do pętli.
     * Dla autobusu zostaje ustawiona trasa powrotna do zajezdni i stan zakończenia kursu.
     */
    private final class BusComeBackSignalStrategy extends MyStrategy {
        @Override
        void execute(Bus bus) {
            bus.setValueToNextStop(SimulatorConstants.depotTerminusDistance);
            bus.setState(BusState.FINISHED);
        }
    }

    /**
     * Strategia zdarzenia gotowości do jazdy.
     * Autobus zmienia stan na gotowy do jazdy.
     */
    private final class BusReadyToGoStrategy extends MyStrategy {
        @Override
        void execute(Bus bus) {
            bus.setState(BusState.IN_DEPOT);
        }
    }

    /**
     * Strategia zdarzenia powrotu do zajezdni.
     * Autobus, który wrócił do zajezdni zmienia obecny przystanek na zajezdnię
     * i zmienia stan na "przerwa".
     */
    private final class BusReturnedToDepotStrategy extends MyStrategy {
        @Override
        void execute(Bus bus) {
            bus.setCurrentBusStop(BusDepot.getInstance());
            bus.setState(BusState.HAVING_BREAK);
        }
    }

    /**
     * Strategia sygnału rozpoczęcia kursu.
     * Autobus zmienia stan na jadący.
     */
    private final class BusStartSignalStrategy extends MyStrategy {
        @Override
        void execute(Bus bus) {
            bus.setState(BusState.RUNNING);
        }
    }

    /**
     * Strategia zdarzenia próby wja
     * chuj, nie wiem co to jest
     */
    private final class BusStopOccupiedStrategy extends MyStrategy {
        @Override
        void execute(Bus bus) {

        }
    }
}
