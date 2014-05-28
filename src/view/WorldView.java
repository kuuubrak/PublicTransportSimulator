package view;

import java.awt.*;

/**
 * Created by mateusz on 28.05.14.
 */
public class WorldView extends Canvas {

    private ViewModel viewModel;

    public WorldView(ViewModel viewModel){
        super();
        this.viewModel = viewModel;
    }

    public void paint(Graphics g){
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLUE);
        for(BusView busView: viewModel.getBusViewList()){
            busView.drawMiniView(g);
        }
    }

}
