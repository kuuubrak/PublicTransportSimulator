package model.counter;

import model.Bus;
import view.BusEvent;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ppeczek on 2014-05-21.
 */
abstract public class Cooldown extends Counter {
    public int max_value;

    public Cooldown(LinkedBlockingQueue<BusEvent> blockingQueue, int value, Bus bus) {
        super(blockingQueue, value, bus);
        max_value = value;
    }

    public void reset() {
        setValue(max_value);
    }

    private void finishedCounting() {
        reset();
        throwEvent();
    }
}
