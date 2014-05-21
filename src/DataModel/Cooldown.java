package DataModel;

import Simulator.SimulatorConstants;

/**
 * Created by ppeczek on 2014-05-21.
 */
public class Cooldown extends Counter {
    public final static int max_value = SimulatorConstants.cooldown;

    public Cooldown() {
        super(max_value);
    }

    @Override
    public int countdown() {
        //TODO: rzuć event
        super.countdown();
        if (isCounted()) {
            setValue(max_value);
        }
        return getValue();
    }
}
