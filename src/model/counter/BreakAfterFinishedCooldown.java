package model.counter;

import event.BusReadyToGo;
import model.Bus;
import view.BusEvent;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ppeczek on 2014-05-24.
 */
public class BreakAfterFinishedCooldown extends Cooldown {
    public BreakAfterFinishedCooldown(LinkedBlockingQueue<BusEvent> blockingQueue, int cooldownAfterLoops, Bus bus) {
        super(blockingQueue, cooldownAfterLoops, bus);
    }

    public void throwEvent() {
        try
        {
            getBlockingQueue().put(new BusReadyToGo(getBus()));
        } catch (final InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
