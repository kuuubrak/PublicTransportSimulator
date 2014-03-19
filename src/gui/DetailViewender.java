package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DetailViewender extends Canvas {

	private static final long serialVersionUID = -7824633740035921796L;
	BusStopView busStopView;
	
	public DetailViewender(){
		busStopView = new BusStopView();
		addMouseListener(busStopView);
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				repaint();
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	
	@Override
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		busStopView.paintDetiles(g2, getWidth(), getHeight());
	}
	
	
}
