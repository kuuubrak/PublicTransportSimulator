package model;

import mockup.Mockup;
import simulator.SimulatorConstants;
import view.BusEvent;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

import static java.lang.Math.random;
import static simulator.SimulatorConstants.N;

/**
 * Created by ppeczek on 2014-05-21.
 */
public class Model {

    private ArrayList<Bus> busContainer = new ArrayList<Bus>();
    private Schedule schedule = Schedule.getInstance();

    private double passengerGenerationIntensity = SimulatorConstants.simulatorDefaultGenerationIntensity;

    public Model(BlockingQueue<BusEvent> blockingQueue) {
        BusDepot busDepot = BusDepot.getInstance();
        for (int i=0; i<N; ++i) {
            busContainer.add(new Bus(busDepot, blockingQueue));
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
        System.out.println("Stan:");
        for (BusStop bs : schedule.getPassengersStops()) {
            System.out.println(bs.getNAME() + ": " + bs.getPassengerQueue().size());
        }
        System.out.println();
    }

    /**
     * <b>generatePassengers</b>
     * Adds new <b>Passengers</b> to the <b>BusStops</b>.
     */
    public final void generatePassengers(final double intensity) {
        double numberOfPassengersToGenerate = (double) (random() * intensity);
        PassengerModule passengerModule = new PassengerModule();
        ArrayList<BusStop> passengersStops = schedule.getPassengersStops();
        for (double i = 0; i < numberOfPassengersToGenerate; i++) {
            Random rand = new Random();
            int index;
            index = rand.nextInt(passengersStops.size());
            BusStop location = passengersStops.get(index);

            index = (rand.nextInt(passengersStops.size() - 1) + index)%passengersStops.size();
            BusStop destination = passengersStops.get(index);
            passengerModule.setPassenger(location, destination, System.currentTimeMillis());
        }
    }

    public double getPassengerGenerationIntensity() {
        return passengerGenerationIntensity;
    }


}
