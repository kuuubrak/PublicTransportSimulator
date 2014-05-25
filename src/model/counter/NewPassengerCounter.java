package model.counter;

import model.Bus;
import view.BusEvent;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ppeczek on 2014-05-25.
 */
public class NewPassengerCounter extends Counter {

    public NewPassengerCounter(LinkedBlockingQueue<BusEvent> blockingQueue, int value, Bus bus) {
        super(blockingQueue, value, bus);
    }
}
