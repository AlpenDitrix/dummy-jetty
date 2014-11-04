package com.avapira.dummy_indexer.indexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Object to send to user to display it on the JSP-page
 */
public class SearchResponse {
    /**
     * The word to find
     */
    private final String    request;
    /**
     * {@link com.avapira.dummy_indexer.indexer.WordIndex} for this word.
     */
    private final WordIndex wordIndex;

    public SearchResponse(String request, WordIndex wordIndex) throws IOException {
        this.request = request;
        this.wordIndex = wordIndex;
        tryCreateAmbits();
    }

    /**
     * Creates ambits for all occurrences if they was not cached yet
     *
     * @throws IOException if some file where that word was met are unable to read
     */
    private void tryCreateAmbits() throws IOException {
        if (wordIndex == null) {
            return;
        }
        Map meetings = wordIndex.getMeetings();
        if (meetings == null) {
            return;
        }
        for (int file : wordIndex.getMeetings().keySet()) {
            RendezVous rv = wordIndex.getMeetings().get(file);
            if (!rv.hasAmbits()) {
                InputStream is = InvertedIndex.getInstance().getFiles().get(file);
                is.reset();
                String source = new BufferedReader(new InputStreamReader(is)).readLine();

                rv.generateAmbit(source, request);
            }
        }
    }

    public String getRequest() {
        return request;
    }

    public WordIndex getWordIndex() {
        return wordIndex;
    }

    public void intersect(SearchResponse searchResponse) {
        Map<Integer, RendezVous> m1 = wordIndex.getMeetings();
        Map<Integer, RendezVous> m2 = searchResponse.getWordIndex().getMeetings();
        List<Integer> marker = new ArrayList<>();
        for (Integer file : m1.keySet()) {
            RendezVous rv2 = m2.get(file);
            if (rv2 != null) {
                ArrayList places1 = m1.get(file).getPlaces();
                ArrayList places2 = rv2.getPlaces();
                places1.addAll(places2);
            } else {
                marker.add(file);
            }
        }
        for (Integer i : marker) {
            m1.remove(i);
        }

    }

}