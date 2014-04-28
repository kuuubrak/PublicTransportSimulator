package zkm;

import DataModel.*;
import Order.FunctionalityMockupParser;
import Order.Order;
import Order.zkm.OrderReleaseBus;
import Order.zkm.OrderTrapBus;
import network.Client;
import sun.util.logging.resources.logging_pt_BR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by macdr_000 on 19.03.14.
 */
public class ZkmMain implements FunctionalityMockupParser{

    private String host;
    private int port;
    private Client sc = new Client();
    private Mockup mockup;
    private Integer lowerBound;
    private Integer upperBound;

    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.out.println("Wrong number of arguments. Program is stopping.");
            return;
        }

        Integer lowerBound = Integer.parseInt(args[0]);
        Integer upperBound = Integer.parseInt(args[1]);
        if (lowerBound > upperBound)
        {
            System.out.println("Lower bound (first arg) cannot be higher than upper bound (second argument)\n"
                    + "Program is stopping.");
            return;
        }

        ZkmMain zkmMain = new ZkmMain("127.0.0.1", 8125, lowerBound, upperBound);
        zkmMain.mainLoop();
    }

    public ZkmMain(String host, int port, Integer lowerBound, Integer upperBound)
    {
        this.host = host;
        this.port = port;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    private void receiveMockup()
    {

        Order<FunctionalityMockupParser> order = null;
        try {
            order = sc.getOrdersQueue().take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(order != null){
                order.execute(this);
            }

    }

    public void mainLoop()
    {
        sc.establishConnection(host, port);
        Integer loopTimeMinute = 0; /* TODO */

        System.out.println("Press enter key to stop.");
        Integer k = 0;
        do {

            System.out.println("K1");
            sc.send(new OrderReleaseBus());
            System.out.println("K2");
            receiveMockup();
            System.out.println("K3");
            ArrayList<Bus> buses = (ArrayList<Bus>) mockup.getBuses();
            ArrayList<BusStop> busStops = (ArrayList<BusStop>) mockup.getBusStops();

            Integer busesNr = buses.size();
            Integer freeSeatsNr = 0;
            Integer peopleWaitingNr = 0;

            for (Bus bus : buses)
            {
                freeSeatsNr += bus.getNumberOfFreeSeats();
            }

            for (BusStop busStop: busStops)
            {
                peopleWaitingNr += busStop.getNumberOfPassengersWaiting();
            }

            // Decision making
            if (freeSeatsNr == 0)
            {
                if(peopleWaitingNr == 0)
                {
                    sc.send(new OrderTrapBus());
                }
                else
                {
                    sc.send(new OrderReleaseBus());
                }
            }
            else
            {
                Integer coef = peopleWaitingNr > freeSeatsNr ? (peopleWaitingNr / freeSeatsNr) : 1;
                coef = coef * loopTimeMinute;
                if (coef > upperBound)
                {
                    sc.send(new OrderReleaseBus());
                }
                else if (coef < lowerBound)
                {
                    sc.send(new OrderTrapBus());
                }
            }

            try
            {
                k = System.in.available();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return;
            }
        } while (k == 0);

        System.out.println("ZKM has stopped.");
    }

    @Override
    public void newMockup(Mockup fresh) {
        mockup = fresh;
    }
}
