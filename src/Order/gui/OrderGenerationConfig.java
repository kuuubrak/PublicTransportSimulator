
package Order.gui;

import Order.FunctionalitySimulationModule;
import Order.OrderPrioritableAbstract;

/**
 * Implementacja rozkazu używanego przez GUI
 * @author Maciej Majewski
 */
public class OrderGenerationConfig extends OrderPrioritableAbstract<FunctionalitySimulationModule>{
    
    private final int min;
    private final int max;

    /**
     * Konstrukctor.
     * @param min najmniejsza liczba kroków przed generacją podróżnego
     * @param max największa liczba kroków od generacji podróżnego
     */
    public OrderGenerationConfig(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void execute(FunctionalitySimulationModule subject) {
       subject.passengerGenerationConfig(min, max);
    }

    @Override
    public int priority() {
        throw new UnsupportedOperationException("Not supported yet."); //Danielu, wstaw istotność do returna i usuń tę linijkę.
        //return 666;
    }
    
    
}
