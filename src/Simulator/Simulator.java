package simulator;

import controller.Controller;
import model.BusStop;
import order.FunctionalitySimulationModule;

import java.util.ArrayList;

import static java.lang.Math.random;

/**
 * <b>simulator</b><br>
 */
public final class Simulator implements FunctionalitySimulationModule {
    private static Simulator ourInstance = new Simulator();

    private int simulationWait = SimulatorConstants.simulatorDefaultWaitTime; // czas oczekiwania pomiędzy kolejnymi krokami symulacji
    private double passengerGenerationIntensity = SimulatorConstants.simulatorDefaultGenerationIntensity;

    public static Simulator getInstance() {
        return ourInstance;
    }

    public static void main(final String[] args) {
        final Controller controller = Controller.getInstance();
        controller.setNetData(SimulatorConstants.simulatorHostAddress, SimulatorConstants.simulatorPort);
        controller.work();
    }


    /**
     * <b>receiveMock</b><br>
     * Receive the list of commands from GUI and ZKM <b>Modules</b>.
     */
    /*private final ArrayList<order<FunctionalitySimulationModule>> receiveOrders()
    {
        // TODO receiving by server
        networkClient.getOrdersQueue();
        return new ArrayList<order<FunctionalitySimulationModule>>();
    }*/

    /**
     * <b>getTime</b>
     *
     * @return number of steps that already passed.
     */
    /*public final int getTime()
    {
        return time;
    }*/

    /**
     * <b>generatePassengers</b>
     * Adds new <b>Passengers</b> to the <b>BusStops</b>.
     */
    public final void generatePassengers(final ArrayList<BusStop> schedule, final double intensity, final int time) {
        int numberOfPassengersToGenerate = (int) (random() * intensity);
        PassengerModule passengerModule = new PassengerModule();
        for (int i = 0; i < numberOfPassengersToGenerate; i++) {
            BusStop location = schedule.get((int) (random() * schedule.size()));
            BusStop destination = schedule.get((int) (random() * schedule.size()));
            passengerModule.setPassenger(location, destination, time);
        }
    }

    public double getPassengerGenerationIntensity() {
        return passengerGenerationIntensity;
    }

    @Override
    public void runSimulation(boolean patataj) {

    }

    @Override
    public void stepSimulation(boolean goSlower) {

    }

    @Override
    public void passengerGenerationConfig(int minGen, int maxGen) {

    }

    @Override
    public void newPassenger(String busStopStart, String busStopStop) {

    }

    @Override
    public void releaseBus() {

    }

    @Override
    public void trapBus() {

    }

    @Override
    public void updateBus() {

    }
}
