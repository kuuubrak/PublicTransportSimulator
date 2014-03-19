package DataModel;

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
public final class Bus
{
    /** How many <b>Passengers</b> can one <b>Bus</b> hold. */
    private static final int MAX_SEATS = 10;
    /** How many <b>Passengers</b> the <b>Bus</b> is already holding. */
    private int seatsTaken = 0;
    /** A container of currently held <b>Passengers</b> */
    private ArrayList<Passenger> passengerContainer;
    /** A manager of BusStops. */
    private BusStopIterator busStopIterator;
    /** Number of laps already done. */
    private int laps = 0; // TODO
    /** Number of laps to do */
    private int lapsToDo = 3; // TODO
    /** Number of steps to wait after finishing all the laps */
    private int waitFor = 10; // TODO
    
    public Bus( final ArrayList<BusStop> schedule )
    {
        setSchedule( schedule );
        this.passengerContainer = new ArrayList<Passenger>();
    }
    
    /**
     * @return the passengerContainer
     */
    public final ArrayList<Passenger> getPassengerContainer()
    {
        return passengerContainer;
    }
    
    /**
     * @param ArrayList< BusStop > the <b>BusStop Schedule</b>.
     */
    private final void setSchedule( final ArrayList<BusStop> schedule )
    {
        this.busStopIterator = new BusStopIterator( schedule );
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
            addPassenger( getCurrentBusStop().takeAPassenger() );
        }
    }
    
    /**
     * <b>isFull</b><br>
     * 
     * @return true if the <b>Bus</b> is full.
     */
    private final boolean isFull()
    {
        if( MAX_SEATS == seatsTaken ) return true;
        return false;
    }
    
    /**
     * <b>getCurrentBusStop</b><br>
     * <br>
     * public BusStop <b>getCurrentBusStop</b>()
     * 
     * @return current <b>BusStop</b>
     */
    public final BusStop getCurrentBusStop()
    {
        return busStopIterator.getCurrent();
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
        return busStopIterator.getNext();
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
        busStopIterator.setNext(); // TODO
    }
    
    /**
     * <b>BusStopIterator</b><br>
     * Iterator jednokierunkowy po <b>Przystankach</b>.<br>
     * <br>
     * Wymaga, przy tworzeniu, podania gotowego <b>rozkładu jazdy</b>.<br>
     * Zwraca informacje o aktualnym i następnym <b>Przystanku</b>.<br>
     * 
     * 
     * @author dan.krasniak
     *
     */
    private final class BusStopIterator
    {
        /** <b>BusStop</b> Container. */
        private final ArrayList<BusStop> busStopContainer;
        /** <b>Index</b> of the current <b>BusStop</b>. */
        private int currentBusStopIndex;
        /** <b>Distance</b> passed in steps */
        private int distancePassed = 0; // TODO
        
        private BusStopIterator( final ArrayList<BusStop> busStopContainer )
        {
            this.busStopContainer = busStopContainer;
            this.currentBusStopIndex = 0;
            
        }
        
        /**
         * <b>getNext</b><br>
         * <br>
         * public BusStop <b>getNext</b>()<br>
         * <br>
         * Returns the next <b>BusStop</b>, without changing the <b>CurrentBusStop</b>.
         * <br>
         * @return the next <b>BusStop</b>.
         * @throws IndexOutOfBoundsException
         */
        private final BusStop getNext()
        {
            return get( currentBusStopIndex + 1 );
        }
        
        /**
         * <b>setNext</b><br>
         * Moves forward in the iteration and changes the value of <b>currentBusStop</b> to the value of next <b>BusStop</b>.
         * @throws IndexOutOfBoundsException if already at the last <b>BustStop</b>
         */
        private final void setNext() throws IndexOutOfBoundsException
        {
            if( busStopContainer.size() != currentBusStopIndex + 1 )
            {
                ++currentBusStopIndex;
            }
            else throw new IndexOutOfBoundsException( "currentBusStopIndex == size()" );
        }
        
        /**
         * <b>getCurrent</b><br>
         * <br>
         * public BusStop <b>get</b>()
         * 
         * @return current <b>BusStop</b>.
         */
        private final BusStop getCurrent()
        {
            return get( currentBusStopIndex );
        }
        
        /**
         * <b>get</b><br>
         * <br>
         * private BusStop <b>get</b>( int index )
         * 
         * @return  the <b>BusStop</b> of the given <b>index</b>.
         */
        private final BusStop get( final int index )
        {
            return busStopContainer.get( index );
        }

    }

}
