package DataModel;


/**
 * <b>Passenger</b><br>
 * Podróżny, pojawia się losowo na <b>Przystankach</b> z losowym <b>Przystankiem</b> docelowym.<br>
 * Na <b>Przystanku</b> tworzy kolejkę FIFO.
 */
public class Passenger
{
    /** <b>Passengers'</b> designated <b>BusStop</b>. */
    private BusStop destination;
    /** Unique Number/Color */
    private int id;
//    /** Location. Either in which <b>Bus</b> or <b>BusStop</b>. */
//    private Location location;
    
    /**
     * 
     * @param location Wymagane
     * @param destionation Wymagane, niekoniecznie typu BusStop.
     */
    public Passenger( BusStop destionation )
    {
        this.destination = destination;
    }
    
    /**
     * <b>waitForTheBus</b><br>
     * Joins this <b>Passegner</b> to the <b>Queue</b> on the <b>BusStop</b> on which this <b>Passenger</b> is located.<br>
     */
    public void waitForTheBus()
    {
        
    }
    
    /**
     * <b>enter</b><br>
     * Enters the <b>Bus</b>.
     */
    public void enter()
    {
        // TODO Może zwracać aktualną Lokację.
    }
    
    /**
     * <b>leave</b><br>
     * Leaves the <b>Bus</b>.
     */
    public void leave()
    {
        // TODO Może zwracać aktualną Lokację.
    }
}
