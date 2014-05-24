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
        this.counted = true;
        this.blockingQueue = blockingQueue;
        this.bus = bus;
    }

    public int countdown() {
        if (value > 0) {
            value--;
        }
        else {
            finishedCounting();
        }
        return value;
    }

    private void finishedCounting() {
        throwEvent();
    }

    public void throwEvent() {

    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
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
