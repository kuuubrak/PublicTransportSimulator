package zkm;

import DataModel.*;
import sun.util.logging.resources.logging_pt_BR;

import java.io.IOException;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by macdr_000 on 19.03.14.
 */
public class ZkmMain {
    public static void main(String[] args)
    {
        if (args.length != 3)
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

        ServerClient sc = new ServerClient(/* TODO */);
        if (sc.connect() == false)
        {
            System.out.println("Connection unavailable");
            return;
        }
        Integer loopTimeMinute = 0; /* TODO */

        System.out.println("Press enter key to stop.");
        Integer k = 0;
        do {
            Pair<List<Bus>, List<BusStop>> pair = sc.getMockup();
            Integer busesNr = pair.first.size();
            Integer freeSeatsNr = 0;
            Integer peopleWaitingNr = 0;

            for (Bus bus : pair.first)
            {
                // TODO: zlicz liczbę wolnych miejsc
            }

            for (BusStop busStop: pair.second)
            {
                // TODO: zliczyć liczbę pasażerów oczekujących
            }

            // Decision making
            if (freeSeatsNr == 0)
            {
                if(peopleWaitingNr == 0)
                {
                    sc.sendMessage(/* TODO - zmniejsz*/);
                }
                else
                {
                    sc.sendMessage(/* TODO - zwiększ */);
                }
            }
            else
            {
                Integer coef = peopleWaitingNr > freeSeatsNr ? (peopleWaitingNr / freeSeatsNr) : 1;
                coef = coef * loopTimeMinute;
                if (coef > upperBound)
                {
                    sc.sendMessage(/* TODO - zwiększ */);
                }
                else if (coef < lowerBound)
                {
                    sc.sendMessage(/* TODO - zmniejsz */);
                }
                else
                {
                    sc.sendMessage(/* TODO - nie zmieniaj */);
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
}
