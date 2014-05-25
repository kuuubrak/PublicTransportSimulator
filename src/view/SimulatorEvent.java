package view;

import model.Bus;
import model.BusStop;

/**
 * Created by ppeczek on 2014-05-25.
 */
public class SimulatorEvent {
    public Bus getBus() {
        return null;
    }

    public BusStop getTo() { return null; }

    public BusStop getFrom() { return null; }

    public int getMin() {
        return 0;
    }

    public int getMax() {
        return 0;
    }

    public boolean isContinuous() {
        return false;
    }
}
