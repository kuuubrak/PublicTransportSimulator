package DataModel;

/**
 * Created by ppeczek on 2014-05-20.
 */
public class Counter {
    private int value;

    public Counter(int value) {
        this.value = value;
    }

    public int countdown() {
        if (value > 0) {
            value--;
        }
        if (value == 0) {
            System.out.println("Jestem na przystanku. Rzuć event.");
            return 0;
        }
        return 1;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
