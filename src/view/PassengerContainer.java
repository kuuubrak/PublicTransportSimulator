package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PassengerContainer {
    private final int cellResolution;
    private ArrayList<PassengerView> passengerViewList;
    private PassengerView moreDetailes = null;
    private int columnsNumber;
    private int x;
    private int y;
    private int rowsNumber;

    public PassengerContainer(int x, int y, int columnsNumber, int rowsNumber, int cellResolution) {
        passengerViewList = new ArrayList<PassengerView>();
        this.x = x;
        this.y = y;
        this.rowsNumber = rowsNumber;
        this.columnsNumber = columnsNumber;
        this.cellResolution = cellResolution;
    }

    public void addPassengerView(PassengerView passengerView) {
        passengerView.setXY(x - cellResolution * (passengerViewList.size() % columnsNumber), y - cellResolution * (passengerViewList.size() / columnsNumber));
        passengerViewList.add(passengerView);
    }

    public void draw(Graphics2D g2) {
        PassengerView passenger;
        for (int i = 0; i < passengerViewList.size(); i++) {
            passenger = passengerViewList.get(i);
            passenger.setXY(i%columnsNumber*cellResolution,100 + i/columnsNumber*cellResolution);
            passenger.paint(g2);
        }
        if (moreDetailes != null) moreDetailes.paintCloud(g2);
    }

    public void onMouseClick(int x, int y) {

        int place = (this.x - x + cellResolution) / cellResolution + ((this.y - y + cellResolution) / cellResolution) * columnsNumber;
        if (x < this.x + cellResolution && x > this.x - cellResolution * columnsNumber && y < this.y + cellResolution && place < passengerViewList.size()) {
            moreDetailes = passengerViewList.get(place);
        } else {
            moreDetailes = null;
        }

    }


}
