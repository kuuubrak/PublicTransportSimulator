package DataModel;

import Simulator.SimulatorConstants;

import java.util.ArrayList;

/**
 * Created by ppeczek on 2014-05-21.
 */
public class BusDepot extends BusStop {
    private static BusDepot ourInstance = new BusDepot();
    private ArrayList<Bus> buses;
    public static BusDepot getInstance() {
        return ourInstance;
    }

    private BusDepot() {
        super(SimulatorConstants.depotName);
    }
}
