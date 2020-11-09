package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountCommitsPerAuthorPerDayPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    public CountCommitsPerAuthorPerDayPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        //rester a modifier
        //ajouter la date
        for (var commit : gitLog) {
            var nb = result.commitsPerAuthorPerDay.getOrDefault(commit.author, 0);
            result.commitsPerAuthorPerDay.put(commit.author, nb + 1);
        }
        return result;
    }

    @Override
    public void run() {
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> commitsPerAuthorPerDay = new HashMap<>();

        Map<String, Integer> getCommitsPerAuthorPerDay() {
            return commitsPerAuthorPerDay;
        }

        @Override
        public String getResultAsString() {
            return commitsPerAuthorPerDay.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Commits per author per day: <ul>");
            for (var item : commitsPerAuthorPerDay.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
    }
}
