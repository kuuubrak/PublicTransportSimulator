package order;

/**
 * Implementacja częściowa.
 *
 * @param <T> interfejs funkcjonalności
 * @author Maciej Majewski
 */
public abstract class ServerOrderAbstract<T> implements ServerOrder<T> {

    // w sumie to niepotrzebne tu te funkcje...

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public int compareTo(Order<T> o) {
        return 0;
    }

}
