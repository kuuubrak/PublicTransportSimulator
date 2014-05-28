package view;

import model.Passenger;

import java.awt.*;
import java.math.BigDecimal;
import static java.lang.Math.abs;
import java.util.UUID;


public class PassengerView {

    private static final int textCloudWdiht = 100;
    private static final int textCloudHeigth = 70;
    private int x;
    private int y;
    private int passengerSize;
    private TextCloud textCloud;
    private Passenger passenger;

    public PassengerView(Passenger passenger, int passengerSize) {
        this.passenger = passenger;
        this.passengerSize = passengerSize;
        textCloud = createCloud();
    }

    private Color convertIdToColor(UUID id) {
        return new Color(id.hashCode() & 255 , abs(~id.hashCode())  & 255, (id.hashCode()>>6)  & 256);
    }

    public void paint(Graphics2D g2) {
        g2.setColor(convertIdToColor(passenger.getID()));
        g2.fillOval(x, y, passengerSize, passengerSize);
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawOval(x, y, passengerSize, passengerSize);
        g2.setColor(Color.RED);
        if (passenger.getTIMESTAMP() > 200) g2.drawString("!", x + passengerSize, y + passengerSize / 4);
        //textCloud.paint(g2);

    }

    public void paintCloud(Graphics2D g2) {
        textCloud.paint(g2);
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
        textCloud = createCloud();
    }

    private TextCloud createCloud() {
        return new TextCloud(x - textCloudWdiht / 2 + passengerSize / 2, y - (int) (1.2 * textCloudHeigth), textCloudWdiht, textCloudHeigth) {
            @Override
            public void paint(Graphics2D g2) {
                super.paint(g2);
                g2.setColor(Color.DARK_GRAY);
                g2.setFont(new Font("Arial", Font.PLAIN, 10));
                g2.drawString("ID:", getX() + 3, getY() + 14);
                g2.drawString("Destination:", getX() + 3, getY() + 28);
                g2.drawString("Waiting:", getX() + 3, getY() + 56);
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                g2.drawString(passenger.getID().toString(), getX() + 20, getY() + 14);
                g2.drawString(passenger.getDestination().toString(), getX() + 3, getY() + 42);
                g2.drawString(BigDecimal.valueOf(passenger.getTIMESTAMP()).toString(), getX() + 45, getY() + 56);
            }
        };
    }

}
