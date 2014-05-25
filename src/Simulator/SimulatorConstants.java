package simulator;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by ppeczek on 2014-05-20.
 */
public final class SimulatorConstants {
    public final static int simulationSpeed = 50;
    public final static double simulatorDefaultGenerationIntensity = 0.5;
    public final static String simulatorHostAddress = "127.0.0.1";
    public final static int simulatorPort = 8124;
    public final static int cooldownAfterLoops = 10;
    public final static int defaultBusReleaseCooldown = 30;

    public final static int depotTerminusDistance = 2;
    public final static String depotName = "Zajezdnia";
    public final static String terminusName = "Pętla";
    public final static int loops = 3;


    public final static String ratuszStopName = "Ratusz ArsenaL";
    public final static String centrumStopName = "DZendruM";
    public final static String SGHStopName = "SGHuj";
    public final static String westStopName = "Łest stejszn";
    public final static String tomaszKnapik = "czytał Tomasz Knapik";

    public final static int N = 10;
    public final static ArrayList<Pair<String, Integer>> busStopSettings = new ArrayList<Pair<String, Integer>>() {{
        add(new Pair<String, Integer>(ratuszStopName, 3));
        add(new Pair<String, Integer>(centrumStopName, 5));
        add(new Pair<String, Integer>(SGHStopName, 3));
        add(new Pair<String, Integer>(westStopName, 2));
        add(new Pair<String, Integer>(tomaszKnapik, 4));
    }};

}
