
package Order.gui;

import Order.FunctionalitySimulationModule;
import Order.OrderPrioritableAbstract;

/**
 * Implementacja rozkazu używanego przez GUI.
 * @author Maciej Majewski
 */
public class OrderRunContinuously extends OrderPrioritableAbstract<FunctionalitySimulationModule>{

    private boolean contin;
    
    /**
     * Konstruktor
     * @param shouldRun true, jeśli ma działać ciągle (nie krokowo) 
     */
    public OrderRunContinuously(boolean shouldRun) {
        contin = shouldRun;
    }    

    @Override
    public void execute(FunctionalitySimulationModule subject) {
        subject.stepSimulation(!contin);
    }

    @Override
    public int priority() {
        throw new UnsupportedOperationException("Not supported yet."); //Danielu, wstaw istotność do returna i usuń tę linijkę.
        //return 666;
    }
    
}
