package mockup;

import model.Bus;
import model.BusStop;
import view.SimulatorEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>Mockup</b><br>
 * Makieta.<br>
 * <br>
 * Zawiera <b>rozkład jazdy</b> ( listę <b>Przystanków</b> ) oraz listę <b>Autobusów</b>.
 */
public final class Mockup extends SimulatorEvent
{
    private final ArrayList<Bus> schedule;
    private final ArrayList<BusStop> busStops;
    private final long currentTime;

    public Mockup(final ArrayList<Bus> schedule, final ArrayList<BusStop> busStops, long currentTime) {
        this.schedule = schedule;
        this.busStops = busStops;
        this.currentTime = currentTime;
    }

    /**
     * @return schedule
     */
    public List<Bus> getBuses() {
        return schedule;
    }

    /**
     * @return busStops
     */
    public List<BusStop> getBusStops() {
        return busStops;
    }

    @Override
    public Mockup getMockup() { return this; }

    public long getCurrentTime()
    {
        return currentTime;
    }
}