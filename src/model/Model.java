package model;

import event.BusStartSignal;
import mockup.Mockup;
import simulator.SimulatorConstants;
import view.BusEvent;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Math.random;
import static simulator.SimulatorConstants.N;

/**
 * Created by ppeczek on 2014-05-21.
 */
public class Model {

    private ArrayList<Bus> busContainer = new ArrayList<Bus>();
    private Schedule schedule = Schedule.getInstance();

    private double passengerGenerationIntensity = SimulatorConstants.simulatorDefaultGenerationIntensity;

    public Model(LinkedBlockingQueue<BusEvent> blockingQueue) {
        BusDepot busDepot = BusDepot.getInstance();
        for (int i=0; i<N; ++i) {
            busContainer.add(new Bus(busDepot, blockingQueue));
        }
        try {
            blockingQueue.put(new BusStartSignal(getBusContainer().get(0)));
//            blockingQueue.put(new BusStartSignal(getBusContainer().get(1)));
        } catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        generatePassengers(getPassengerGenerationIntensity());
//        System.out.println("Zajętość przystanków:");
//        for (BusStop busStop : schedule.getPassengersStops()) {
//            System.out.println(busStop.getNAME() + ": " + busStop.getPassengerQueue().size());
//        }
    }

    /**
     * <b>generatePassengers</b>
     * Adds new <b>Passengers</b> to <b>BusStops</b>.
     */
    public final void generatePassengers(final double intensity) {
        double numberOfPassengersToGenerate = (double) (random() * intensity);
        ArrayList<BusStop> passengersStops = schedule.getPassengersStops();
        for (double i = 0; i < numberOfPassengersToGenerate; i++) {
            Random rand = new Random();
            int index;
            index = rand.nextInt(passengersStops.size());
            BusStop location = passengersStops.get(index);

            index = (rand.nextInt(passengersStops.size() - 1) + index)%passengersStops.size();
            BusStop destination = passengersStops.get(index);
            location.queuePush(new Passenger(destination, System.currentTimeMillis()));
        }
    }

    public double getPassengerGenerationIntensity() {
        return passengerGenerationIntensity;
    }


}
