package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BusStopView implements DetailView {
	
	private Image image;
	
	public BusStopView(){
		try {
			image = ImageIO.read(new File("res/bus_stop.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void paintDetiles(Graphics2D g2, int width, int height) {
		g2.setColor(new Color(80, 80, 80));
		g2.fillRect(0,  (int)(0.6*height), width, (int)(0.4*height));
		g2.setColor(new Color(120, 120, 130));
		g2.fillRect(0,  (int)(0.2*height), width, (int)(0.4*height));
		g2.setColor(new Color(50, 180, 20));
		g2.fillRect(0,  0, width, (int)(0.2*height));
		g2.setColor(new Color(30, 30, 30));
		g2.drawLine(0,  (int)(0.6*height)-1, width, (int)(0.6*height)-1);
		g2.drawLine(0,  (int)(0.6*height), width, (int)(0.6*height));
		g2.drawLine(0,  (int)(0.6*height)+1, width, (int)(0.6*height)+1);
		g2.setColor(new Color(180, 20, 10));
		g2.fillRect(width - 34, height/2 - 50, 8, 60);
		g2.drawImage(image, width - 50, height/2 - 100, 40, 52, null);
		g2.setColor(new Color(210, 200, 10));
		g2.fillRect(0, height - 8, width, 8);
		
		PassengerView passenger = new PassengerView(100, 100);
		PassengerView passenger2 = new PassengerView(150, 200);
		passenger2.setId(253);
		passenger.paint(g2);
		passenger2.paint(g2);
		

	}

}
