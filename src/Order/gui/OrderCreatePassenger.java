package order.gui;

import order.FunctionalitySimulationModule;
import order.OrderPrioritableAbstract;

/**
 * Implementacja rozkazu używanego przez GUI.
 *
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

}
