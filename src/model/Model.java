package model;

import main.SimulatorConstants;
import mockup.Mockup;
import model.counter.BusReleaseCounter;
import view.BusEvent;
import view.SimulatorEvent;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import static main.SimulatorConstants.N;

/**
 * Created by ppeczek on 2014-05-21.
 */
public class Model {

    private ArrayList<Bus> busContainer = new ArrayList<Bus>();
    private Schedule schedule = Schedule.getInstance();
    private BusReleaseCounter busReleaseCounter;

    public Model(LinkedBlockingQueue<SimulatorEvent> blockingQueue) {
        BusDepot busDepot = BusDepot.getInstance();
        busReleaseCounter = new BusReleaseCounter(blockingQueue, SimulatorConstants.defaultBusReleaseCooldown);
        for (int i=0; i<N; ++i) {
            Bus bus = new Bus(busDepot, blockingQueue);
            busContainer.add(bus);
            BusDepot.getInstance().getBusQueue().add(bus);
        }
        schedule.initBusStopPassengersCounters(blockingQueue);
    }

    public Mockup createMockup() {
        return new Mockup(getBusContainer(), schedule.getBusStops());
    }

    public ArrayList<Bus> getBusContainer() {
        return busContainer;
    }

    public void step() {
        for (Bus bus : busContainer) {
            bus.move();
        }
        for (BusStop busStop : new ArrayList<BusStop>(schedule.getPassengersStops())) {
            busStop.getPassengerCounter().countdown();
        }
        generatePassengers();
        busReleaseCounter.countdown();
//        System.out.println("Zajętość przystanków:");
//        for (BusStop busStop : schedule.getPassengersStops()) {
//            System.out.println(busStop.getNAME() + ": " + busStop.getPassengerQueue().size());
//        }
    }

    /**
     * <b>generatePassengers</b>
     * Adds new <b>Passengers</b> to <b>BusStops</b>.
     */
    public final void generatePassengers() {
//        double numberOfPassengersToGenerate = (random() * intensity);
//        ArrayList<BusStop> passengersStops = new ArrayList<BusStop>(schedule.getPassengersStops().values());
//        for (double i = 0; i < numberOfPassengersToGenerate; i++) {
//            Random rand = new Random();
//            int index;
//            index = rand.nextInt(passengersStops.size());
//            BusStop location = passengersStops.get(index);
//
//            index = (rand.nextInt(passengersStops.size() - 1) + index)%passengersStops.size();
//            BusStop destination = passengersStops.get(index);
//            location.queuePush(new Passenger(destination, System.currentTimeMillis()));
//        }
    }
}
