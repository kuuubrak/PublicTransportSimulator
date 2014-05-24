package model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

/**
 * <b>BusStop.</b><br>
 * <br>
 * Buffor, przetrzymuje  <b>Pasażerów</b> do momentu ich wejścia do <b>Autobusu</b> ( niekoniecznie wszyscy od razu wejdą ( pełny <b>Autobus</b> ) ).<br>
 * <b>Pasażerowie</b> oczekujący na miejsce w <b>Autobusie</b> tworzą kolejkę FIFO.<br>
 * Wysiadający mają pierwszeństwo przed wsiadającymi.<br>
 *
 * @author dan.krasniak
 */
public class BusStop implements Serializable {
    private final UUID ID;
    /**
     * Name of the <b>BusStop</b>.
     */
    private final String NAME;
    /**
     * Queue of <b>Passengers</b> waiting for a place in the <b>Bus</b>.
     */
    private Queue<Passenger> passengerQueue;
    private Route route;
    private boolean occupied;

    public BusStop(final String name) {
        this.ID = UUID.randomUUID();
        this.NAME = name;
        this.passengerQueue = new LinkedList<Passenger>();
        this.route = new Route();
        this.occupied = false;

    }

    /**
     * @return the NAME
     */
    public final String getNAME() {
        return NAME;
    }

    /**
     * <b>queuePush</b>.<br>
     * Sets the <b>Passenger</b> in the <b>Queue</b> of other <b>Passenegrs</b> waiting for a place in the <b>Bus</b>.<br>
     */
    public final void queuePush(final Passenger passenger) {
        passengerQueue.add(passenger);
    }

    /**
     * <b>takerAPassenger</b><br>
     * Transfers <b>One Passenger</b> from the <b>BusStop</b>.<br>
     * <br>
     * Takes <b>One Passenger</b> from the head of the <b>PassengerQueue</b>
     *
     * @return <b>One Passenger</b> from the <b>BusStop</b>
     */
    public final Passenger takePassenger() {
        return passengerQueue.poll();
    }

    /**
     * <b>isEmpty</b><br>
     *
     * @return true if <b>BusStop</b> has no <b>Passengers</b> in the <b>PassengerQueue</b>.
     */
    public final boolean isEmpty() {
        return passengerQueue.isEmpty();
    }

    /**
     * <b>getNumberOfPassengersWaiting</b><br>
     *
     * @return the number of <b>Passenger</b> foiting for a <b>Bus</b> in the <b>BusStop</b>
     */
    public final int getNumberOfPassengersWaiting() {
        return passengerQueue.size();
    }

    public Route getRoute() {
        return route;
    }

    public BusStop getNextBusStop() {
        return route.getToBusStop();
    }

    /**
     * <b>getDistanceToNextStop</b><br>
     *
     * @return distance - required to reach next <b>BusStop</b>.
     */
    public final int getDistanceToNextStop() {
        return route.getLength();
    }

    public void setRoute(BusStop busStop, int length) {
        getRoute().setToBusStop(busStop);
        getRoute().setLength(length);
    }

    public Queue<Passenger> getPassengerQueue() {
        return passengerQueue;
    }

    public boolean isOccupied() { return occupied; }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
