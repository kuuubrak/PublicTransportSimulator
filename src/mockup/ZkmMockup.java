package mockup;

import model.Bus;
import model.BusStop;
import view.SimulatorEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ppeczek on 2014-05-28.
 */
public class ZkmMockup extends SimulatorEvent {
    private final ArrayList<Bus> schedule;
    private final ArrayList<BusStop> busStops;
    private final long currentTime;

    public ZkmMockup(final ArrayList<Bus> schedule, final ArrayList<BusStop> busStops, long currentTime) {
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
    public ZkmMockup getZkmMockup() { return this; }

    public long getCurrentTime()
    {
        return currentTime;
    }
}
