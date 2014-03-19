package gui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class PassengerContainer{
	private List<PassengerView> passengerViewList;
	private int rowsNumber;
	private int columnsNumber;
	private int x;
	private int y;
	private String jajco = "Jajco";
	
	public PassengerContainer(int x, int y, int columnsNumber, int rowsNumber){
		passengerViewList = new ArrayList<PassengerView>();
		this.x = x;
		this.y = y;
		this.rowsNumber = rowsNumber;
		this.columnsNumber = columnsNumber;
	}
	
	public void addPassengerView(PassengerView passengerView){
		passengerViewList.add(passengerView);
		passengerView.setXY(x - 28*(passengerViewList.size()%columnsNumber), y);
	}
	
	public void paint(Graphics2D g2){
		for(PassengerView passenger: passengerViewList){
			passenger.paint(g2);
		}
	}

	public void onMouseClick(int x, int y) {
		for(PassengerView passenger: passengerViewList){
			passenger.setColudVisable(false);
		}
		int place = (this.x - x)/28;
		if(place > -1 && place < passengerViewList.size()){
			passengerViewList.get(place).setColudVisable(true);
		}
		
	}

	
	
}
