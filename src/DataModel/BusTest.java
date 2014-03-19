package DataModel;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class BusTest
{

    /**
     * Creates a <b>schedule</b> ( <b>BusStop</b> list ) and a <b>Bus</b>, based on the <b>schedule</b>.<br>
     * Checks basic functionality.
     */
    @Test
    public void create()
    {
        ArrayList<BusStop> scheduleTest = new ArrayList<BusStop>();
        scheduleTest.add( new BusStop( "Petla", 0 ) );
        Bus busTest = new Bus( scheduleTest );
        
        assertTrue( busTest.getPassengerContainer().isEmpty() );    //TODO    
    }
    
    

}
