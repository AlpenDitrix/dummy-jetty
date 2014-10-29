package com.avapira.dummy_indexer.indexer;

import java.util.ArrayList;

/**
 *
 */
public class RendezVous {
    private final ArrayList<Place> places = new ArrayList<Place>();
    private final int file;
    private boolean hasAmbits = false;

    public RendezVous(int file, int at) {
        this.file = file;
        places.add(new Place(at));
    }

    public ArrayList<Place> getPlaces() {
        return places;
    }

    public int getFile() {
        return file;
    }

    public void met(int at) {
        places.add(new Place(at));
    }

    public void generateAmbit(String source) {
        for (int i = 0; i < places.size(); i++) {
            Place p = places.get(i);
            int start = p.getLocation() - 60;
            int end = p.getLocation() + 60;
            while (start > 0 && source.charAt(start) != ' ') {
                start--;
            }
            start = start < 0 ? 0 : start;
            end = source.indexOf(' ', end);
            end = end == -1 ? source.length() - 1 : end;
            p.setAmbit(source.substring(start, end));
        }
        hasAmbits = true;
    }

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
