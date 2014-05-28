package view;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class BusStopView implements DetailView, MouseListener {

    private final static int columnNumber = 15;
    private final static int rowNumber = 4;
    PassengerContainer passengerContainer;
    private BusStopBackground busStopBackground;
    private BusStopSign busStopSign;

    public BusStopView(int width, int height) {

        busStopBackground = new BusStopBackground(width, height);
        busStopSign = new BusStopSign((int) (0.9 * width), (int) (0.25 * height), 100, (int) (0.3 * height));
        passengerContainer = new PassengerContainer((int) (0.80 * width), (int) (0.49 * height), columnNumber, rowNumber, (int) (0.05 * width));
        
    }

    //@Override
    public void paint(Graphics2D g2) {
        busStopBackground.paint(g2);
        busStopSign.paint(g2);
        passengerContainer.draw(g2);


    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {

        passengerContainer.onMouseClick(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void drawDetailView(Graphics2D g2) {

    }

    @Override
    public void setDetailViewSize(int width, int height) {

    }

    @Override
    public void setDetailViewPosition(int x, int y) {

    }

    private class BusStopSign {

        private final static int imageWidth = 40;
        private final static int imageHeight = 50;
        private final static int pistilWidth = 8;
        private final Color pistilColor = new Color(180, 20, 10);
        private int width;
        private int height;
        private int x;
        private int y;
        private Image image;

        public BusStopSign(int x, int y, int width, int height) {
            try {
                image = ImageIO.read(new File("res/bus_stop.png"));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public void paint(Graphics2D g2) {
            g2.drawImage(image, x, y, imageWidth, imageHeight, null);
            g2.setColor(pistilColor);
            g2.fillRect(x + imageWidth / 2 - pistilWidth / 2, y + imageHeight, pistilWidth, height - imageHeight);
        }

    }

    private class BusStopBackground {

        private final Color roadColor = new Color(80, 80, 80);
        private final Color sidewalkColor = new Color(120, 120, 130);
        private final Color grassColor = new Color(50, 180, 20);
        private final Color curbColor = new Color(30, 30, 30);
        private final Color roadLineColor = new Color(210, 200, 10);
        private int width;
        private int height;

        public BusStopBackground(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public void paint(Graphics2D g2) {
            g2.setColor(roadColor);
            g2.fillRect(0, (int) (0.6 * height), width, (int) (0.4 * height));
            g2.setColor(sidewalkColor);
            g2.fillRect(0, (int) (0.2 * height), width, (int) (0.4 * height));
            g2.setColor(grassColor);
            g2.fillRect(0, 0, width, (int) (0.2 * height));
            g2.setColor(curbColor);
            g2.drawLine(0, (int) (0.6 * height) - 1, width, (int) (0.6 * height) - 1);
            g2.drawLine(0, (int) (0.6 * height), width, (int) (0.6 * height));
            g2.drawLine(0, (int) (0.6 * height) + 1, width, (int) (0.6 * height) + 1);
            g2.setColor(roadLineColor);
            g2.fillRect(0, height - 8, width, 8);
        }
    }

}
