package model;

/**
 * Created by ppeczek on 2014-05-21.
 */
public class TrafficScheme {
    private static TrafficScheme ourInstance = new TrafficScheme();

    public static TrafficScheme getInstance() {
        return ourInstance;
    }

    private TrafficScheme() {
    }
}
