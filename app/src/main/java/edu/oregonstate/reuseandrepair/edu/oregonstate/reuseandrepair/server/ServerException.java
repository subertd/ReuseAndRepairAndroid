package edu.oregonstate.reuseandrepair.edu.oregonstate.reuseandrepair.server;

/**
 * Created by Donald on 5/23/2015.
 */
public class ServerException extends Exception {

    public ServerException() {
        super();
    }

    public ServerException(String detailMessage) {
        super(detailMessage);
    }

    public ServerException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ServerException(Throwable throwable) {
        super(throwable);
    }
}
