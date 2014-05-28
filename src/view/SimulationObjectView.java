package view;

import model.Passenger;

import java.awt.*;
import java.util.Collection;

/**
 * Created by mateusz on 28.05.14.
 */
public abstract class SimulationObjectView implements DoubleView{

    private static final int CELL_RESOLUTION = 28;
    private static final int PASSENGER_ROW_NUM = 6;
    private static final int PASSENGER_COLUMN_NUM = 10;
    private static final int PASSENGER_RESOLUTION = 24;

    private int miniViewXPosition = 0;
    private int miniViewYPosition = 0;
    private int miniViewWidth = 0;
    private int miniViewHeight = 0;
    private int miniViewDirect = 0;
    private int detailViewXPosition = 0;
    private int detailViewYPosition = 0;
    private int detailViewWidth = 0;
    private int detailViewHeight = 0;
    private PassengerContainer passengerContainer;

    protected SimulationObjectView() {

        passengerContainer = new PassengerContainer(0,0,PASSENGER_COLUMN_NUM, PASSENGER_ROW_NUM,CELL_RESOLUTION);
    }

    public void setPassengers(Collection<Passenger> passengersList){
        for(Passenger passenger: passengersList){
            passengerContainer.addPassengerView(new PassengerView(passenger, PASSENGER_RESOLUTION));
        }

    }

    public int getMiniViewXPosition() {
        return miniViewXPosition;
    }



    public int getMiniViewYPosition() {
        return miniViewYPosition;
    }



    public int getMiniViewWidth() {
        return miniViewWidth;
    }



    public int getMiniViewHeight() {
        return miniViewHeight;
    }


    public int getDetailViewXPosition() {
        return detailViewXPosition;
    }


    public int getMiniViewDirect() {
        return miniViewDirect;
    }



    public int getDetailViewYPosition() {
        return detailViewYPosition;
    }


    public int getDetailViewWidth() {
        return detailViewWidth;
    }


    public int getDetailViewHeight() {
        return detailViewHeight;
    }

    @Override
    public void drawDetailView(Graphics g) {
        passengerContainer.draw(g);
    }

    @Override
    public void setDetailViewSize(int width, int height) {
        this.detailViewWidth = width;
        this.detailViewHeight = height;
    }

    @Override
    public void setDetailViewPosition(int x, int y) {
        this.detailViewXPosition = x;
        this.detailViewYPosition = y;
    }

    @Override
    public void drawMiniView(Graphics g) {

    }

    @Override
    public void setMiniViewPosition(int x, int y) {
        this.miniViewXPosition = x;
        this.miniViewYPosition = y;
    }

    @Override
    public void setMiniViewSize(int width, int height) {
        this.miniViewWidth = width;
        this.miniViewHeight = height;
    }

    @Override
    public void rotateMiniView(int direct) {
        this.miniViewDirect = direct;
    }
}
