package event;

import model.BusStop;
import view.SimulatorEvent;

/**
 * Created by ppeczek on 2014-05-25.
 */
public class NewPassengerEvent extends SimulatorEvent {
    private final BusStop from;
    private final BusStop to;

    public NewPassengerEvent(BusStop from, BusStop to) {
        this.from = from;
        this.to = to;
    }

    public BusStop getFrom() {
        return from;
    }

    public BusStop getTo() {
        return to;
    }
}
