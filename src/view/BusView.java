package view;

import model.Bus;

import java.awt.*;

/**
 * Created by mateusz on 28.05.14.
 */
public class BusView extends SimulationObjectView {


    private Bus bus;

    public BusView(Bus bus){
        super();
        this.bus = bus;
        setPassengers(bus.getPassengerMap().values());
    }

    @Override
    public void drawDetailView(Graphics2D g2){
        g2.setColor(Color.WHITE);
        g2.drawString((new Integer(bus.getID()).toString()) ,10, 10);
        super.drawDetailView(g2);
    }
}
