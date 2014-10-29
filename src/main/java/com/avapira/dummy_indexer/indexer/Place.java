package com.avapira.dummy_indexer.indexer;

/**
 * Info about some word occurrence string location into the some file.
 * When user made request for this word also ambit (for displaying to user) will be cached at {@link
 * com.avapira.dummy_indexer.indexer.RendezVous#generateAmbit(String)}
 */
public class Place {
    /**
     * Index of place there some word was occurred in some file.
     */
    private final int    location;
    /**
     * Ambit of this some word in file. It contain at least 60 character to both directions (if file start/end did
     * not nearer) and starts\end at first delimiter character met after that '60-symbols-border'.
     */
    private       String ambit;

    public Place(int place) { location = place; }

    public int getLocation() { return location; }

    public String getAmbit() { return ambit; }

    public void setAmbit(String s) { ambit = s; }
}
