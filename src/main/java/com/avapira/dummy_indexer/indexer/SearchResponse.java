package com.avapira.dummy_indexer.indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Object to send to user to display it on the JSP-page
 */
public class SearchResponse {
    /**
     * The word to find
     */
    String    request;
    /**
     * {@link com.avapira.dummy_indexer.indexer.WordIndex} for this word.
     */
    WordIndex wordIndex;

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
        for (int file : wordIndex.getMeetings().keySet()) {
            RendezVous rv = wordIndex.getMeetings().get(file);
            if (!rv.hasAmbits()) {
                File f = InvertedIndex.getInstance().getFiles().get(file);
                String source = new BufferedReader(new FileReader(f)).readLine();

                rv.generateAmbit(source);
            }
        }
    }

    public String getRequest() {
        return request;
    }

    public WordIndex getWordIndex() {
        return wordIndex;
    }
}