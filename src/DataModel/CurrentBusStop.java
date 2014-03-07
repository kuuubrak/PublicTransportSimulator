package DataModel;

import java.util.ArrayList;

/**
 * <b>BusStopSchedule</b><br>
 * Żongluje <b>Przystankami</b>.<br>
 * Wymaga przy tworzeniu podawania gotowego <b>rozkładu jazdy</b>.<br>
 * 
 * @author dan.krasniak
 *
 */
public final class CurrentBusStop
{
    /** <b>BusStop</b> Container. */
    private final ArrayList<BusStop> busStopContainer;
    /** <b>BusStop</b> last or current. */
    private BusStop lastBusStop;
    /** <b>Index</b> of the current <b>BusStop</b>. */
    private int indexOfBusStop = 0;
    
    public CurrentBusStop( final ArrayList<BusStop> busStopContainer )
    {
        this.busStopContainer = busStopContainer;
        this.lastBusStop = busStopContainer.get( 0 );
    }
    
    /**
     * <b>getNext</b><br>
     * <br>
     * public BusStop <b>getNext</b>()<br>
     * <br>
     * Returns the next <b>BusStop</b>, without changing the <b>CurrentBusStop</b>.
     * <br>
     * @return the next <b>BusStop</b>.
     */
    public final BusStop getNext()
    {
        return get( indexOfBusStop + 1 );
    }
    
    /**
     * <b>setNext</b><br>
     * Sets the <b>CurrentBusStop</b> as the next <b>BusStop</b>.
     */
    public final void setNext()
    {
        lastBusStop = get( ++indexOfBusStop ); 
    }
    
    /**
     * <b>get</b><br>
     * <br>
     * public BusStop <b>get</b>()
     * 
     * @return current or last( if in midway ) <b>BusStop</b>.
     */
    public final BusStop get()
    {
        return lastBusStop;
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
