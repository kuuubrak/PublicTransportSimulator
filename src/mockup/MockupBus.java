package mockup;

import model.Bus;
import model.Passenger;

import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by ppeczek on 2014-05-28.
 */
public class MockupBus {
    private final ArrayList<Passenger> passengerList;
    private final String currentBusStop;
    private final int lengthPassed;
    private final Integer ID;
    private RoundingMode passengerMap;

    public MockupBus(final Bus bus) {
        this.passengerList = new ArrayList<Passenger>(bus.getPassengerMap().values());
        this.currentBusStop = bus.getCurrentBusStop().getNAME();
        this.lengthPassed = bus.getCurrentBusStop().getRoute().getLength() - bus.getToNextStop().getValue();
        this.ID = bus.getID();
    }

    public ArrayList<Passenger> getPassengerList() {
        return passengerList;
    }

    public String getCurrentBusStop() {
        return currentBusStop;
    }

    public int getLengthPassed() {
        return lengthPassed;
    }

    public Integer getID() {
        return ID;
    }

    public RoundingMode getPassengerMap() {
        return passengerMap;
    }
}
