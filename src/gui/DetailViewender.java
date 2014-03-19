package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class DetailViewender extends Canvas {

	private static final long serialVersionUID = -7824633740035921796L;
	BusStopView busStopView = new BusStopView();

	
	@Override
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		busStopView.paintDetiles(g2, getWidth(), getHeight());
	}
}
