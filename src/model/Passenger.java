package model;


import java.util.UUID;

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
    private final UUID ID;
    /**
     * On which step the <b>Passenger</b> appeared at the <b>BusStop</b>
     */
    private final float TIMESTAMP;
    /**
     * <b>Passengers'</b> designated <b>BusStop</b>.
     */
    private BusStop destination;

    /**
     * @param destination Wymagane.
     */
    public Passenger(final BusStop destination, final float timeStamp) {
        this.destination = destination;
        this.TIMESTAMP = timeStamp;
        this.ID = UUID.randomUUID();
    }

    public final float getWaitingTime()
    {
        return System.currentTimeMillis() - TIMESTAMP;
    }

    /**
     * @return the ID
     */
    public final UUID getID() {
        return ID;
    }

    /**
     * @return the TIMESTAMP
     */
    public final float getTIMESTAMP() {
        return TIMESTAMP;
    }

    /**
     * @return the destination
     */
    public final BusStop getDestination() {
        return destination;
    }
}
