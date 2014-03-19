package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import sun.net.www.MimeEntry;

public class BusStopView implements DetailView, MouseListener {
	
	private Image image;
	PassengerContainer passengerContainer;
	
	public BusStopView(){
		try {
			image = ImageIO.read(new File("res/bus_stop.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		passengerContainer = new PassengerContainer(200, 100, 10, 4);
		PassengerView passenger = new PassengerView();
		PassengerView passenger2 = new PassengerView();
		PassengerView passenger3 = new PassengerView();
		passengerContainer.addPassengerView(passenger);
		passengerContainer.addPassengerView(passenger2);
		passengerContainer.addPassengerView(passenger3);
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
		
		
		passengerContainer.paint(g2);
		

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

}
