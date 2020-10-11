package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.APIresponse;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountCommitsPerAuthorPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private up.visulog.analyzer.CountCommitsPerAuthorPlugin.Result result;

    public CountCommitsPerAuthorPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static up.visulog.analyzer.CountCommitsPerAuthorPlugin.Result processLog(List<Commit> gitLog) {
        var result = new up.visulog.analyzer.CountCommitsPerAuthorPlugin.Result();
        for (var commit : gitLog) {
            var nb = result.commitsPerAuthor.getOrDefault(commit.author, 0);
            result.commitsPerAuthor.put(commit.author, nb + 1);
        }
        return result;
    }

    @Override
    public void run() {
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));
    }

    @Override
    public up.visulog.analyzer.CountCommitsPerAuthorPlugin.Result getResult() {
        if (result == null) run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> commitsPerAuthor = new HashMap<>();

        Map<String, Integer> getCommitsPerAuthor() {
            return commitsPerAuthor;
        }

        @Override
        public String getResultAsString() {
            return commitsPerAuthor.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            APIresponse json = new APIresponse();
            json.createLogFileForCommits();
            json.createLogFileForIssues();
            json.createLogFileForOneCommit("7fa1a620338aec79ade8c77c2edfeba64cfad393");
            json.createLogFileForOneIssue(41);
            StringBuilder html = new StringBuilder("<div>Commits per author: <ul>");
            for (var item : commitsPerAuthor.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
    }
}
