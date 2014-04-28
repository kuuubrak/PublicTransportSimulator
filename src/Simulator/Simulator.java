package Simulator;

import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.*;

import DataModel.Bus;
import DataModel.BusStop;
import DataModel.Mockup;
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
    private int simulationWait = 1000; // czas oczekiwania pomiędzy kolejnymi krokami symulacji
    private double passengerGenerationIntensity = 0.5;
    private Client networkClient = new Client();

    public static void main( String[] args )
    {
        Simulator simulator = new Simulator();
        simulator.mainLoop();
    }
    
    /**
     * <b>mainLoop</b><br>
     * Simulations' main loop.<br>
     */
    private final void mainLoop()
    {
        networkClient.establishConnection(.......);
        ArrayList<Bus> busContainer = new ArrayList<Bus>();
        ArrayList<BusStop> schedule = generateBusStopSchedule();
        int time = 0;
        
        while(true /* ??? */)
        {
            // generating new random Passengers
            generatePassengers(schedule, passengerGenerationIntensity, time);

            // sending current Mockup
            sendMock(busContainer, schedule);

            //  executing received Orders
            ArrayList<Order<FunctionalitySimulationModule>> orders = receiveOrders();
            for(Order order : orders)
            {
                order.execute(this);
            }

            // BusList.action()

            // simulateStep()
            for(Bus bus : busContainer)
            {

            }

            // wait
            try {
                Thread.sleep(simulationWait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ++time;
        }

        networkClient.closeConnection();
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
    private final ArrayList<Order<FunctionalitySimulationModule>> receiveOrders()
    {
        // TODO receiving by server
        return new ArrayList<Order<FunctionalitySimulationModule>>();
    }
    
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
        int noOfPassengersToGenerate = (int) (random() * intensity);
        PassengerModule passengerModule = new PassengerModule();
        for (int i=0; i < noOfPassengersToGenerate; i++)
        {
            passengerModule.setPassenger(
                    schedule.get((int)(random() * schedule.size())),
                    schedule.get((int)(random() * schedule.size())),
                    time
            );
        }
    }

    /**
     * <b>generateBusStopSchedule</b><br>
     * 
     * @return <b>BusStop schedule</b>
     */
    private final ArrayList<BusStop> generateBusStopSchedule()
    {
        ArrayList<BusStop> schedule = new ArrayList<BusStop>();
        BusStop petla = new BusStop( "petla", 3 );
        schedule.add( petla );
        schedule.add( new BusStop( "Pierwszy", 5 ) );
        schedule.add( new BusStop( "Drugi", 3 ) );
        schedule.add( new BusStop( "Trzeci", 2 ) );
        schedule.add( petla );
        
        return schedule;
    }
}
