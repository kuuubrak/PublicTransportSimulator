package order.zkm;

import order.FunctionalitySimulationModule;
import order.OrderPrioritableAbstract;

/**
 * Implementacja rozkazu wydawanego przez zkm.
 * Zastawia sidła na krakena. Albo bus, whatever.
 *
 * @author Maciej Majewski
 */
public class OrderTrapBus extends OrderPrioritableAbstract<FunctionalitySimulationModule> {

    @Override
    public void execute(FunctionalitySimulationModule subject) {
        subject.trapBus();
    }

}
