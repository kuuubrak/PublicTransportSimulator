package DataModel;

import Simulator.SimulatorConstants;

/**
 * Created by ppeczek on 2014-05-21.
 */
public class BusTerminus extends BusStop {
    private static BusTerminus ourInstance = new BusTerminus();

    public static BusTerminus getInstance() {
        return ourInstance;
    }

    private BusTerminus() {
        super(SimulatorConstants.terminusName);
    }
}
