package mockup;

import model.Passenger;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by ppeczek on 2014-05-28.
 */
public class MockupBus {
    private final ArrayList<Passenger> passengerMap;
    private final MockupBusStop currentBusStop;
    private final int lengthPassed;
    private final UUID ID;

    public MockupBus(ArrayList<Passenger> passengerMap, MockupBusStop currentBusStop, int lengthPassed, UUID id) {
        this.passengerMap = passengerMap;
        this.currentBusStop = currentBusStop;
        this.lengthPassed = lengthPassed;
        ID = id;
    }

    public ArrayList<Passenger> getPassengerMap() {
        return passengerMap;
    }

    public MockupBusStop getCurrentBusStop() {
        return currentBusStop;
    }

    public int getLengthPassed() {
        return lengthPassed;
    }

    public UUID getID() {
        return ID;
    }
}
