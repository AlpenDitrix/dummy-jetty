package com.avapira.dummy_indexer.indexer;

/**
 *
 */
public class Place {
    private String ambit;
    private final int location;

    public Place(int place) {
        location = place;
    }

    public int getLocation() {
        return location;
    }

    public void setAmbit(String s) {
        ambit = s;
    }
}
