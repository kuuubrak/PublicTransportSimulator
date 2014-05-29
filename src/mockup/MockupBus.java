package mockup;

import model.Bus;
import model.Passenger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by ppeczek on 2014-05-28.
 */
public class MockupBus implements Serializable{
    private final ArrayList<MockupPassenger> passengerList;
    private final String currentBusStop;
    private final int lengthPassed;
    private final Integer ID;

    public MockupBus(final Bus bus) {
        this.passengerList = new ArrayList<>();
        for(LinkedList<Passenger> linkedList: bus.getPassengerList().values()) {
            for(Passenger passenger: linkedList){
                this.passengerList.add(new MockupPassenger(passenger));
            }
        }
        this.currentBusStop = bus.getCurrentBusStop().getNAME();
        this.lengthPassed = bus.getCurrentBusStop().getRoute().getLength() - bus.getToNextStop().getValue();
        this.ID = bus.getID();
    }

    public ArrayList<MockupPassenger> getPassengerList() {
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
}
