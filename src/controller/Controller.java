package controller;

import event.*;
import mockup.Mockup;
import model.Model;
import network.Client;
import order.sim.OrderParseMockup;
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
    private Mockup mockup;
    private final Map<Class<? extends BusEvent>, MyStrategy> eventDictionaryMap;
    private final LinkedBlockingQueue<BusEvent> blockingQueue;
    private final Timer timer;

    private Client networkClient = new Client();
    private String host;
    private int port;

    public static Controller getInstance() {
        return ourInstance;
    }

    private Controller() {
        networkClient.establishConnection(host, port);
        this.blockingQueue = new LinkedBlockingQueue<BusEvent>();
        this.eventDictionaryMap = getEventDictionaryMap();
        this.model = Model.getInstance();
        this.mockup = createMockup();
//        this.view = new View(blockingQueue, mockup);
        this.timer = new Timer(50, this);
        timer.start();
    }

    private Map<Class<? extends BusEvent>, MyStrategy> getEventDictionaryMap() {
        final Map<Class<? extends BusEvent>, MyStrategy> resultMap = new HashMap<Class<? extends BusEvent>, MyStrategy>();
        resultMap.put(BusArrivesToBusStop.class, new BusArrivesToBusStopStrategy());
        resultMap.put(BusComeBackSignal.class, new BusComeBackSignalStrategy());
        resultMap.put(BusReadyToGo.class, new BusReadyToGoStrategy());
        resultMap.put(BusReturnedToDepot.class, new BusReturnedToDepotStrategy());
        resultMap.put(BusStartSignal.class, new BusStartSignalStrategy());
        resultMap.put(BusWaitsBeforeStop.class, new BusWaitsBeforeStopStrategy());
        return Collections.unmodifiableMap(resultMap);
    }

    abstract private class MyStrategy {
        abstract void execute();
    }

    private final class BusArrivesToBusStopStrategy extends MyStrategy {
        @Override
        void execute()
        {

        }
    }

    private final class BusComeBackSignalStrategy extends MyStrategy {
        @Override
        void execute()
        {

        }
    }

    private final class BusReadyToGoStrategy extends MyStrategy {
        @Override
        void execute() {

        }
    }

    private final class BusReturnedToDepotStrategy extends MyStrategy {
        @Override
        void execute()
        {

        }
    }

    private final class BusStartSignalStrategy extends MyStrategy {
        @Override
        void execute()
        {

        }
    }

    private final class BusWaitsBeforeStopStrategy extends MyStrategy {
        @Override
        void execute()
        {

        }
    }

    public Mockup createMockup() { return model.createMockup(); }

    public void work()
    {
//        view.showGUI();
        while (true)
        {
            BusEvent busEvent = null;
            try
            {
                busEvent = blockingQueue.take();
            } catch (final InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            final MyStrategy myStrategy = eventDictionaryMap.get(busEvent.getClass());
            myStrategy.execute();
        }
    }

    @Override
    public void actionPerformed(final ActionEvent actionEvent)
    {
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


//        while(!networkClient.getOrdersQueue().isEmpty())
//        {
//            try
//            {
//                Order<FunctionalitySimulationModule> order = networkClient.getOrdersQueue().take();
//                order.execute(this);
//            }
//            catch(InterruptedException e)
//            {
//                //TODO Generated
//                e.printStackTrace();
//                throw new RuntimeException();
//            }
//        }
//        try {
//            Thread.sleep(simulationWait);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public void setView()
    {
//        view.repaint();
    }

    public void setNetData(String host, int port) {
        this.host = host;
        this.port = port;
    }
}
