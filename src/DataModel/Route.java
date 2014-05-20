package DataModel;

/**
 * Created by ppeczek on 2014-05-20.
 */
public class Route {
    private BusStopBase toBusStopBase;
    private int length;

    public BusStopBase getToBusStopBase() {
        return toBusStopBase;
    }

    public void setToBusStopBase(BusStopBase toBusStopBase) {
        this.toBusStopBase = toBusStopBase;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
