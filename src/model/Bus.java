package model;

import simulator.SimulatorConstants;

import java.util.ArrayList;

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
 * 
 * @author dan.krasniak
 *
 */
public final class Bus extends TrafficObject
{
    /** How many <b>Passengers</b> can one <b>Bus</b> hold. */
    private static final int MAX_SEATS = 10;
    /** How many <b>Passengers</b> the <b>Bus</b> is already holding. */
    private int seatsTaken = 0;
    /** A container of currently held <b>Passengers</b> */
    private ArrayList<Passenger> passengerContainer;
    private BusStop currentBusStop;
    private BusState state;
    private Counter toNextStop;
    private Counter loopsToFinish;
    private Cooldown cooldownAfterLoops;
    
    public Bus(Schedule schedule)
    {
        this.passengerContainer = new ArrayList<Passenger>();
        this.currentBusStop = schedule.getBusDepot();
        this.state = BusState.IN_DEPOT;
        this.toNextStop = new Counter(this.currentBusStop.getDistance());
        this.loopsToFinish = new Counter(SimulatorConstants.loops);
    }
    
    /**
     * @return the passengerContainer
     */
    public final ArrayList<Passenger> getPassengerContainer()
    {
        return passengerContainer;
    }

    /**
     * <b>addPassenger</b><br>
     * Adds a <b>Passenger</b> to the <b>PassengerContainer</b>.
     * 
     * @param passenger
     */
    private final void addPassenger( final Passenger passenger )
    {
        passengerContainer.add( passenger );
        ++seatsTaken;
    }
    
    /**
     * <b>takeInPassengers</b><br>
     * Fills the <b>Bus</b> with <b>Passengers</b> until there are no free seats left or the <b>BusStop</b> is empty.
     */
    public final void takeInPassengers()
    {
        while( !isFull() && !getCurrentBusStop().isEmpty() )
        {
            addPassenger( getCurrentBusStop().takePassenger() );
        }
    }
    
    /**
     * <b>isFull</b><br>
     * 
     * @return true if the <b>Bus</b> is full.
     */
    private final boolean isFull() { return ( MAX_SEATS == seatsTaken); }

    /**
     * <b>getNumberOfFreeSeats</b><br>
     *
     * @return the number of free seats in the <b>Bus</b>
     */
    public final int getNumberOfFreeSeats()
    {
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

    /**
     * <b>getNextBusStop</b><br>
     * <br>
     * public BusStop <b>getNextBusStop</b>()
     * 
     * @return next <b>BusStop</b>
     * @throws IndexOutOfBoundsException
     */
    public final BusStop getNextBusStop()
    {
        return currentBusStop.getRoute().getToBusStop();
    }

    public final void remove( Passenger passenger )
    {
        passengerContainer.remove( passenger );
    }
    
    /**
     * <b>move</b><br>
     * Moves to the next location.<br>
     */
    public final void move()
    {
        //TODO: implement Strategy Pattern
        if (getState() == BusState.RUNNING) {
            //TODO: throw event if toNextStop drops to 0
            toNextStop.countdown();
            System.out.println("Obecny: " + currentBusStop.getNAME() + ", do następnego: " + toNextStop.getValue());
            if (toNextStop.isDownCounted()) {
                //TODO: if stop empty
                setCurrentBusStop(getNextBusStop());
                toNextStop.setValue(currentBusStop.getDistance());
                if (currentBusStop instanceof BusTerminus) {
                    loopsToFinish.countdown();
                    if (loopsToFinish.isDownCounted()) {
                        setState(BusState.FINISHED);
                    }
                }
                setState(BusState.ON_STOP);
                //TODO: else setState WAITING
            }
        }
        if (getState() == BusState.WAITING) {
            //TODO: sprawdz
        }
        if (getState() == BusState.ON_STOP) {
            //TODO: zabieraj pasazerow
            setState(BusState.RUNNING);
        }
        if (getState() == BusState.FINISHED) {
            if (getCurrentBusStop().equals(BusDepot.getInstance())) {
                //TODO: obsługa eventem
                cooldownAfterLoops.countdown();
                if (cooldownAfterLoops.isDownCounted()) {
                    setState(BusState.IN_DEPOT);
                }
            }
        }
    }

}
