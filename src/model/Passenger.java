package model;


/**
 * <b>Passenger</b><br>
 * Podróżny, pojawia się losowo na <b>Przystankach</b> z losowym <b>Przystankiem</b> docelowym.<br>
 * Na <b>Przystanku</b> tworzy kolejkę FIFO.
 * Moment pojawienia się <b>Pasażera</b> na przystanku oznaczany jako <b>TimeStamp</b>.
 */
public class Passenger {
    /**
     * Unique Number/Color
     */
    private final int ID;
    /**
     * On which step the <b>Passenger</b> appeared at the <b>BusStop</b>
     */
    private final int TIMESTAMP;
    /**
     * <b>Passengers'</b> designated <b>BusStop</b>.
     */
    private BusStop destination;

    /**
     * @param destination Wymagane.
     */
    public Passenger(final BusStop destination, final int timeStamp, final int id) {
        this.destination = destination;
        this.TIMESTAMP = timeStamp;
        this.ID = id;
    }

    /**
     * @return the ID
     */
    public final int getID() {
        return ID;
    }

    /**
     * @return the TIMESTAMP
     */
    public final int getTIMESTAMP() {
        return TIMESTAMP;
    }

    /**
     * @return the destination
     */
    public final BusStop getDestination() {
        return destination;
    }
}
