package order.zkm;

import order.FunctionalitySimulationModule;
import order.OrderPrioritableAbstract;

/**
 * Implementacja rozkazu wydawanego przez zkm.
 * Wypuszcza krakena. Albo bus, whatever.
 *
 * @author Maciej Majewski
 */
public class OrderReleaseBus extends OrderPrioritableAbstract<FunctionalitySimulationModule> {

    @Override
    public void execute(FunctionalitySimulationModule subject) {
        subject.releaseBus();
    }

}
