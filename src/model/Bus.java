package model;

import event.*;
import simulator.SimulatorConstants;
import view.BusEvent;

import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * <b>Bus</b>.<br>
 * <br>
 * Początkowo wszystkie stacjonują w <b>Zajezdni</b>.<br>
 * W razie wywołania przez <b>Moduł Zarządzający Komunikacją Miejską</b>, opuszczają <b>Zajezdnię</b> i jadą na <b>Pętlę</b>.<br>
 * Z <b>Pętli</b> wyrusza na <b>Linię</b>, odwiedza <b>M Przystanków</b> i wraca na <b>Pętlę</b>.<br>
 * Z <b>Pętli</b> według polecenia <b>Modułu Zarządzającego Komunikacją Miejską</b>, może zjechać do <b>Zajezdni</b>.<br>
 * Mieści <b>P Pasażerów</b>.<br>
 * Pomija <b>Przystanek</b>, jeśli żaden <b>Pasażer</b> nie chce wsiąść i wysiąść lub <b>Autobus</b> jest pełny i żaden <b>Pasażer</b> nie che wysiaść.<br>
 *
 * @author dan.krasniak
 */
public final class Bus implements EventListener {
    /**
     * How many <b>Passengers</b> can one <b>Bus</b> hold.
     */
    private static final int MAX_SEATS = 10;
    /**
     * A container of currently held <b>Passengers</b>
     */
    private Map<BusStop, Passenger> passengerMap = new HashMap<BusStop, Passenger>();
    private BusStop currentBusStop;
    private BusState state;
    private Counter toNextStop;
    private Counter loopsToFinish;
    private Cooldown cooldownAfterLoops;
    private Map<BusState, BusBehaviorStrategy> busBehaviorStrategyMap = new HashMap<BusState, BusBehaviorStrategy>();
    public BlockingQueue<BusEvent> blockingQueue;

    public Bus(BusStop startStation, BlockingQueue<BusEvent> blockingQueue) {
        this.currentBusStop = startStation;
        this.state = BusState.READY_TO_GO;
        this.toNextStop = new Counter(this.currentBusStop.getDistance());
        this.loopsToFinish = new Cooldown(SimulatorConstants.loops);
        this.cooldownAfterLoops = new Cooldown(SimulatorConstants.cooldownAfterLoops);
        this.busBehaviorStrategyMap = getBusBehaviorStrategyMap();
        this.blockingQueue = blockingQueue;
    }

    private Map<BusState, BusBehaviorStrategy> getBusBehaviorStrategyMap() {
        final Map<BusState, BusBehaviorStrategy> resultMap = new HashMap<BusState, BusBehaviorStrategy>();
        resultMap.put(BusState.READY_TO_GO, new BusIdleStrategy());
        resultMap.put(BusState.RUNNING, new BusOnRunStrategy());
        resultMap.put(BusState.WAITING, new BusWaitingForBusStopStrategy());
        resultMap.put(BusState.FINISHED, new BusReturningStrategy());
        resultMap.put(BusState.HAVING_BREAK, new BusReturnedStrategy());
        resultMap.put(BusState.PUT_OUT, new BusPutsOutPassengersStrategy());
        resultMap.put(BusState.TAKE_IN, new BusTakesInPassengersStrategy());
        resultMap.put(BusState.PUT_OUT_ALL, new BusPutsOutAllStrategy());
        return Collections.unmodifiableMap(resultMap);
    }

    /**
     * @return the passengerMap
     */
    public final Map<BusStop, Passenger> getPassengerMap() {
        return passengerMap;
    }

    public final void takeInPassengers() {
        takePassenger(getCurrentBusStop());
        if (isFull() || getCurrentBusStop().isEmpty()) {
            try
            {
                blockingQueue.put(new BusTookInPassengers(this));
            } catch (final InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private final void takePassenger(BusStop busStop) {
//        System.out.println("kolejka: " + busStop.getPassengerQueue().size());
        Passenger passenger = busStop.takePassenger();
//        System.out.println("pasażer: " + passenger.getID());
//        System.out.println("kolejka: " + busStop.getPassengerQueue().size());
        passengerMap.put(passenger.getDestination(), passenger);
//        System.out.println("Liczba pasażerów:" + getPassengerMap().size());
    }

    private final void putOutPassengers() {
        putOutPassenger(getCurrentBusStop());
        if (!isGetOffRequestNow()) {
            try
            {
                blockingQueue.put(new BusPutOutPassengers(this));
            } catch (final InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private final Passenger putOutPassenger(BusStop busStop) {
        Passenger passenger = getPassengerMap().remove(busStop);
//        System.out.println("Wysiadający pasażer:" + passenger.getID());
//        System.out.println("Liczba pasażerów:" + getPassengerMap().size());
        return passenger;
    }

    private final void putOutAll() {
        transferPassenger(getCurrentBusStop());
//        System.out.println("Liczba pasażerów:" + getPassengerMap().size());
        if (isEmpty()) {
            try {
                blockingQueue.put(new BusPutOutAll(this));
            } catch (final InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private final void transferPassenger(BusStop busStop) {
        Passenger passenger = getPassengerMap().entrySet().iterator().next().getValue();
        getPassengerMap().remove(passenger.getDestination(), passenger);
        BusStop busStop1 = getCurrentBusStop();
        if (!currentBusStop.equals(busStop1)) {
            busStop1.getPassengerQueue().add(passenger);
        }
    }

    public final void comeback() {
        setValueToNextStop(SimulatorConstants.depotTerminusDistance);
        setState(BusState.FINISHED);
    }
    /**
     * <b>isFull</b><br>
     *
     * @return true if the <b>Bus</b> is full.
     */
    public final boolean isFull() {
        return (getPassengerMap().size() == MAX_SEATS);
    }

    public final boolean isEmpty() { return getPassengerMap().isEmpty(); }

    /**
     * <b>getNumberOfFreeSeats</b><br>
     *
     * @return the number of free seats in the <b>Bus</b>
     */
    public final int getNumberOfFreeSeats() {
        return MAX_SEATS - getPassengerMap().size();
    }

    /**
     * <b>getCurrentBusStop</b><br>
     * <br>
     * public BusStop <b>getCurrentBusStop</b>()
     *
     * @return current <b>BusStop</b>
     */
    public BusStop getCurrentBusStop() {
        return currentBusStop;
    }

    public void setCurrentBusStop(BusStop currentBusStop) {
        this.currentBusStop = currentBusStop;
    }

    public BusState getState() {
        return state;
    }

    public void setState(BusState state) {
        this.state = state;
    }

    public Counter getToNextStop() {
        return toNextStop;
    }

    public Counter getLoopsToFinish() {
        return loopsToFinish;
    }

    /**
     * <b>getNextBusStop</b><br>
     * <br>
     * public BusStop <b>getNextBusStop</b>()
     *
     * @return next <b>BusStop</b>
     * @throws IndexOutOfBoundsException
     */
    public final BusStop getNextBusStop() {
        return currentBusStop.getNextBusStop();
    }

    /**
     * <b>move</b><br>
     */
    public final void move() {
        final BusBehaviorStrategy behaviorStrategy = busBehaviorStrategyMap.get(getState());
        behaviorStrategy.execute();
    }


    public void setValueToNextStop(int length) {
        getToNextStop().setValue(length);
    }

    public void reachStop(BusStop busStop)
    {
        setCurrentBusStop(busStop);
        setValueToNextStop(getCurrentBusStop().getDistance());
    }

    public void reachNextStop() {
        reachStop(getNextBusStop());
    }

    public void reachDepot() {
        reachStop(BusDepot.getInstance());
    }

    public boolean areLoopsFinished() {
        return getLoopsToFinish().isDownCounted();
    }
    
    public boolean isGetOffRequest(BusStop busStop) {
        return !getPassengerMap().isEmpty() && getPassengerMap().containsKey(busStop);
    }

    public boolean isGetOnRequest(BusStop busStop) {
        return !isFull() && !busStop.getPassengerQueue().isEmpty();
    }

    public boolean isGetOffRequestNow() {
        return isGetOffRequest(getCurrentBusStop());
    }

    public boolean isGetOnRequestNow() {
        return isGetOnRequest(getCurrentBusStop());
    }

    private void occupyBusStop(BusStop busStop) { busStop.setOccupied(true); }

    private void freeBusStop(BusStop busStop) { busStop.setOccupied(false); }

    public void occupyCurrentBusStop() { occupyBusStop(getCurrentBusStop()); }

    public void freeCurrentBusStop() { freeBusStop(getCurrentBusStop()); }

    abstract private class BusBehaviorStrategy {
        abstract void execute();
        public Bus getBus() {
            return Bus.this;
        }
    }

    /**
     * <b>Strategia autobusu czekającego na zajezdni na sygnał.</b>
     * Autobus nic nie robi.
     */
    private final class BusIdleStrategy extends BusBehaviorStrategy {
        @Override
        void execute() {
            //Do nothing
        }
    }

    /**
     * <b>Strategia jadącego autobusu.</b>
     * Dopóki autobus nie dojedzie do przystanka, to jest w drodze.
     */
    private final class BusOnRunStrategy extends BusBehaviorStrategy {
        @Override
        void execute() {
            toNextStop.countdown();
            if (toNextStop.isDownCounted()) {
                try
                {
                    blockingQueue.put(new BusArrivesToBusStop(getBus()));
                } catch (final InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * <b>Strategia autobusu czekającego na zwolnienie się przystanka.</b>
     * Jeśli inny autobus zajmował przystanek,
     * to ten sprawdza, czy przystanek jest nadal zajęty, jak nie to wjeżdża.
     *
     * TODO: jeśli jest kolejkowanie do przystanka, to zaimplementować
     *
     */
    private final class BusWaitingForBusStopStrategy extends BusBehaviorStrategy {
        @Override
        void execute() {
            try
            {
                blockingQueue.put(new BusArrivesToBusStop(getBus()));
            } catch (final InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * <b>Strategia autobusu wracającego do zajezdni.</b>
     * Autobus wraca do zajezdni.
     */
    private final class BusReturningStrategy extends BusBehaviorStrategy {
        @Override
        void execute() {
            toNextStop.countdown();
            if (toNextStop.isDownCounted()) {
                try
                {
                    blockingQueue.put(new BusReturnedToDepot(getBus()));
                } catch (final InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * <b>Strategia autobusu odbywającego przerwę po kursie.</b>
     * Autobus odczekuje i staje się gotowy do dalszej drogi.
     */
    private final class BusReturnedStrategy extends BusBehaviorStrategy {
        @Override
        void execute() {
            cooldownAfterLoops.countdown();
            if (cooldownAfterLoops.isDownCounted()) {
                try
                {
                    blockingQueue.put(new BusReadyToGo(getBus()));
                } catch (final InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Strategia wypuszczania pasażerów.
     */
    private final class BusPutsOutPassengersStrategy extends BusBehaviorStrategy {
        @Override
        void execute() {
            getBus().putOutPassengers();
        }
    }

    /**
     * Strategia zabierania pasażerów.
     */
    private final class BusTakesInPassengersStrategy extends BusBehaviorStrategy {
        @Override
        void execute() {
            getBus().takeInPassengers();
        }
    }

    /**
     * Strategia wypuszczania wszystkich pasażerów
     */
    private final class BusPutsOutAllStrategy extends BusBehaviorStrategy {
        @Override
        void execute() {
            getBus().putOutAll();
        }
    }
}