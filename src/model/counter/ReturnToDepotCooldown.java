package model.counter;

import event.BusReturnedToDepot;
import model.Bus;
import view.BusEvent;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ppeczek on 2014-05-24.
 */
public class ReturnToDepotCooldown extends Cooldown {
    public ReturnToDepotCooldown(LinkedBlockingQueue<BusEvent> blockingQueue, int value, Bus bus) {
        super(blockingQueue, value, bus);
    }

    public void throwEvent() {
        try {
            getBlockingQueue().put(new BusReturnedToDepot(getBus()));
        } catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
