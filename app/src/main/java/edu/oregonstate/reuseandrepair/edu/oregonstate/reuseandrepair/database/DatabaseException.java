package edu.oregonstate.reuseandrepair.edu.oregonstate.reuseandrepair.database;

/**
 * Created by Donald on 5/23/2015.
 */
public class DatabaseException extends Exception {

    public DatabaseException() {
        super();
    }

    public DatabaseException(final String detailMessage) {
        super(detailMessage);
    }

    public DatabaseException(final String detailMessage, final Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DatabaseException(final Throwable throwable) {
        super(throwable);
    }
}
