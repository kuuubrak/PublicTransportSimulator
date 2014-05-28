package model;

import event.busevents.BusPutOutAll;
import event.busevents.BusPutOutPassengers;
import event.busevents.BusTookInPassengers;
import main.SimulatorConstants;
import model.counter.*;
import view.SimulatorEvent;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
public final class Bus implements EventListener, Serializable
{
    /**
     * How many <b>Passengers</b> can one <b>Bus</b> hold.
     */
    private static final int MAX_SEATS = SimulatorConstants.noOfSeatsInBus;
    /**
     * A container of currently held <b>Passengers</b>
     */
    public BlockingQueue<SimulatorEvent> blockingQueue;
    private Map<BusStop, Passenger> passengerMap = new HashMap<BusStop, Passenger>();
    private BusStop currentBusStop;
    private BusState state;
    private ToNextStopDistanceCounter toNextStop;
    private LoopsCounter loopsToFinish;
    private BreakAfterFinishedCounter cooldownAfterLoops;
    private ReturnToDepotCooldown returnToDepotCooldown;
    private final int ID;
    private Map<BusState, BusBehaviorStrategy> busBehaviorStrategyMap = new HashMap<BusState, BusBehaviorStrategy>();

    private static class IDGenerator{
        private static int lastId = 0;

        public static int getNextId(){
            return lastId++;
        }
    }

    public Bus(BusStop startStation, LinkedBlockingQueue<SimulatorEvent> blockingQueue) {
        this.blockingQueue = blockingQueue;
        this.currentBusStop = startStation;
        this.state = BusState.READY_TO_GO;
        this.toNextStop = new ToNextStopDistanceCounter(blockingQueue, this.currentBusStop.getDistanceToNextStop(), this);
        this.loopsToFinish = new LoopsCounter(blockingQueue, SimulatorConstants.loops, this);
        this.cooldownAfterLoops = new BreakAfterFinishedCounter(blockingQueue, SimulatorConstants.cooldownAfterLoops, this);
        this.returnToDepotCooldown = new ReturnToDepotCooldown(blockingQueue, SimulatorConstants.depotTerminusDistance, this);
        this.busBehaviorStrategyMap = getBusBehaviorStrategyMap();
        ID = IDGenerator.getNextId();
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

    public int getID(){
        return ID;
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
        if (passenger != null) {
            passengerMap.put(passenger.getDestination(), passenger);
        }

    }

    private final void putOutPassengers() {
        putOutPassenger(getCurrentBusStop());
        if (!isGetOffRequestNow()) {
            try
            {
                blockingQueue.put(new BusPutOutPassengers(this));
            } catch (final InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private final Passenger putOutPassenger(BusStop busStop) {
//        System.out.println("Liczba pasażerów:" + getPassengerMap().size());
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
        passenger.setTIMESTAMP(System.currentTimeMillis());
     //   getPassengerMap().remove(passenger.getDestination(), passenger); // czemu to ciagle rzuca errora?
        BusStop busStop1 = getCurrentBusStop();
        if (!currentBusStop.equals(busStop1)) {
            busStop1.getPassengerQueue().add(passenger);
        }

    }

    public final void comeback() {
        setCounterToNextStop(SimulatorConstants.depotTerminusDistance);
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


    public void setCounterToNextStop(int length) {
        getToNextStop().initiateCounter(length);
    }

    public void reachStop(BusStop busStop)
    {
        System.out.println(this + " Liczba pasażerów:" + getPassengerMap().size());
        setCurrentBusStop(busStop);
        setCounterToNextStop(getCurrentBusStop().getDistanceToNextStop());
    }

    public void reachNextStop() {
        reachStop(getNextBusStop());
    }

    public void reachDepot() {
        reachStop(BusDepot.getInstance());
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

    public boolean isNextStopOccupied() { return getNextBusStop().isOccupied(); }

    private boolean onTerminusAlready = false;// ugly fix
    public void terminusCheck() {
        if (getCurrentBusStop() instanceof BusTerminus) {
            if(!onTerminusAlready) {
                getLoopsToFinish().countdown();
                onTerminusAlready = true;
            }
            System.out.println(this + ": To finish:" + getLoopsToFinish().getValue());
        }else{
            onTerminusAlready =false;
        }
    }

    public boolean isFinished() {
        return getLoopsToFinish().isDownCounted();
    }

    abstract private class BusBehaviorStrategy implements Serializable {
        abstract void execute();
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
        }
    }

    /**
     * <b>Strategia autobusu czekającego na zwolnienie się przystanka.</b>
     * Jeśli inny autobus zajmował przystanek,
     * to ten sprawdza, czy przystanek jest nadal zajęty.
     * Jeśli jest wolny, to wjeżdża na niego i w przypadku zakończenia trasy
     * wysadza wszystkich pasażerów, w przeciwnym razie standardowo: najpierw opuszczają
     * go pasażerowie, dla których jest to przystanek docelowy, a następnie wsiadają do niego
     * kolejne osoby w miarę wolnych miejsc.
     *
     */
    private final class BusWaitingForBusStopStrategy extends BusBehaviorStrategy {
        @Override
        void execute() {
            if (!Bus.this.isNextStopOccupied()) {
                reachNextStop();
                occupyCurrentBusStop();
                if (isFinished()) {
                    setState(BusState.PUT_OUT_ALL);
                }
                else {
                    if (isGetOffRequestNow()) {
                        setState(BusState.PUT_OUT);
                    }
                    else {
                        setState(BusState.TAKE_IN);
                    }
                }
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
            returnToDepotCooldown.countdown();
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
        }
    }

    /**
     * Strategia wypuszczania pasażerów.
     */
    private final class BusPutsOutPassengersStrategy extends BusBehaviorStrategy {
        @Override
        void execute() {
            Bus.this.putOutPassengers();
        }
    }

    /**
     * Strategia zabierania pasażerów.
     */
    private final class BusTakesInPassengersStrategy extends BusBehaviorStrategy {
        @Override
        void execute() {
            Bus.this.takeInPassengers();
        }
    }

    /**
     * Strategia wypuszczania wszystkich pasażerów
     */
    private final class BusPutsOutAllStrategy extends BusBehaviorStrategy {
        @Override
        void execute() {
            Bus.this.putOutAll();
        }
    }
}
