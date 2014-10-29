package com.avapira.dummy_indexer.indexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 */
public class SearchResponse {
    String    s;
    WordIndex index;
    int total = 0;

    public SearchResponse(String s, WordIndex index) throws IOException {
        this.s = s;
        this.index = index;

        for (RendezVous rv : index.getMeetings().values()) {
            if (!rv.hasAmbits()) {
                String source = new BufferedReader(new FileReader(InvertedIndex.files.get(rv.getFile()))).readLine();
                rv.generateAmbit(source);
            }
            total += rv.getPlaces().size();
        }
    }

    public String getRequest() {
        return s;
    }

    public WordIndex getIndex() {
        return index;
    }
}