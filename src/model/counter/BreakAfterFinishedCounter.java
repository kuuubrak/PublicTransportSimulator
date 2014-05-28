package model.counter;

import event.busevents.BusReadyToGo;
import model.Bus;
import view.SimulatorEvent;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ppeczek on 2014-05-24.
 */
public class BreakAfterFinishedCounter extends CyclicCounter implements Serializable
{
    public BreakAfterFinishedCounter(LinkedBlockingQueue<SimulatorEvent> blockingQueue, int cooldownAfterLoops, Bus bus) {
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
