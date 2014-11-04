package com.avapira.dummy_indexer.indexer;

import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Singleton which indexes resource files and can show all occurrences of some word in that files
 */
public class InvertedIndex {

    private static ArrayList<InputStream> streams;
    private static boolean OK = false;
    private static InvertedIndex instance;
    /**
     * Map of files what will be or was indexed
     */
    private final Map<Integer, InputStream> files = new HashMap<>();
    /**
     * Mapping of found words to info about that places (file and strict place)
     */
    private final Map<String, WordIndex>    words = new HashMap<>();

    private InvertedIndex() {
        rescan();
    }

    public static void tryInit(ServletContext sc) {
        if (OK) {
            return;
        }
        final String fmt = "/index/uliss18_%s";
        int i = 0;
        streams = new ArrayList<>();
        InputStream is = sc.getResourceAsStream(String.format(fmt, i));
        while (is != null) {
            streams.add(is);
            is = sc.getResourceAsStream(String.format(fmt, ++i));
        }
        instance = new InvertedIndex();
        OK = true;
    }

    public static InvertedIndex getInstance() {
        return instance;
    }

    /**
     * Refreshes wordIndex of files at 'resources/wordIndex'
     */
    private synchronized void rescan() {
        files.clear();
        words.clear();

        int i = 0;
        for (InputStream is : streams) {
            files.put(i++, is);
        }

        try {
            doIndex();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scans files and adds gathered info into wordIndex maps
     *
     * @throws IOException when can't read file for indexing
     */
    private void doIndex() throws IOException {
        for (int i = 0; i < files.size(); i++) {
            InputStream is = files.get(i);
            String s = new BufferedReader(new InputStreamReader(is)).readLine();
            tokenize(s, i);
        }
    }

    /**
     * Parses file-string word-by-word processing indexation
     *
     * @param s      string representation of data file
     * @param fileId id of file which is processing now
     */
    private void tokenize(String s, int fileId) {
        StringTokenizer st = new StringTokenizer(s);
        String[] tokens = new String[st.countTokens()];
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = st.nextToken();
        }

        int lastIndex = 0;
        for (String t : tokens) {
            WordIndex wi = words.get(t);
            if (wi != null) { // word already met
                wi.met(fileId, lastIndex); // add new 'rendez vous'
                lastIndex += t.length() + 1; // word_length + spacing_character
            } else { // word has not met in any file before
                words.put(t, new WordIndex());
                words.get(t).met(fileId, lastIndex);
                lastIndex += t.length() + 1;
            }
        }
    }

    /**
     * Shows info in wordIndex about specified word
     *
     * @param s word to find
     *
     * @return {@link com.avapira.dummy_indexer.indexer.SearchResponse} object which stores information about all
     * places where specified word was met in files
     *
     * @throws IOException when user executes request about some word, wordIndex will cache some text around this word
     *                     site. If some file will be unable to read, then IOE will be thrown.
     */
    public SearchResponse search(String s) throws IOException {
        return new SearchResponse(s, words.get(s));
    }

    @Override
    public String toString() {
        return words.toString();
    }

    public Map<Integer, InputStream> getFiles() {
        return files;
    }
}
