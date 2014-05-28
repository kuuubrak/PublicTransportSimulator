package mockup;

import model.Bus;
import model.BusStop;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Mockup</b><br>
 * Makieta.<br>
 * <br>
 * Zawiera <b>rozkład jazdy</b> ( listę <b>Przystanków</b> ) oraz listę <b>Autobusów</b>.
 */
public final class Mockup
{
    private ArrayList<MockupBus> schedule = new ArrayList<>();
    private ArrayList<MockupBusStop> busStops = new ArrayList<>();
    private final long currentTime;

    public Mockup(final ArrayList<Bus> schedule, final ArrayList<BusStop> busStops, long currentTime) {
        for (Bus b: schedule) {
            this.schedule.add(new MockupBus(b));
        }
        for (BusStop bs: busStops) {
            this.busStops.add(new MockupBusStop(bs));
        }
        this.currentTime = currentTime;
    }

    /**
     * @return schedule
     */
    public List<MockupBus> getBuses() {
        return schedule;
    }

    /**
     * @return busStops
     */
    public List<MockupBusStop> getBusStops() {
        return busStops;
    }

    public long getCurrentTime()
    {
        return currentTime;
    }
}