
package DataModel;

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
    private Queue<Passenger> passengerQueue; // TODO zmienić nazwę
    /** Name of the <b>BusStation</b>. */
    private String NAME;
    /** Direction in which the <b>Passengers</b> want to go. */
    private Direction DIRECTION;

    public BusStop( final String name, final Direction direction )
    {
        this.NAME = name;
        this.DIRECTION = direction;
    }

    /**
     * @return the DIRECTION
     */
    public final Direction getDIRECTION()
    {
        return DIRECTION;
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
     * TODO Przemyśleć. Zmienić nazwę, budowę.
     */
    public final Passenger leave()
    {
        return passengerQueue.remove();
    }
}
