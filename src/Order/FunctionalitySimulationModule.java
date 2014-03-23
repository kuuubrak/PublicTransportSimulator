
package Order;

/**
 * Funkcjonalność oczekiwana od modułu symulujączego rzeczywistość.
 * @author Maciej Majewski
 */
public interface FunctionalitySimulationModule {
    
    /**
     * Polecenie zatrymania/wznowienia symulacji
     * @param patataj false jeśli symulacja ma być wstrzymana
     */
    public void runSimulation(boolean patataj);
    
    /**
     * Zmiana trybu symulacji.
     * @param goSlower true, jeśli należy postępować krokowo
     */
    public void stepSimulation(boolean goSlower);
    
    /**
     * Ustalenie czasów generacji podróżnych
     * @param minGen najmniejsza liczba kroków przed ponowną generacją podróżnego
     * @param maxGen największa ilość kroków od ostatniej generacji podróżnego
     */
    public void passengerGenerationConfig(int minGen, int maxGen);
    
    /**
     * Polecenie umieszczenia nowego podróżnego w symulacji.
     * @param busStopStart Nazwa przystanku startowego
     * @param busStopStop Nazwa przystanku docelowego
     */
    public void newPassenger(String busStopStart, String busStopStop);
}
