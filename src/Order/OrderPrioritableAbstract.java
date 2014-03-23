
package Order;

/**
 * Częściowa implementacja Rozkazu. Część dot. priorytetów.
 * @author Maciej Majewski
 */
public abstract class OrderPrioritableAbstract<T> implements Order<T>{

    @Override
    public int compareTo(Order<T> o) {
        return Integer.compare(priority(), o.priority());
    }
    
}
