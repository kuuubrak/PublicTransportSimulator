
package Order.gui;

import Order.FunctionalitySimulationModule;
import Order.OrderPrioritableAbstract;

/**
 * Implementacja rozkazu używanego przez GUI.
 * @author Maciej Majewski
 */
public class OrderCreatePassenger extends OrderPrioritableAbstract<FunctionalitySimulationModule> {
    
    private final String from;
    private final String to;

    public OrderCreatePassenger(String from, String to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute(FunctionalitySimulationModule subject) {
        subject.newPassenger(from, to);
    }

    @Override
    public int priority() {
        throw new UnsupportedOperationException("Not supported yet."); //Danielu, wstaw istotność do returna i usuń tę linijkę.
        //return 666;
    }
    
}
