package model.counter;

import model.Bus;
import view.BusEvent;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ppeczek on 2014-05-21.
 */
abstract public class CyclicCounter extends Counter {
    public int max_value;

    public CyclicCounter(LinkedBlockingQueue<BusEvent> blockingQueue, int value, Bus bus) {
        super(blockingQueue, value, bus);
        max_value = value;
    }

    public void reset() {
        initiateCounter(max_value);
    }

    public void finishedCounting() {
        reset();
        throwEvent();
    }
}
