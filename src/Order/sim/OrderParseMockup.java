package Order.sim;

import DataModel.Mockup;
import Order.FunctionalityMockupParser;
import Order.OrderPrioritableAbstract;

/**
 * Polecenie przetworzenia makiety.
 * @author Maciej Majewski
 */
public class OrderParseMockup extends OrderPrioritableAbstract<FunctionalityMockupParser>{
    private final Mockup makieta;

    public OrderParseMockup(Mockup makieta) {
        this.makieta = makieta;
    }

    @Override
    public void execute(FunctionalityMockupParser subject) {
        subject.newMockup(makieta);
    }

    @Override
    public int priority() {
        throw new UnsupportedOperationException("Not supported yet."); //Danielu, wstaw istotność do returna i usuń tę linijkę.
        //return 666;
    }
    
    
}
