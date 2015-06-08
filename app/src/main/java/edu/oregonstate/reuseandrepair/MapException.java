package edu.oregonstate.reuseandrepair;

/**
 * Indicates a problem displaying the physical address of an organization in a map view
 *
 * Created by Donald on 6/8/2015.
 */
public class MapException extends Exception {

    public MapException(String detailMessage) {
        super(detailMessage);
    }
}
