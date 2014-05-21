package model;

import simulator.SimulatorConstants;

import java.util.ArrayList;

/**
 * Created by ppeczek on 2014-05-21.
 */
public class Schedule {
    private static Schedule ourInstance = new Schedule();
    private static ArrayList<BusStop> busStops;

    public static Schedule getInstance() {
        return ourInstance;
    }

    private Schedule() {
        busStops = new ArrayList<BusStop>();
        BusDepot busDepot = BusDepot.getInstance();
        BusTerminus busTerminus = BusTerminus.getInstance();
        BusStop stop1 = new BusStop(SimulatorConstants.firstBusStopName);
        BusStop stop2 = new BusStop(SimulatorConstants.secondBusStopName);
        BusStop stop3 = new BusStop(SimulatorConstants.thirdBusStopName);
        busDepot.setRoute(SimulatorConstants.depotTerminusDistance);
        busTerminus.setToDepot(SimulatorConstants.depotTerminusDistance);
        busTerminus.setRoute(stop1, SimulatorConstants.firstBusStopDistance);
        stop1.setRoute(stop2, SimulatorConstants.secondBusStopDistance);
        stop2.setRoute(stop3, SimulatorConstants.thirdBusStopDistance);
        stop3.setRoute(busTerminus, SimulatorConstants.busHomeDistance);
        busStops.add(busDepot);
        busStops.add( busTerminus );
        busStops.add( stop1 );
        busStops.add( stop2 );
        busStops.add( stop3 );
    }

    public BusTerminus getTerminus() {
        return BusTerminus.getInstance();
    }

    public static BusDepot getBusDepot() {
        return BusDepot.getInstance();
    }

    public static ArrayList<BusStop> getBusStops() {
        return busStops;
    }
}
