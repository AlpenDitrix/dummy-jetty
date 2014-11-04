package com.avapira.dummy_indexer.server;

import com.avapira.dummy_indexer.indexer.InvertedIndex;
import com.avapira.dummy_indexer.indexer.SearchResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
public class SearchServlet extends HttpServlet {

    private static final Logger logger      = LogManager.getLogger("Request");
    private static final String fmtSucc     = "Request: %s; Total found: %s";
    private static final String fmtNotFound = "Request: %s; No occurrences were found";
    private static final String fmtError    = "Request: %s\n%s";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        InvertedIndex.tryInit(getServletContext());
        String userRequest = req.getParameter("q");

        try {
            SearchResponse sr = InvertedIndex.getInstance().search(userRequest);
            int found = sr.getWordIndex().getTotalOccurrencesAmount();
            req.setAttribute("q", sr.getRequest());
            req.setAttribute("chw", sr.getWordIndex());
            if (found > 0) {
                logger.info(String.format(fmtSucc, userRequest, sr.getWordIndex().getTotalOccurrencesAmount()));
            } else {
                logger.warn(String.format(fmtNotFound, userRequest));
            }
        } catch (Exception e) {
            logger.error(String.format(fmtError, userRequest, e.getMessage()));
            e.printStackTrace();
        }
        req.getRequestDispatcher("search.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("search.jsp").forward(req, resp);
    }

}
