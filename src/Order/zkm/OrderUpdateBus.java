package order.zkm;

import order.FunctionalitySimulationModule;
import order.OrderPrioritableAbstract;

/**
 * Implementacja rozkazu wydawanego przez zkm.
 * Odświeża krakena dezodorantem. Bo tak.
 *
 * @author Maciej Majewski
 */
public class OrderUpdateBus extends OrderPrioritableAbstract<FunctionalitySimulationModule> {

    @Override
    public void execute(FunctionalitySimulationModule subject) {
        subject.updateBus();
    }

}
