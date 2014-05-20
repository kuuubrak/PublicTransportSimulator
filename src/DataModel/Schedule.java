package DataModel;

import Simulator.SimulatorConstants;

import java.util.ArrayList;

/**
 * Created by ppeczek on 2014-05-21.
 */
public class Schedule {
    private static Schedule ourInstance = new Schedule();
    private static ArrayList<BusStopBase> busStops;
//    private static final BusDepot busDepot;

    public static Schedule getInstance() {
        return ourInstance;
    }

    public Schedule() {
        busStops = new ArrayList<BusStopBase>();
        BusTerminus busHome = new BusTerminus(SimulatorConstants.busHomeName);
        BusStopBase stop1 = new BusStopBase(SimulatorConstants.thirdBusStopName);
        BusStopBase stop2 = new BusStopBase(SimulatorConstants.thirdBusStopName);
        BusStopBase stop3 = new BusStopBase(SimulatorConstants.thirdBusStopName);
        busHome.setRoute(stop1, SimulatorConstants.firstBusStopDistance);
        stop1.setRoute(stop2, SimulatorConstants.secondBusStopDistance);
        stop2.setRoute(stop3, SimulatorConstants.thirdBusStopDistance);
        stop3.setRoute(busHome, SimulatorConstants.busHomeDistance);
        busStops.add( busHome );
        busStops.add( stop1 );
        busStops.add( stop2 );
        busStops.add( stop3 );

    }

    public BusTerminus getTerminus() {
        for (BusStopBase busStop : busStops) {
            if (busStop instanceof BusTerminus) {
                return (BusTerminus)busStop;
            }
        }
        return null;
    }

//    public static BusDepot getBusDepot() {
//        return busDepot;
//    }

    public static ArrayList<BusStopBase> getBusStops() {
        return busStops;
    }
}
