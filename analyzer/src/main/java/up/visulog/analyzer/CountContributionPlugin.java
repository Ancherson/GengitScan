package up.visulog.analyzer;

import up.visulog.analyzer.CountCommitsPerAuthorPlugin.Result;
import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountContributionPlugin implements AnalyzerPlugin{
	 private final Configuration configuration;
	    private Result result;

	    public CountContributionPlugin(Configuration generalConfiguration) {
	        this.configuration = generalConfiguration;
	    }
	    
	    public HashMap<String, String> getName(HashMap<String, Integer>hm) {
	    	List<Commit> commits = Commit.parseLogFromCommand(configuration.getGitPath());
	    	HashMap<String, String>emailToName = new HashMap<String, String>();
	    	
	    	
	    	return emailToName;
	    }

	    public Result processLog(List<Commit> gitLog) {
	        var result = new Result();
	        
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
	        private final Map<String, Integer> contributionPerAuthor = new HashMap<>();

	        Map<String, Integer> getCommitsPerAuthor() {
	            return contributionPerAuthor;
	        }

	        @Override
	        public String getResultAsString() {
	            return contributionPerAuthor.toString();
	        }

	        @Override
	        public String getResultAsHtmlDiv() {
	            StringBuilder html = new StringBuilder("<div>Contribution per author: <ul>");
	            for (var item : contributionPerAuthor.entrySet()) {
	                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
	            }
	            html.append("</ul></div>");
	            return html.toString();
	        }
	    }
}
