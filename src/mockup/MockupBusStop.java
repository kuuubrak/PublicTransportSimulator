package mockup;

import java.util.Queue;
import java.util.UUID;

/**
 * Created by ppeczek on 2014-05-28.
 */
public class MockupBusStop {
    private final UUID ID;
    private final String NAME;
    private Queue<MockupPassenger> passengerQueue;
    private final int toNextStop;
    private final MockupBusStopType busStopType;

    public MockupBusStop(UUID id, String name, Queue<MockupPassenger> passengerQueue, int toNextStop, MockupBusStopType busStopType) {
        ID = id;
        NAME = name;
        this.passengerQueue = passengerQueue;
        this.toNextStop = toNextStop;
        this.busStopType = busStopType;
    }

    public UUID getID() {
        return ID;
    }

    public String getNAME() {
        return NAME;
    }

    public Queue<MockupPassenger> getPassengerQueue() {
        return passengerQueue;
    }

    public int getToNextStop() {
        return toNextStop;
    }
}
