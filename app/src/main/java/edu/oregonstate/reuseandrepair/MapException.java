package edu.oregonstate.reuseandrepair;

/**
 * Created by Donald on 6/8/2015.
 */
public class MapException extends Exception {
    public MapException() {
    }

    public MapException(String detailMessage) {
        super(detailMessage);
    }

    public MapException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public MapException(Throwable throwable) {
        super(throwable);
    }
}
