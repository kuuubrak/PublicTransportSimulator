package DataModel;

import java.util.ArrayList;

/**
 * Bus.<br>
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
    private ArrayList<Passenger> passengerContainer = new ArrayList<Passenger>();
    /** A manager of BusStops. */
    private CurrentBusStop currentBusStop = null;
    /** Direction in which the <b>Bus</b> is heading> */
    private Direction direction = null;
    
    /**
     * @return the direction
     */
    public final Direction getDirection()
    {
        return direction;
    }
    
    /**
     * @param direction the direction to set
     */
    public final void setDirection( Direction direction )
    {
        this.direction = direction;
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
    public final void setSchedule( final ArrayList<BusStop> schedule )
    {
        this.currentBusStop = new CurrentBusStop( schedule );
    }
    
    

}
