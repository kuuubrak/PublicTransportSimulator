package DataModel;

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
    private final List<BusStopBase> busStopBases;

    public Mockup( final List<Bus> schedule, final List<BusStopBase> busStopBases)
    {
        this.schedule = schedule;
        this.busStopBases = busStopBases;
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
    public List<BusStopBase> getBusStopBases()
    {
        return busStopBases;
    }
}