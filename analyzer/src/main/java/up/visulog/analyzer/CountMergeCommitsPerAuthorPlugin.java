package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountMergeCommitsPerAuthorPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    public CountMergeCommitsPerAuthorPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        Map<String,String>emailToName = new HashMap<String,String>();
        for (var commit : gitLog) {
            if(commit.mergedFrom != null) { // If the commit is a merge commit, add one to the number of merge commits done by the author
            	String[] author = commit.author.split(" ");
            	String email = author[author.length - 1];
            	if(emailToName.get(email) == null) {
            		emailToName.put(email, commit.author);
            	}
            	var nb = result.mergeCommitsPerAuthor.getOrDefault(email, 0);
            	result.mergeCommitsPerAuthor.put(email, nb + 1);
            }
        }
        for(var e : emailToName.entrySet()) {
        	int nbCommit = result.mergeCommitsPerAuthor.remove(e.getKey());
        	result.mergeCommitsPerAuthor.put(e.getValue(), nbCommit);
        }
        return result;
    }

    @Override
    public void run() {
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath(), false));
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> mergeCommitsPerAuthor = new HashMap<>();

        Map<String, Integer> getCommitsPerAuthor() {
            return mergeCommitsPerAuthor;
        }

        @Override
        public String getResultAsString() {
            return mergeCommitsPerAuthor.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Number of merge commits per author: <ul>");
            for (var item : mergeCommitsPerAuthor.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
    }
}