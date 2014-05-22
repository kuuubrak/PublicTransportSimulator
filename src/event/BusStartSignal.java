package event;

import model.Bus;
import view.BusEvent;

/**
 * Created by ppeczek on 2014-05-21.
 */
public class BusStartSignal extends BusEvent {
    public BusStartSignal(Bus bus) {
        super(bus);
    }
}
