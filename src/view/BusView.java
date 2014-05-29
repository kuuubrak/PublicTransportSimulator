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
        g.drawString("Bus: "+(new Integer(bus.getID()).toString()),10, 10);
        super.drawDetailView(g);
    }

    @Override
    public void drawMiniView(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(getMiniViewXPosition(),getMiniViewYPosition(),getMiniViewWidth(), getMiniViewHeight());
        g.setColor(Color.WHITE);
        g.drawString("Bus: " + bus.getID() +
                     " progress: "+ bus.getLengthPassed() +
                     " to " + bus.getCurrentBusStop(), getMiniViewXPosition() + getMiniViewWidth(),
                                            getMiniViewYPosition() + getMiniViewHeight() );
    }


    public void updateView(MockupBus bus) {
        this.bus = bus;
        setPassengers(bus.getPassengerList());
    }

    public boolean ifViewOf(MockupBus bus) {
        return (this.bus.getID() == bus.getID());
    }
}
