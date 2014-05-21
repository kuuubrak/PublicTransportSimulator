package Simulator;

import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.*;

import DataModel.*;
import Order.sim.OrderParseMockup;
import Order.Order;
import Order.FunctionalitySimulationModule;
import network.Client;

/**
 * <b>Simulator</b><br>
 * Silnik emulacji.<br>
 * <br>
 * Zajmuje się symulowaniem kreowanego świata.<br>
 * Czyli:<br>
 *  - Zawiera w sobie główną pętlę.<br>
 *  - Zajmuje się poruszaniem się <b>Autobusów</b> po <b>Trasie</b>.<br>
 *  - Rozsyła <b>Makiety</b> do pozostałych <b>Modułów</b>.<br>
 *  - Wdraża otrzymane <b>Makiety</b> do symulacji. ( np. zmiana rozkładu jazdy <b>Autobusu</b> )<br>
 * 
 * @author dan.krasniak
 */
public final class Simulator implements FunctionalitySimulationModule
{
    private int simulationWait = SimulatorConstants.simulatorDefaultWaitTime; // czas oczekiwania pomiędzy kolejnymi krokami symulacji
    private double passengerGenerationIntensity = SimulatorConstants.simulatorDefaultGenerationIntensity;
    private Client networkClient = new Client();
    private ArrayList<Bus> busContainer;
    ArrayList<BusStop> schedule;
    private String host;
    private int port;

    public static void main( String[] args )
    {
        //Simulator simulator = new Simulator(args[0], Integer.valueOf(args[1]));
        Simulator simulator = new Simulator(SimulatorConstants.simulatorHostAddress, SimulatorConstants.simulatorPort);
        simulator.mainLoop();
    }

    /**
     * <b>Constructor</b><br>
     */
    public Simulator(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    /**
     * <b>mainLoop</b><br>
     * Simulations' main loop.<br>
     */
    private final void mainLoop()
    {
        networkClient.establishConnection(host, port);
        busContainer = new ArrayList<Bus>();
        Schedule schedule = Schedule.getInstance();
        int time = 0;

        //TODO autobusy nie są tworzone ot tak. Mają być stworzone na początku programu wg settingsów w zajezdni
        //i wyjeżdżać z niej kiedy jest potrzeba.
        Bus testBus = new Bus(schedule);
        testBus.setState(BusState.RUNNING);
        Bus testBus2 = new Bus(schedule);
        Bus testBus3 = new Bus(schedule);

        busContainer.add(testBus);
        busContainer.add(testBus2);
        busContainer.add(testBus3);

        while(true /* ??? */)
        {
            // generating new random Passengers
            generatePassengers(schedule.getBusStops(), passengerGenerationIntensity, time);
            // sending current Mockup
            sendMock(busContainer, schedule.getBusStops());
            //  executing received Orders
            while(!networkClient.getOrdersQueue().isEmpty())
            {
                try
                {
                    Order<FunctionalitySimulationModule> order = networkClient.getOrdersQueue().take();
                    order.execute(this);
                }
                catch(InterruptedException e)
                {
                    //TODO Generated
                    e.printStackTrace();
                    throw new RuntimeException();
                }
            }

            // BusList.action()

            simulationStep();

            // wait
            try {
                Thread.sleep(simulationWait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ++time;
        }

        // TODO
        //networkClient.closeConnection();
    }
    
    /**
     * <b>sendMock</b><br>
     * Sends the simulations' state to other <b>Modules</b>.<br>
     */
    private final void sendMock(final List<Bus> schedule, final List<BusStop> busStops)
    {
        Mockup mockup = new Mockup(schedule, busStops);
        networkClient.send(new OrderParseMockup(mockup));
    }

    /**
     * <b>receiveMock</b><br>
     * Receive the list of commands from GUI and ZKM <b>Modules</b>.
     */
    /*private final ArrayList<Order<FunctionalitySimulationModule>> receiveOrders()
    {
        // TODO receiving by server
        networkClient.getOrdersQueue();
        return new ArrayList<Order<FunctionalitySimulationModule>>();
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
    private final void generatePassengers(final ArrayList<BusStop> schedule, final double intensity, final int time)
    {
        int numberOfPassengersToGenerate = (int) (random() * intensity);
        PassengerModule passengerModule = new PassengerModule();
        for (int i=0; i < numberOfPassengersToGenerate; i++)
        {
            BusStop location = schedule.get((int)(random() * schedule.size()));
            BusStop destination = schedule.get((int)(random() * schedule.size()));
            passengerModule.setPassenger(location, destination, time);
        }
    }

    private void simulationStep() {
        moveBuses();
    }

    private void moveBuses() {
        for (Bus bus : busContainer) {
            bus.move();
        }
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
