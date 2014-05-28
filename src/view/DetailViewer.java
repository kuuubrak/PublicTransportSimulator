package view;

import java.awt.*;

public class DetailViewer extends Canvas{

    private static final long serialVersionUID = -7824633740035921796L;
    private DetailView detalilView;

    public DetailViewer(int x, int y, int width, int height) {
        super();
        setBounds(x, y, width, height);
    }

    public void setDetaliView(DetailView detalilView){
        this.detalilView = detalilView;
        //addMouseListener(detalilView);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(new Color(2, 133, 4));
        g.fillRect(0, 0, getWidth(), getHeight());

        if(detalilView != null) detalilView.drawDetailView(g2);
    }


}
