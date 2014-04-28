package Simulator;

import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.*;

import DataModel.Bus;
import DataModel.BusStop;
import DataModel.Mockup;

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
public final class Simulator
{
    private int time = 0;
    private int simulationWait = 1000; // czas oczekiwania pomiędzy kolejnymi krokami symulacji
    private double passengerGenerationIntensity = 0.5;

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
        ArrayList<Bus> busContainer = new ArrayList<Bus>();
        ArrayList<BusStop> schedule = generateBusStopSchedule();
        
        while(true /* ??? */)
        {
            sendMock(busContainer, schedule);
            receiveMock();

            generatePassengers(schedule, passengerGenerationIntensity);

            // BusList.action()

            // simulateStep() pseudokod
            for(Bus bus : busContainer)
            {

            }

            // wait pseudokod
            try {
                Thread.sleep(simulationWait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ++time;
        }
    }
    
    /**
     * <b>sendMock</b><br>
     * Sends the simulations' state to other <b>Modules</b>.<br>
     */
    private final void sendMock(final List<Bus> schedule, final List<BusStop> busStops)
    {
        Mockup mockup = new Mockup(schedule, busStops);
        // TODO sending by server
    }

    /**
     * <b>receiveMock</b><br>
     * Receive the list of commands from GUI and ZKM <b>Modules</b>.
     */
    private final void receiveMock()
    {
        // TODO receiving by server

    }
    
    /**
     * <b>getTime</b>
     * 
     * @return number of steps that already passed.
     */
    public final int getTime()
    {
        return time;
    }

    private final void generatePassengers(final ArrayList<BusStop> schedule, double intensity)
    {
        int noOfPassengersToGenerate = (int) (random() * intensity);
        PassengerModule passengerModule = new PassengerModule();
        for (int i=0; i < noOfPassengersToGenerate; i++)
        {
            passengerModule.setPassenger(
                    schedule.get((int)(random() * schedule.size())),
                    schedule.get((int)(random() * schedule.size()))
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
