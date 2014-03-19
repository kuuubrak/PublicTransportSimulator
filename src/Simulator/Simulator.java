package Simulator;

import java.util.ArrayList;

import DataModel.Bus;
import DataModel.BusStop;

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
    private static int time = 0;

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
        
        // TODO
        {
            // receiveMock() pseudokod
            // BusList.action() || simulateStep() pseudokod
            sendMock();
            // wait pseudokod
            ++time;
        }
    }
    
    /**
     * <b>sendMock</b><br>
     * Sends the simulations' state to other <b>Modules</b>.<br>
     */
    private final void sendMock()
    {
        generateMock(); // TODO
    }
    
    /**
     * <b>generateMock</b><br>
     * Generates <b>Mockup</b> based on current simulations' state.<br>
     */
    private final void generateMock()
    {
        //return new Mock(); // TODO
    }
    
    /**
     * <b>getTime</b>
     * 
     * @return number of steps that already passed.
     */
    public static final int getTime()
    {
        return time;
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
