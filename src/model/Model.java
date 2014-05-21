package model;

/**
 * Created by ppeczek on 2014-05-21.
 */
public class Model {
    private static Model ourInstance = new Model();

    public static Model getInstance() {
        return ourInstance;
    }

    private Model() {
    }
}
