package up.visulog.analyzer;

import java.lang.module.Configuration;

import up.visulog.webgen.WebGen;

public interface AnalyzerPlugin extends Runnable{
    interface Result {
        String getResultAsString();
        String getResultAsHtmlDiv();
        void getResultAsHtmlDiv(WebGen wg);
    }

    /**
     * run this analyzer plugin
     */
    void run();

    /**
     *
     * @return the result of this analysis. Runs the analysis first if not already done.
     */
    Result getResult();
}
