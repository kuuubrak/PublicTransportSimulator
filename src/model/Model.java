package model;

import mockup.Mockup;
import simulator.Simulator;
import view.BusEvent;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import static simulator.SimulatorConstants.N;

/**
 * Created by ppeczek on 2014-05-21.
 */
public class Model {

    private ArrayList<Bus> busContainer = new ArrayList<Bus>();
    private Schedule schedule = Schedule.getInstance();


    public Model(BlockingQueue<BusEvent> blockingQueue) {
        BusDepot busDepot = BusDepot.getInstance();
        for (int i=0; i<N; ++i) {
            busContainer.add(new Bus(busDepot, blockingQueue));
        }
        //na potrzebe testu
        busContainer.get(0).setState(BusState.RUNNING);
    }

    public Mockup createMockup() {
        final Mockup mockup = new Mockup(getBusContainer(), schedule.getBusStops());
        return mockup;
    }

    public ArrayList<Bus> getBusContainer() {
        return busContainer;
    }

    public void step() {
        for (Bus bus : busContainer) {
            bus.move();
        }
        Simulator simulator = Simulator.getInstance();
//        simulator.generatePassengers(schedule.getBusStops(), simulator.getPassengerGenerationIntensity(), );

    }
}
