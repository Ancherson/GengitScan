package up.visulog.analyzer;

import java.lang.module.Configuration;

import up.visulog.webgen.WebGen;

/**
 * <b>AnalyzerPlugin</b> is an interface which represents the plugin
 * All the plugins implement this interface
 */
public interface AnalyzerPlugin extends Runnable{
	
	/**
	 * This is where the result of the plugin is store
	 */
    interface Result {
    	/**
    	 * @return the result of the plugin as a <b>String</b>
    	 */
        String getResultAsString();
        /**
         * @return the result of the plugin as a <b>String</b> in html format
         * @deprecated
         */
        String getResultAsHtmlDiv();
        
        /**
         * This method uses the <b>WebGen</b> object to generate the html
         * @param wg is an object that generate the html
         */
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
