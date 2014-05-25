package model;

import javafx.util.Pair;
import main.SimulatorConstants;

import java.util.ArrayList;

/**
 * Created by ppeczek on 2014-05-21.
 */
public class Schedule {
    private static Schedule ourInstance = new Schedule();
    private static ArrayList<BusStop> busStops;

    private Schedule() {
        busStops = new ArrayList<BusStop>();
        BusStop lastStop, currentStop;
        BusDepot busDepot = BusDepot.getInstance();
        BusTerminus busTerminus = BusTerminus.getInstance();
        busStops.add(busDepot);
        busStops.add(busTerminus);
        busDepot.setRoute(SimulatorConstants.depotTerminusDistance);
        lastStop = busTerminus;
        for (Pair<String, Integer> pair : SimulatorConstants.busStopSettings) {
            currentStop = new BusStop(pair.getKey());
            lastStop.setRoute(currentStop, pair.getValue());
            busStops.add(currentStop);
            lastStop = currentStop;
        }
        lastStop.setRoute(busTerminus, 3);
    }

    public static Schedule getInstance() {
        return ourInstance;
    }

    public static ArrayList<BusStop> getBusStops() {
        return busStops;
    }

    public ArrayList<BusStop> getPassengersStops() {
        ArrayList<BusStop> passengersStops = new ArrayList<BusStop>();
        for (BusStop bs : busStops) {
            if (!(bs instanceof BusDepot)) {
                passengersStops.add(bs);
            }
        }
        return passengersStops;
    }

    public BusStop findBusStop(String name) {
        for (BusStop bs : busStops) {
            if (bs.getNAME().equals(name)) {
                return bs;
            }
        }
        return null;
    }

//    public void addStop(BusStop busStop) {
//        getBusStops().put(busStop.getNAME(), busStop);
//    }
}
