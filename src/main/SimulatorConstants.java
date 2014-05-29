package main;

import model.Pair;

import java.util.ArrayList;

/**
 * Created by ppeczek on 2014-05-20.
 */
public final class SimulatorConstants {
    public final static int simulationSpeed = 10;
    public final static String simulatorHostAddress = "127.0.0.1";
    public final static int simulatorPort = 8124;
    public final static int connectingTimeout = 5000;
    public final static int cooldownAfterLoops = 10;
    public final static int defaultBusReleaseCooldown = 15;
    public final static int noOfSeatsInBus = 10;
    public final static int defaultMinGenerationTime = 1;
    public final static int defaultMaxGenerationTime = 5;
    public final static int randomTimeGenerationShift = 1; //Random.nextInt generuje wartosci od 0, a nie od 1

    public final static int depotTerminusDistance = 2;
    public final static String depotName = "Zajezdnia";
    public final static String terminusName = "Pętla";
    public final static int loops = 3;


    public final static String ratuszStopName = "Ratusz ArsenaL";
    public final static String centrumStopName = "DZendruM";
    public final static String SGHStopName = "SGHuj";
//    public final static String westStopName = "Łest stejszn";
//    public final static String tomaszKnapik = "czytał Tomasz Knapik";

    public final static int N = 10;
    public final static ArrayList<Pair<String, Integer>> busStopSettings = new ArrayList<Pair<String, Integer>>() {{
        add(new Pair<>(ratuszStopName, 3));
        add(new Pair<>(centrumStopName, 5));
        add(new Pair<>(SGHStopName, 3));
//        add(new Pair<>(westStopName, 2));
//        add(new Pair<>(tomaszKnapik, 4));
    }};

}
