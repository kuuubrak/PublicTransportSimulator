package model.counter;

import model.Bus;
import view.BusEvent;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ppeczek on 2014-05-20.
 */
abstract public class Counter {
    private int value;
    private boolean counted;
    private LinkedBlockingQueue<BusEvent> blockingQueue;
    private Bus bus;

    public Counter(LinkedBlockingQueue<BusEvent> blockingQueue, int value, Bus bus) {
        this.value = value;
        this.counted = false;
        this.blockingQueue = blockingQueue;
        this.bus = bus;
    }

    public int countdown() {
//        if (isDownCounted()) {
//            throw new CounterNotInitiatedException();
//        }
        if (value > 0) {
            value--;
        }
        else {
            setCounted(true);
            finishedCounting();
        }
        return value;
    }

    public void finishedCounting() {
        throwEvent();
    }

    public void throwEvent() {

    }

    public int getValue() {
        return value;
    }

    public void initiateCounter(int value) {
        this.counted = false;
        this.value = value;
    }

    public void setCounted(boolean counted) {
        this.counted = counted;
    }

    public boolean isDownCounted() {
        return counted;
    }

    public LinkedBlockingQueue<BusEvent> getBlockingQueue() {
        return blockingQueue;
    }

    public Bus getBus() {
        return bus;
    }
}
