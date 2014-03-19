
package DataModel;

import java.util.LinkedList;
import java.util.Queue;

/**
 * <b>BusStop.</b><br>
 * <br>
 * Buffor, przetrzymuje  <b>Pasażerów</b> do momentu ich wejścia do <b>Autobusu</b> ( niekoniecznie wszyscy od razu wejdą ( pełny <b>Autobus</b> ) ).<br>
 * <b>Pasażerowie</b> oczekujący na miejsce w <b>Autobusie</b> tworzą kolejkę FIFO.<br>
 * Wysiadający mają pierwszeństwo przed wsiadającymi.<br>
 * 
 * @author dan.krasniak
 *
 */
public final class BusStop
{
    /** Queue of <b>Passengers</b> waiting for a place in the <b>Bus</b>. */
    private Queue<Passenger> passengerQueue;
    /** Name of the <b>BusStop</b>. */
    private final String NAME;
    /** <b>Distance</b> in steps required to reach */
    private int distance;

    public BusStop( final String name, final int distance )
    {
        this.NAME = name;
        this.passengerQueue = new LinkedList<Passenger>();
        this.distance = distance;
    }
    
    /**
     * <b>getDistance</b><br>
     * 
     * @return distance - required to reach this <b>BusStop</b>.
     */
    public final int getDistance()
    {
        return distance;
    }
    
    /**
     * @return the NAME
     */
    public final String getNAME()
    {
        return NAME;
    }
    
    /**
     * <b>setInQueue</b>.<br>
     * Sets the <b>Passenger</b> in the <b>Queue</b> of other <b>Passenegrs</b> waiting for a place in the <b>Bus</b>.<br>
     */
    public final void setInQueue( final Passenger passenger )
    {
        passengerQueue.add( passenger );
    }
    
    /**
     * <b>takerAPassenger</b><br>
     * Transfers <b>One Passenger</b> from the <b>BusStop</b>.<br>
     * <br>
     * Takes <b>One Passenger</b> from the head of the <b>PassengerQueue</b>
     * 
     * @return <b>One Passenger</b> from the <b>BusStop</b>
     */
    public final Passenger takeAPassenger()
    {
        return passengerQueue.poll();
    }
    
    /**
     * <b>isEmpty</b><br>
     * 
     * @return true if <b>BusStop</b> has no <b>Passengers</b> in the <b>PassengerQueue</b>.
     */
    public final boolean isEmpty()
    {
        return passengerQueue.isEmpty();
    }
}
