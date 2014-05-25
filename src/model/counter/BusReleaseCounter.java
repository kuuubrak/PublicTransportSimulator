package model.counter;

import event.BusStartSignal;
import model.Bus;
import model.BusDepot;
import view.BusEvent;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by tomasz on 25.05.2014.
 */
public class BusReleaseCounter extends Cooldown {

    private BusDepot busStop;

    public BusReleaseCounter(LinkedBlockingQueue<BusEvent> blockingQueue, int releasePeriod, BusDepot busStop) {
        super(blockingQueue, releasePeriod, null);
        this.busStop = busStop;
    }

    public void throwEvent() {
     try
        {
            getBlockingQueue().put(new BusStartSignal(busStop.getBusQueue().poll()));
        } catch (final InterruptedException e)
        {
            // TODO Auto-generated catch block
           e.printStackTrace();
        }
    }
}
