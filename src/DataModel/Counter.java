package DataModel;

/**
 * Created by ppeczek on 2014-05-20.
 */
public class Counter {
    private int value;
    private boolean counted;

    public Counter(int value) {
        this.value = value;
        this.counted = false;
    }

    public int countdown() {
        if (isCounted()) {
            counted = false;
        }
        if (value > 0) {
            value--;
        }
        if (value == 0) {
            System.out.println("Jestem na przystanku. Rzuć event.");
            counted = true;
            return value;
        }
        return value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isCounted() {
        return counted;
    }

    public void setCounted(boolean counted) {
        this.counted = counted;
    }
}
