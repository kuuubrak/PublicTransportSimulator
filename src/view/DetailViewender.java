package view;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DetailViewender extends Canvas {

    private static final long serialVersionUID = -7824633740035921796L;
    DetailView detalilView;

    public DetailViewender(int x, int y, int width, int height) {
        setBounds(x, y, width, height);

        addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                repaint();

            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
    }

    public void setDetaliView(DetailView detalilView){
        this.detalilView = detalilView;
        //addMouseListener(detalilView);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(2,133,4));
        g2.fillRect(getX(), getY(), getWidth(), getHeight());

        if(detalilView != null) detalilView.drawDetailView(g2);
    }


}
