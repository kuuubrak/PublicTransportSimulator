package zkm;

import mockup.Mockup;
import model.Bus;
import model.BusStop;
import network.Client;
import order.FunctionalityMockupParser;
import order.Order;
import order.zkm.OrderReleaseBus;
import order.zkm.OrderTrapBus;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Rafal Jagielski
 */
public class ZkmMain implements FunctionalityMockupParser {

    private String host;
    private int port;
    private Client sc = new Client();
    private Mockup mockup;
    private Integer lowerBound;
    private Integer upperBound;
    private Integer loopTimeMinute; // TODO


    public ZkmMain(String host, int port, Integer lowerBound, Integer upperBound, Integer loopTimeMinute) {
        this.host = host;
        this.port = port;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.loopTimeMinute = loopTimeMinute;
        mockup = new Mockup(new ArrayList<Bus>(), new ArrayList<BusStop>()); //Tymczasowo
    }

    public static void main(String[] args) {
        //if (args.length != 2)
        //{
        //    System.out.println("Wrong number of arguments. Program is stopping.");
        //    return;
        //}
        String host = "127.0.0.1";
        int port = 8125;
        Integer lowerBound = 10;//Integer.parseInt(args[0]);
        Integer upperBound = 20;//Integer.parseInt(args[1]);
        Integer loopTimeMinute = 0;
        if (lowerBound > upperBound) {
            System.out.println("Lower bound (first arg) cannot be higher than upper bound (second argument)\n"
                    + "Program is stopping.");
            return;
        }

        ZkmMain zkmMain = new ZkmMain(host, port, lowerBound, upperBound, loopTimeMinute);
        zkmMain.mainLoop();
    }

    private void receiveMockup() {

        Order<FunctionalityMockupParser> order;
        order = sc.getOrdersQueue().poll();

        if (order != null) {
            order.execute(this);
        }

    }

    public void mainLoop() {
        sc.establishConnection(host, port);

        System.out.println("Press enter key to stop.");

        do {
            receiveMockup();
            ArrayList<Bus> buses = (ArrayList<Bus>) mockup.getBuses();
            ArrayList<BusStop> busStops = (ArrayList<BusStop>) mockup.getBusStops();

            Integer freeSeatsNr = 0;
            Integer peopleWaitingNr = 0;

            for (Bus bus : buses) {
                freeSeatsNr += bus.getNumberOfFreeSeats();
            }

            for (BusStop busStop : busStops) {
                peopleWaitingNr += busStop.getNumberOfPassengersWaiting();
            }

            makeDecision(freeSeatsNr, peopleWaitingNr);

        } while (endLoop());

        System.out.println("ZKM has stopped.");
    }

    @Override
    public void newMockup(Mockup fresh) {
        mockup = fresh;
    }

    private boolean endLoop() {
        try {
            if (System.in.available() != 0) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void makeDecision(int freeSeatsNr, int peopleWaitingNr) {
        if (freeSeatsNr == 0) {
            if (peopleWaitingNr == 0) {
                sc.send(new OrderTrapBus());
            } else {
                sc.send(new OrderReleaseBus());
            }
        } else {
            Integer coef = peopleWaitingNr > freeSeatsNr ? (peopleWaitingNr / freeSeatsNr) : 1;
            coef = coef * loopTimeMinute;
            if (coef > upperBound) {
                sc.send(new OrderReleaseBus());
            } else if (coef < lowerBound) {
                sc.send(new OrderTrapBus());
            }
        }
    }
}
