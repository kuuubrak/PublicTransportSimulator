package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class PassengerView{

	private int x = 100;
	private int y = 100;
	private int id = 41323;
	private int waitingSteps = 242;
	private boolean showCloud = true;
	private TextCloud textCloud;
	
	
	public PassengerView(int x, int y){
		this.x = x;
		this.y = y;
		textCloud = createCloud();
	}
	
	
	public void paint(Graphics2D g2) {
		g2.setColor(new Color((50 + id*37)%256, (20 + id*67)%256, (100 + id*17)%256));
		g2.fillOval(x, y, 20, 20);
		g2.setColor(Color.LIGHT_GRAY);
		g2.drawOval(x, y, 20, 20);
		if(showCloud) textCloud.paint(g2);
		g2.setColor(Color.RED);
		if(waitingSteps > 200) g2.drawString("!", x+20, y+5);
		
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setXY(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	private TextCloud createCloud(){
		return new TextCloud(x - 40, y - 70, 100, 60){
			@Override
			public void paint(Graphics2D g2){
				super.paint(g2);
				g2.setColor(Color.DARK_GRAY);
				g2.setFont(new Font("Arial", Font.PLAIN, 10));
				g2.drawString("ID:", getX() + 3, getY() + 14);
				g2.drawString("Destination:", getX() + 3, getY() + 28);
				g2.drawString("Waiting:", getX() + 3, getY() + 56);
				g2.setColor(Color.BLACK);
				g2.setFont(new Font("Arial", Font.BOLD, 12));
				g2.drawString(Integer.toString(id), getX() + 20, getY() + 14);
				g2.drawString("Wypizdów", getX() + 3, getY() + 42);
				g2.drawString(Integer.toString(waitingSteps), getX() + 45, getY() + 56);
			}
		};
	}

}
