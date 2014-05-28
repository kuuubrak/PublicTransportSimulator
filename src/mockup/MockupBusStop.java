package mockup;

import model.BusDepot;
import model.BusStop;
import model.BusTerminus;
import model.Passenger;

import java.util.*;

/**
 * Created by ppeczek on 2014-05-28.
 */
public class MockupBusStop {
    private final UUID ID;
    private final String NAME;
    private Queue<MockupPassenger> passengerQueue = new LinkedList<MockupPassenger>();
    private final int toNextStop;
    private final MockupBusStopType busStopType;

    public MockupBusStop(final BusStop busStop) {
        this.ID = busStop.getID();
        this.NAME = busStop.getNAME();
        for (Passenger p: busStop.getPassengerQueue()) {
            this.passengerQueue.add(new MockupPassenger(p));
        }
        this.toNextStop = busStop.getRoute().getLength();
        if (busStop instanceof BusDepot) {
            this.busStopType = MockupBusStopType.DEPOT;
        }
        else if (busStop instanceof BusTerminus) {
            this.busStopType = MockupBusStopType.TERMINUS;
        }
        else {
            this.busStopType = MockupBusStopType.STOP;
        }
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

    public MockupBusStopType getBusStopType() {
        return busStopType;
    }
}
