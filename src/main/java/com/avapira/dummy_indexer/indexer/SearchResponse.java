package com.avapira.dummy_indexer.indexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
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

    public SearchResponse(String request, WordIndex wordIndex) {
        this.request = request;
        this.wordIndex = wordIndex;
        try {
            tryCreateAmbits();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static SearchResponse intersect(SearchResponse s1, SearchResponse s2) {
        Map<Integer, RendezVous> m1 = s1.getWordIndex().getMeetings();
        Map<Integer, RendezVous> m2 = s2.getWordIndex().getMeetings();
        Map<Integer, RendezVous> mResult = new HashMap<>();
        for (Integer file : m1.keySet()) {
            RendezVous rv2 = m2.get(file);
            if (rv2 != null) { // both in file
                ArrayList<Place> places1 = m1.get(file).getPlaces();
                ArrayList<Place> places2 = rv2.getPlaces();
                if(null != mResult.put(file, new RendezVous(file, true))) {
                    throw new RuntimeException("Duplicate file indexing");
                }
                ArrayList<Place> placesRes = mResult.get(file).getPlaces();
                placesRes.addAll(places1);
                placesRes.addAll(places2);
            }
        }
        return new SearchResponse(s1.getRequest().concat(" ").concat(s2.getRequest()), new WordIndex(mResult));
    }

}