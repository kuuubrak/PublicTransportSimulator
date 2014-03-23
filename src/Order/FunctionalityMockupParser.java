
package Order;

import DataModel.Mockup;

/**
 * Zbiór funkconalności oczekiwanych od obiektów mających
 * 'rozumieć' makiety Pana Daniela
 * @author Maciej Majewski
 */
public interface FunctionalityMockupParser extends FunctionalityCommon{
    
    /**
     * Pozostaje przetworzyć otrzymaną makietę.
     * @param fresh makieta którą dostajesz
     */
    public void newMockup(Mockup fresh);
}
