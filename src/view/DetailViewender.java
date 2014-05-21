package view;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DetailViewender extends Canvas {

	private static final long serialVersionUID = -7824633740035921796L;
	BusStopView busStopView;
	
	public DetailViewender(int x, int y, int width, int height){
		setBounds(x, y, width, height);
		busStopView = new BusStopView(width, height);
		addMouseListener(busStopView);
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
				repaint();
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
	}

	
	@Override
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		busStopView.paint(g2);
	}
	
	
}
