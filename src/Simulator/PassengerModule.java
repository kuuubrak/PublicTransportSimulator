package Simulator;

import DataModel.Bus;
import DataModel.BusStop;
import DataModel.Passenger;

/**
 * <b>PassengerModule</b><br>
 * 
 * @author dan.krasniak
 *
 */
public final class PassengerModule
{
    /**
     * <b>generatePassenger</b><br>
     * Generates a new <b>Passenger</b> with an unique number and given destination.
     */
    private final Passenger generatePassenger( final BusStop destination )
    {
        return new Passenger( destination, Simulator.getTime(), (int)( Math.random() * 10000 ) ); //TODO
    }
    
    /**
     * <b>setPassenger</b><br>
     * Set newly generated <b>Passenger</b> at a given or random <b>BusStop</b> with a given or random <b>Destination</b>.
     */
    public final void setPassenger( final BusStop location, final BusStop destination )
    {
        location.setInQueue( generatePassenger( destination ) ); // TODO
    }
    
    /**
     * <b>transferPassengers</b><br>
     * Transfers <b>Passengers</b> between <b>Bus</b> and <b>BusStop</b>.<br>
     * <br>
     * @param bus
     */
    public final void transferPassengers( final Bus bus )
    {
        removeFromBus( bus );
        bus.takeInPassengers();
    }

    /**
     * <b>removeFromBus</b><br>
     * Removes <b>Passengers</b> which reached their destination.<br>
     * 
     * @param bus
     */
    private void removeFromBus( final Bus bus )
    {
        for( Passenger passenger : bus.getPassengerContainer() )
        {
            if( isDestination( passenger, bus.getCurrentBusStop() ) )
            {
                bus.remove( passenger );
            }
        }
    }

    /**
     * <b>isDestination</b><br>
     * Checks if given dstination is the one on which the <b>Passenger</b> wants to get.<br>
     * 
     * @param passenger
     * @param currentLocation - compares with <b>Passengers'</b> destination.
     * @return true if <b>Passenger</b> is located at his designated <b>BusStop</b>.
     */
    public final boolean isDestination( Passenger passenger, BusStop currentLocation )
    {
        if( passenger.getDestination() == currentLocation ) return true;
        return false;
    }
}
