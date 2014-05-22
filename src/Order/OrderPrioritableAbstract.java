package order;

import java.io.Serializable;

/**
 * Częściowa implementacja Rozkazu. Część dot. priorytetów.
 *
 * @param <T> interfejs funkcjonalny
 * @author Maciej Majewski
 */
public abstract class OrderPrioritableAbstract<T> implements Serializable,
        Order<T> {

    @Override
    public int compareTo(Order<T> o) {
        return Integer.compare(priority(), o.priority());
    }

    @Override
    public int priority() {
        return 0;
    }

}
