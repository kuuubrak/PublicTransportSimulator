package view;

import mockup.MockupBus;

import java.awt.*;

/**
 * Created by mateusz on 28.05.14.
 */
public class BusView extends SimulationObjectView {


    private MockupBus bus;

    public BusView(MockupBus bus){
        super();
        this.bus = bus;
        setPassengers(bus.getPassengerList());
    }

    @Override
    public void drawDetailView(Graphics g){
        g.setColor(Color.WHITE);
        g.drawString((new Integer(bus.getID()).toString()) ,10, 10);
        super.drawDetailView(g);
    }
}
