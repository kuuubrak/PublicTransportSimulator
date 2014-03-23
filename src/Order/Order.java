package Order;

/** Szablon interfejsu dla rozkazów.
 *  który zawiera funkcjonalność do udostępnienia.
 *  Implementacja powinna być serializowalna.
 *  Małe przypomnienie: w Javie szablony działają inaczej niż w C++
 *  Rozszerza Comparable po to, by Pan Daniel mógł sobie szeregować rozkazy
 * (wystarczy je będzie wrzucić w PriorityQueue)
 * @author Maciej Majewski
 * @param <T> powinien być konkretyzowany interfejsem opisującym 
 *            udostępnianą przez przyjmującego_rozkaz funkcjonalność.
 */
public interface Order<T> extends Comparable<Order<T>>{
    
    /**
     * Wykonuje dany rozkaz. Implementacja to zwykle
     * `subject.functionality()`
     * @param subject 
     */
    public void execute(T subject);
    
    /**
     * Powinien zwrócić priorytet danego rozkazu.
     * Istotne z punktu widzenia symulacji.
     * @return 
     */
    public int priority();
}
