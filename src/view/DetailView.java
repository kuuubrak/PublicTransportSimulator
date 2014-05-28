package view;

import java.awt.*;

public interface DetailView {

    public void drawDetailView(Graphics g);
    public void setDetailViewSize(int width, int height);
    public void setDetailViewPosition(int x, int y);

}
