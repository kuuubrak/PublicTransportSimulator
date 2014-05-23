package model;

/**
 * Created by ppeczek on 2014-05-20.
 */
public class Counter {
    private int value;
    private boolean counted;

    public Counter(int value) {
        this.value = value;
        this.counted = true;
    }

    public int countdown() {
        if (isDownCounted()) {
            counted = false;
        }
        if (value > 0) {
            value--;
        }
        else {
            counted = true;
        }
        return value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isDownCounted() {
        return counted;
    }
}
