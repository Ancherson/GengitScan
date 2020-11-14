package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.*;

import java.util.*;

public class GetExtensionsPerProjectPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    public GetExtensionsPerProjectPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(HashMap<String,Double> extensionsLog) {
            Result result = new Result();
            result.extensionsResult = extensionsLog;
            return result;
    }

    @Override
    public void run() {
        //Create an api to get extensions
        Extensions apiExtensions = new Extensions();
        //Get results of extensions
        HashMap<String,Double> resExtensions =  apiExtensions.parseExtensionsFromLog(configuration.getPrivateToken(),configuration.getIdProject());
        result = processLog(resExtensions);
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {
        private HashMap<String, Double> extensionsResult  = new HashMap<>();

        HashMap<String, Double> getExtensionsResult() {
            return extensionsResult;
        }

        @Override
        public String getResultAsString() {
            return extensionsResult.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Extensions in project: <ul>");
            for (var item : extensionsResult.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
    }
}
