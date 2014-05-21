package model;

import simulator.SimulatorConstants;

/**
 * Created by ppeczek on 2014-05-21.
 */
public class BusTerminus extends BusStop {
    private static BusTerminus ourInstance = new BusTerminus();
    private Route toDepot = new Route();

    public static BusTerminus getInstance() {
        return ourInstance;
    }

    private BusTerminus() {
        super(SimulatorConstants.terminusName);
        getToDepot().setToBusStop(BusDepot.getInstance());
    }

    public Route getToDepot() {
        return toDepot;
    }

    public void setToDepot(int distance) {
        toDepot.setLength(distance);
    }
}
