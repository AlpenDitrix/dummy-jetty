package com.avapira.dummy_indexer.indexer;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class WordIndex {

    public Map<Integer, RendezVous> getMeetings() {
        return meetings;
    }

    private final Map<Integer, RendezVous> meetings = new HashMap<Integer, RendezVous>();

    public WordIndex(int file, int at) {
        meetings.put(file, new RendezVous(file, at));
    }

    public void met(int file, int at) {
        RendezVous rv = meetings.get(file);
        if (rv != null) {
            rv.met(at);
        } else { // word has not met in that file before
            meetings.put(file, new RendezVous(file, at));
        }
    }

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
}
