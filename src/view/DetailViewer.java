package view;

import java.awt.*;

public class DetailViewer extends Canvas{

    private static final long serialVersionUID = -7824633740035921796L;
    private ViewModel viewModel;
    private ViewUpdater viewUpdater;

    public DetailViewer(ViewModel viewModel, ViewUpdater viewUpdater) {
        super();
        this.viewModel = viewModel;
        this.viewUpdater = viewUpdater;
    }

    public void setViewModel(ViewModel viewModel){
        this.viewModel = viewModel;
        //addMouseListener(detalilView);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        if(viewModel != null && viewModel.getCurrentDetailView() != null) {
            viewModel.getCurrentDetailView().setDetailViewSize(getWidth(),getHeight());
            viewModel.getCurrentDetailView().drawDetailView(g2);
        }
    }


}
