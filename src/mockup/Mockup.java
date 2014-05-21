package mockup;

import model.Bus;
import model.BusStop;

import java.io.Serializable;
import java.util.List;

/**
 * <b>Mockup</b><br>
 * Makieta.<br>
 * <br>
 * Zawiera <b>rozkład jazdy</b> ( listę <b>Przystanków</b> ) oraz listę <b>Autobusów</b>.  
 * 
 */
public final class Mockup implements Serializable
{
    private final List<Bus> schedule;
    private final List<BusStop> busStops;

    public Mockup( final List<Bus> schedule, final List<BusStop> busStops)
    {
        this.schedule = schedule;
        this.busStops = busStops;
    }

    /**
     * @return schedule
     */
    public List<Bus> getBuses()
    {
        return schedule;
    }

    /**
     * @return busStops
     */
    public List<BusStop> getBusStops()
    {
        return busStops;
    }
}