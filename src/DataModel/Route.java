package DataModel;

/**
 * Created by ppeczek on 2014-05-20.
 */
public class Route {
    private BusStop toBusStop;
    private int length;

    public BusStop getToBusStop() {
        return toBusStop;
    }

    public void setToBusStop(BusStop toBusStop) {
        this.toBusStop = toBusStop;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
