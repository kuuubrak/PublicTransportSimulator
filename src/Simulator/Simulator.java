package simulator;

import controller.Controller;

/**
 * <b>Simulator</b><br>
 *     Main class.
 */
public final class Simulator {

    public static void main(final String[] args) throws Exception {
        final Controller controller = Controller.getInstance();
        controller.setNetData(SimulatorConstants.simulatorHostAddress, SimulatorConstants.simulatorPort);
        controller.work();
    }

}
