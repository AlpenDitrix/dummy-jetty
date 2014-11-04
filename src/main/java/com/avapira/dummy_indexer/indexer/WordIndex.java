package com.avapira.dummy_indexer.indexer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Each instance of this class stores information about places where some word was met in some files. Object has info
 * only about file IDs and meeting locations. It doesn't know the word or link to the files.
 */
public class WordIndex {

    /**
     * Mapping of file wordIndex to {@link com.avapira.dummy_indexer.indexer.RendezVous} instances,
     * which stores all meetings of word in specified file.
     * <p/>
     * Linked map needed because I want to receive occurrences in files sorted by the file index.
     */
    private final Map<Integer, RendezVous> meetings = new LinkedHashMap<>();

    public Map<Integer, RendezVous> getMeetings() {
        return meetings;
    }

    public Collection<RendezVous> getRvs() {
        return meetings.values();
    }

    /**
     * Adds new word occurrence location
     *
     * @param file id of file where the word was met
     * @param at   strict location into the file
     */
    public void met(int file, int at) {
        RendezVous rv = meetings.get(file);
        if (rv != null) {
            rv.met(at);
        } else { // word has not met in that file before
            meetings.put(file, new RendezVous(file));
            meetings.get(file).met(at);
        }
    }

    /**
     * @return {} if no occurrences of that word was found or {FILE_INDEX:RENDEZ_VOUS.toString(), ...}
     */
    @Override
    public String toString() {
        if (meetings.size() == 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(meetings.size());
        sb.append("\\{");
        for (Integer key : meetings.keySet()) {
            sb.append(key);
            sb.append(":");
            sb.append(meetings.get(key));
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    /**
     * @return total amount of some word occurrences
     */
    public int getTotalOccurrencesAmount() {
        int total = 0;
        for (RendezVous rv : meetings.values()) {
            total += rv.getPlaces().size();
        }
        return total;
    }

    /**
     * @return amount of some word occurrences in specified file
     */
    public int getFileOccurrencesAmount(int file) {
        RendezVous rv = meetings.get(file);
        if (rv == null) {
            return 0;
        } else {
            return rv.getPlaces().size();
        }
    }
}
