
package Order.gui;

import Order.FunctionalityServer;
import Order.ServerOrderAbstract;

/**
 * Polecenie wydawane przez gui
 * @author Maciej Majewski
 */
public class OrderCrippleGUI extends ServerOrderAbstract<FunctionalityServer>{

    private final boolean cripple;

    /**
     * @param cripple true jeśli przesyłanie rozkazów od zkm ma być ignorowane
     */
    public OrderCrippleGUI(boolean cripple) {
        this.cripple = cripple;
    }
    
    
    
    @Override
    public void execute(FunctionalityServer subject) {
        subject.crippleGUI(cripple);
    }
    
}