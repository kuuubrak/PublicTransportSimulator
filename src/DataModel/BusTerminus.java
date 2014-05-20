package DataModel;

/**
 * Created by ppeczek on 2014-05-21.
 */
public class BusTerminus extends BusStopBase {
    private static BusTerminus ourInstance = new BusTerminus();

    public static BusTerminus getInstance() {
        return ourInstance;
    }

    public BusTerminus() {}

    public BusTerminus(final String name) {
        super(name);
    }
}
