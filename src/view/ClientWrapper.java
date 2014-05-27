package view;

import event.guievents.ContinuousSimulationEvent;
import event.guievents.NewPassengerEvent;
import event.guievents.PassengerGenerationInterval;
import network.Client;
import order.FunctionalityMockupParser;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * TODO delete
 */
public class ClientWrapper extends Thread{
    private Client client = null;
    private LinkedBlockingQueue<SimulatorEvent> evQueue = new LinkedBlockingQueue<>();
    private GuiFunctionality gun=null;
    private boolean runny = true;

    public ClientWrapper(GuiFunctionality gui){
        addImplementor(gui);
        connect();
    }

    public ClientWrapper(){
        this(null);
    }

    public void addImplementor(GuiFunctionality gui){
        gun=gui;
    }

    @Override
    public void run(){
        try {
            while (runny){
                SimulatorEvent ev = null;
                while (!evQueue.isEmpty()) {//przewijanie do ostatniej otrzymanej
                    ev = evQueue.poll(5, TimeUnit.SECONDS);
                }
                gun.newMockup(ev.getMockup());
            }
        }catch(InterruptedException e){
            if(runny) gun.connectionLost();
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
        runny = false;
    }

    public void connect(){
        client = new Client("127.0.0.1", 666);
        evQueue.clear();
        client.setEventsBlockingQueue(evQueue);
        if(client.connect()){
            gun.connectionEstablished();
        }else{
            gun.connectionLost();
        }
    }
}
