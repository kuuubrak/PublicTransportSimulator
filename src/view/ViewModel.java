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
        for (Bus bus: busList) {
            busViewList.add(new BusView(bus));
        }
           if(!busViewList.isEmpty()){
               currentDetailView = busViewList.get(0);
           }else{
               currentDetailView = null;
           }
    }

    public DetailView getCurrentDetailView() {
        return currentDetailView;
    }
}
