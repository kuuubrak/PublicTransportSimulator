package model;

import event.BusArrivesToBusStop;
import event.BusReadyToGo;
import event.BusReturnedToDepot;
import event.BusStartSignal;
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
     * How many <b>Passengers</b> the <b>Bus</b> is already holding.
     */
    private int seatsTaken = 0;
    /**
     * A container of currently held <b>Passengers</b>
     */
    private ArrayList<Passenger> passengerContainer;
    private BusStop currentBusStop;
    private BusState state;
    private Counter toNextStop;
    private Counter loopsToFinish;
    private Cooldown cooldownAfterLoops;
    private Map<BusState, BusBehaviorStrategy> busBehaviorStrategyMap = new HashMap<BusState, BusBehaviorStrategy>();
    public BlockingQueue<BusEvent> blockingQueue;

    public Bus(BusStop startStation, BlockingQueue<BusEvent> blockingQueue) {
        this.passengerContainer = new ArrayList<Passenger>();
        this.currentBusStop = startStation;
        this.state = BusState.IN_DEPOT;
        this.toNextStop = new Counter(this.currentBusStop.getDistance());
        this.loopsToFinish = new Cooldown(SimulatorConstants.loops);
        this.cooldownAfterLoops = new Cooldown(SimulatorConstants.cooldownAfterLoops);
        this.busBehaviorStrategyMap = getBusBehaviorStrategyMap();
        this.blockingQueue = blockingQueue;
    }

    private Map<BusState, BusBehaviorStrategy> getBusBehaviorStrategyMap() {
        final Map<BusState, BusBehaviorStrategy> resultMap = new HashMap<BusState, BusBehaviorStrategy>();
        resultMap.put(BusState.IN_DEPOT, new BusIdleStrategy());
        resultMap.put(BusState.RUNNING, new BusOnRunStrategy());
        resultMap.put(BusState.ON_STOP, new BusAtBusStopStrategy());
        resultMap.put(BusState.WAITING, new BusWaitingForBusStopStrategy());
        resultMap.put(BusState.FINISHED, new BusReturnSignalStrategy());
        resultMap.put(BusState.HAVING_BREAK, new BusReturnedStrategy());
        return Collections.unmodifiableMap(resultMap);
    }

    /**
     * @return the passengerContainer
     */
    public final ArrayList<Passenger> getPassengerContainer() {
        return passengerContainer;
    }

    /**
     * <b>addPassenger</b><br>
     * Adds a <b>Passenger</b> to the <b>PassengerContainer</b>.
     *
     * @param passenger
     */
    private final void addPassenger(final Passenger passenger) {
        passengerContainer.add(passenger);
        ++seatsTaken;
    }

    /**
     * <b>takeInPassengers</b><br>
     * Fills the <b>Bus</b> with <b>Passengers</b> until there are no free seats left or the <b>BusStop</b> is empty.
     */
    public final void takeInPassengers() {
        while (!isFull() && !getCurrentBusStop().isEmpty()) {
            addPassenger(getCurrentBusStop().takePassenger());
        }
    }

    /**
     * <b>isFull</b><br>
     *
     * @return true if the <b>Bus</b> is full.
     */
    private final boolean isFull() {
        return (MAX_SEATS == seatsTaken);
    }

    /**
     * <b>getNumberOfFreeSeats</b><br>
     *
     * @return the number of free seats in the <b>Bus</b>
     */
    public final int getNumberOfFreeSeats() {
        return MAX_SEATS - seatsTaken;
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
        return currentBusStop.getRoute().getToBusStop();
    }

    public final void remove(Passenger passenger) {
        passengerContainer.remove(passenger);
    }

    /**
     * <b>move</b><br>
     * Moves to the next location.<br>
     */
    public final void move() {
        final BusBehaviorStrategy behaviorStrategy = busBehaviorStrategyMap.get(getState());
        behaviorStrategy.execute();
    }

    abstract private class BusBehaviorStrategy {
        abstract void execute();
        public Bus getBus() {
            return Bus.this;
        }
    }

    private final class BusIdleStrategy extends BusBehaviorStrategy {
        @Override
        void execute() {
            //Do nothing, ew. cooldown
        }
    }

    private final class BusOnRunStrategy extends BusBehaviorStrategy {
        @Override
        void execute() {

            //TODO: throw event if toNextStop drops to 0
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

    private final class BusAtBusStopStrategy extends BusBehaviorStrategy {
        @Override
        void execute() {
            try
            {
                blockingQueue.put(new BusStartSignal(getBus()));
            } catch (final InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private final class BusWaitingForBusStopStrategy extends BusBehaviorStrategy {
        @Override
        void execute() {
            //TODO: sprawdz

        }
    }

    private final class BusReturnSignalStrategy extends BusBehaviorStrategy {
        @Override
        void execute() {
            toNextStop.countdown();
            if (toNextStop.isDownCounted()) {
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
    }

    private final class BusReturnedStrategy extends BusBehaviorStrategy {
        @Override
        void execute() {
            //TODO: obsługa eventem
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

    public void setValueToNextStop(int length) {
        getToNextStop().setValue(length);
    }

    public void reachNextStop() {
        System.out.println("Stacja: " + getCurrentBusStop().getNAME());
        System.out.println("Następna stacja: " + getNextBusStop().getNAME());
        setCurrentBusStop(getNextBusStop());
        getToNextStop().setValue(getCurrentBusStop().getDistance());
    }

    public boolean areLoopsFinished() {
        return getLoopsToFinish().isDownCounted();
    }
}
