package order;

/**
 * Implementacja częściowa.
 * @author Maciej Majewski
 * @param <T> interfejs funkcjonalności
 */
public abstract class ServerOrderAbstract<T> implements ServerOrder<T>{
    
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
