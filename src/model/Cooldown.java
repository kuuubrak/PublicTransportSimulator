package model;

/**
 * Created by ppeczek on 2014-05-21.
 */
public class Cooldown extends Counter {
    public int max_value;

    public Cooldown(int value) {
        super(value);
        max_value = value;
    }

    public void reset() {
        setValue(max_value);
    }

    @Override
    public int countdown() {
        //TODO: rzuć event
        super.countdown();
        if (isDownCounted()) {
            reset();
        }
        return getValue();
    }
}
