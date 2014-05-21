package model;

import mockup.Mockup;
import simulator.Simulator;

import java.util.ArrayList;

/**
 * Created by ppeczek on 2014-05-21.
 */
public class Model {
    private static Model ourInstance = new Model();

    private ArrayList<Bus> busContainer = new ArrayList<Bus>();
    private Schedule schedule = Schedule.getInstance();

    public static Model getInstance() {
        return ourInstance;
    }

    private Model() {
        Schedule schedule = Schedule.getInstance();
        Bus testBus = new Bus(schedule);
        testBus.setState(BusState.RUNNING);
        Bus testBus2 = new Bus(schedule);
        Bus testBus3 = new Bus(schedule);

        busContainer.add(testBus);
        busContainer.add(testBus2);
        busContainer.add(testBus3);
    }

    public Mockup createMockup()
    {
        final Mockup mockup = new Mockup(getBusContainer(), schedule.getBusStops());
        return mockup;
    }

    public ArrayList<Bus> getBusContainer() {
        return busContainer;
    }

    public void step()
    {
        for (Bus bus : busContainer) {
            bus.move();
        }
        Simulator simulator = Simulator.getInstance();
//        simulator.generatePassengers(schedule.getBusStops(), simulator.getPassengerGenerationIntensity());

    }
}
