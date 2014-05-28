package view;

import model.Bus;
import model.BusStop;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by mateusz on 27.05.14.
 */
public class ViewModel {
    private List<BusStopView> busStopViewList;
    private List<BusView> busViewList = new ArrayList<BusView>();
    private DetailView currentDetailView;
    private final int BUS_WIDTH = 40;
    private final int BUS_HEIGHT = 20;

    public ViewModel(){

        currentDetailView = null;
    }

    public void udateBusStopList(List<BusStop> busStopList){
        busStopViewList = new ArrayList<BusStopView>(); //TODO:
        for(BusStop busStop: busStopList){
            //busStopViewList.add(new BusStopView(busStop));
        }
    }

    public void updateBusList(List<Bus> busList){
        busViewList.clear();
        BusView busView;
        int i = 0;
        for (Bus bus: busList) {
            busView = new BusView(bus);
            busView.setMiniViewSize(BUS_WIDTH, BUS_HEIGHT);
            busView.setMiniViewPosition(10, i*(BUS_HEIGHT + 2));
            busViewList.add(busView);
            i++;
        }
    }

    public DetailView getCurrentDetailView() {
        return currentDetailView;
    }

    public List<BusView> getBusViewList() {
        return busViewList;
    }

    public void setCurrentDetailView(BusView currentDetailView) {
        this.currentDetailView = currentDetailView;
    }
}
