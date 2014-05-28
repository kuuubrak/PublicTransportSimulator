package view;

import model.Bus;

/**
 * Created by mateusz on 28.05.14.
 */
public class BusView extends SimulationObjectView {


    public BusView(Bus bus){
        super();
        setPassengers(bus.getPassengerMap().values());
    }
}
