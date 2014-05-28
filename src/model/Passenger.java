package model;


import java.io.Serializable;
import java.util.UUID;

/**
 * <b>Passenger</b><br>
 * Podróżny, pojawia się losowo na <b>Przystankach</b> z losowym <b>Przystankiem</b> docelowym.<br>
 * Na <b>Przystanku</b> tworzy kolejkę FIFO.
 * Moment pojawienia się <b>Pasażera</b> na przystanku oznaczany jako <b>TimeStamp</b>.
 */
public class Passenger implements Serializable
{
    /**
     * Unique id
     */
    private final UUID ID;
    /**
     * On which step the <b>Passenger</b> appeared at the <b>BusStop</b>
     */
    private Long TIMESTAMP;
    /**
     * <b>Passengers'</b> designated <b>BusStop</b>.
     */
    private BusStop destination;

    /**
     * @param destination Wymagane.
     */
    public Passenger(final BusStop destination) {
        this.destination = destination;
        this.TIMESTAMP = SimulationTimer.getInstance().getTime();
        this.ID = UUID.randomUUID();
    }

    //TODO: Nie wiem czy takie odwołanie do SimulationTimer jest poprawne
    public final Long getWaitingTime()
    {
        return SimulationTimer.getInstance().getTime() - TIMESTAMP;
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
    public final Long getTIMESTAMP() {
        return TIMESTAMP;
    }

    public void setTIMESTAMP(Long TIMESTAMP) {
        this.TIMESTAMP = TIMESTAMP;
    }

    /**
     * @return the destination
     */
    public final BusStop getDestination() {
        return destination;
    }
}
