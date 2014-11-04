package com.avapira.dummy_indexer.indexer;

import java.util.ArrayList;

/**
 * Instance of that class stores information of all word occurrences into one specified file. Also,
 * when user made search request for this word, ambit of that word will be cached (for displaying it to user)
 */
class RendezVous {
    /**
     * List of all word occurrence locations
     */
    private final ArrayList<Place> places    = new ArrayList<>();
    private final int file;
    /**
     * Is ambits are already generated for this word
     */
    private boolean hasAmbits = false;

    public RendezVous(int file) {
        this.file = file;
    }

    public int getFile() {
        return file;
    }

    public ArrayList<Place> getPlaces() {
        return places;
    }

    /**
     * Add new word occurrence location
     *
     * @param at strict location into the file
     */
    public void met(int at) {
        places.add(new Place(at));
    }

    /**
     * Caches ambit for all places where that word was found.
     *
     * @param source file-string
     */
    public void generateAmbit(String source, String word) {
        for (Place p : places) {
            int start = p.getLocation() - 60;
            int end = p.getLocation() + 60;
            while (start > 0 && source.charAt(start) != ' ') { // jump to space or string start
                start--;
            }
            start = start < 0 ? 0 : start;
            end = source.indexOf(' ', end);
            end = end == -1 ? source.length() - 1 : end; // jump to space or string end
            p.setAmbit(source.substring(start, end).replace(" " + word + " ", "<b> " + word + " </b>"));
            //todo: выделение слова в окрестности должно работять и для крайних позиций
        }
        hasAmbits = true;
    }

    /**
     * @return [] if no occurrences of that word was found or [PLACE1, PLACE2, ...]
     */
    @Override
    public String toString() {
        if (places.size() == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(places.size());
        sb.append("\\[");
        sb.append(places.get(0));
        for (int i = 1; i < places.size(); i++) {
            sb.append(", ");
            sb.append(places.get(i));
        }
        sb.append("]");
        return sb.toString();
    }


    public boolean hasAmbits() {
        return hasAmbits;
    }
}
