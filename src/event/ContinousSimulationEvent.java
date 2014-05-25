package event;

import view.SimulatorEvent;

/**
 * Created by ppeczek on 2014-05-25.
 */
public class ContinousSimulationEvent extends SimulatorEvent {
    private final boolean continuous;

    public ContinousSimulationEvent(boolean continuous) {
        this.continuous = continuous;
    }

    public boolean isContinuous() {
        return continuous;
    }
}
