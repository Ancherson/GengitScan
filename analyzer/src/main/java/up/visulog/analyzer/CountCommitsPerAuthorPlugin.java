package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.webgen.WebGen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountCommitsPerAuthorPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;
    private boolean allBranchs;

    public CountCommitsPerAuthorPlugin(Configuration generalConfiguration, boolean allBranchs) {
        this.configuration = generalConfiguration;
        this.allBranchs = allBranchs;
    }

    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        Map<String,String>emailToName = new HashMap<String,String>();
        for (var commit : gitLog) {
        	String[] author = commit.author.split(" ");
        	String email = author[author.length - 1];
        	if(emailToName.get(email) == null) {
        		emailToName.put(email, commit.author);
        	}
            var nb = result.commitsPerAuthor.getOrDefault(email, 0);
            result.commitsPerAuthor.put(email, nb + 1);
        }
        for(var e : emailToName.entrySet()) {
        	int nbCommit = result.commitsPerAuthor.remove(e.getKey());
        	result.commitsPerAuthor.put(e.getValue(), nbCommit);
        }
        return result;
    }

    @Override
    public void run() {
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath(), allBranchs));
    }

    @Override
    public Result getResult() {
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
            StringBuilder html = new StringBuilder("<div>Commits per author: <ul>");
            for (var item : commitsPerAuthor.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
        
        @Override
        public void getResultAsHtmlDiv(WebGen wg) {
            ArrayList<String> authorOfCommits = new ArrayList<String>();
            ArrayList<Integer> numberOfCommits= new ArrayList<Integer>();
            for(var data : getCommitsPerAuthor().entrySet()){
                authorOfCommits.add(data.getKey());
                numberOfCommits.add(data.getValue());    
            }
        }
    }
}
