package simulator;

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by ppeczek on 2014-05-20.
 */
public final class SimulatorConstants {
    public final static int simulatorDefaultWaitTime = 1000;
    public final static double simulatorDefaultGenerationIntensity = 0.5;
    public final static String simulatorHostAddress = "127.0.0.1";
    public final static int simulatorPort = 8124;
    public final static int cooldownAfterLoops = 10;

    public final static int depotTerminusDistance = 2;
    public final static String depotName = "Zajezdnia";
    public final static String terminusName = "Pętla";
    public final static int loops = 3;

    public final static int N = 10;
    public final static ArrayList<Pair<String, Integer>> busStopSettings = new ArrayList<Pair<String, Integer>>() {{
        add(new Pair<String, Integer>("Ratusz ArsenaL", 3));
        add(new Pair<String, Integer>("DZendruM, konwinient czendz tu sabulban end longdistanz treinz", 5));
        add(new Pair<String, Integer>("SGHuj", 3));
        add(new Pair<String, Integer>("Łest Łejłej stejszn", 2));
        add(new Pair<String, Integer>("czytał Tomasz Knapik", 4));
    }};

}
