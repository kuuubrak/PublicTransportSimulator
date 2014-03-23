package Order.zkm;

import Order.FunctionalitySimulationModule;
import Order.OrderPrioritableAbstract;

/**
 * Implementacja rozkazu wydawanego przez zkm.
 * Wypuszcza krakena. Albo bus, whatever.
 * @author Maciej Majewski
 */
public class OrderReleaseBus extends OrderPrioritableAbstract<FunctionalitySimulationModule>{

    @Override
    public void execute(FunctionalitySimulationModule subject) {
        subject.releaseBus();
    }
    
}
