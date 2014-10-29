package com.avapira.dummy_indexer.indexer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 */
public class InvertedIndex {

    static {
        rescan();
    }

     @Override
    public String toString() {
        return words.toString();
    }

    static final Map<Integer, File>     files = new HashMap<Integer, File>();
    private static final Map<String, WordIndex> words = new HashMap<String, WordIndex>();

    public static void rescan() {
        files.clear();
        words.clear();
        File indexFolder = new File("index");
        File[] ff = indexFolder.listFiles();
        int i = 0;
        for (File f : ff) {
            files.put(i++, f);
        }
        try {
            doIndex();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void doIndex() throws IOException {
        for (int i = 0; i < files.size(); i++) {
            File f = files.get(i);
            String s = new BufferedReader(new FileReader(f)).readLine();
            tokenize(s, i);
        }
    }

    private static void tokenize(String s, int fileId) {
        StringTokenizer st = new StringTokenizer(s);
        String[] tokens = new String[st.countTokens()];
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = st.nextToken();
        }
        int lastIndex = 0;
        for (String t : tokens) {
            WordIndex wi = words.get(t);
            if (wi != null) {
                wi.met(fileId, lastIndex);
                lastIndex += t.length() + 1;
            } else { // word has not met in any file before
                words.put(t, new WordIndex(fileId, lastIndex));
                lastIndex += t.length() + 1;
            }
        }
    }

    public static SearchResponse search(String s) throws IOException {
        return new SearchResponse(s, words.get(s));
    }

}
