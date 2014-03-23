
package Order.gui;

import Order.FunctionalitySimulationModule;
import Order.OrderPrioritableAbstract;

/**
 * Implementacja rozkazu używanego przez GUI.
 * @author Maciej Majewski
 */
public class OrderRunSimulation extends OrderPrioritableAbstract<FunctionalitySimulationModule>{
    
    private boolean runSim;
    
    
    /**
     * Kontruktor.
     * @param shouldRun false, jeśli symulacja ma się zatrzymać 
     */
    public OrderRunSimulation(boolean shouldRun) {
        runSim = shouldRun;
    }

    
    @Override
    public void execute(FunctionalitySimulationModule subject) {
        subject.runSimulation(runSim);
    }
    
}
