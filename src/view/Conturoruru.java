package view;

import event.guievents.ContinuousSimulationEvent;
import event.guievents.NewPassengerEvent;
import event.guievents.PassengerGenerationInterval;
import network.Client;
import order.FunctionalityMockupParser;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * TODO delete
 */
public class Conturoruru{
    private Client client = null;
    LinkedBlockingQueue<SimulatorEvent> evQueue = new LinkedBlockingQueue<>();
    FunctionalityMockupParser fun=null;
    public Conturoruru(FunctionalityMockupParser gui){
        addImplementor(gui);
        connect();
    }

    public Conturoruru(){
        this(null);
    }

    public void addImplementor(FunctionalityMockupParser gui){
        fun=gui;
    }

    public void suck(){
        while (true){
            SimulatorEvent ev=null;
            while(!evQueue.isEmpty()){//przewijanie do ostatniej otrzymanej
                ev = evQueue.poll();
            }
            fun.newMockup(ev.getMockup());
        }
    }

    public void setContinuous(boolean set){
        client.send(new ContinuousSimulationEvent(set));
    }

    public void createPassenger(String from, String to){
        client.send(new NewPassengerEvent(from, to));
    }

    public void setPassengerGenerationIntervals(int min, int max){
        client.send(new PassengerGenerationInterval(min, max));
    }

    public void kill(){
        client.closeConnection();
        client = null;
    }

    public void connect(){
        client = new Client("127.0.0.1", 666);
        evQueue.clear();
        client.setEventsBlockingQueue(evQueue);
        client.connect();
    }
}
