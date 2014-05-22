package order.gui;

import order.FunctionalitySimulationModule;
import order.OrderPrioritableAbstract;

/**
 * Implementacja rozkazu używanego przez GUI.
 *
 * @author Maciej Majewski
 */
public class OrderRunContinuously extends OrderPrioritableAbstract<FunctionalitySimulationModule> {

    private boolean contin;

    /**
     * @param shouldRun true, jeśli ma działać ciągle (nie krokowo)
     */
    public OrderRunContinuously(boolean shouldRun) {
        contin = shouldRun;
    }

    @Override
    public void execute(FunctionalitySimulationModule subject) {
        subject.stepSimulation(!contin);
    }

}
