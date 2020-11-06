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
	    
	    public HashMap<String, String> getName() {
	    	List<Commit> commits = Commit.parseLogFromCommand(configuration.getGitPath());
	    	HashMap<String, String>emailToName = new HashMap<String, String>();
	    	
	    	for(var commit : commits) {
	    		String[]author = commit.author.split(" ");
	    		String email = author[author.length - 1];
	    		if(emailToName.get(email) == null) {
	    			emailToName.put(email, commit.author);
	    		}
	    	}
	    	
	    	return emailToName;
	    }

	    public Result processLog(HashMap<String, Integer>LinesPerEmail) {
	        var result = new Result();
	        
	        var emailToName = getName();
	        HashMap<String, Integer>LinesPerName = new HashMap<String, Integer>();
	        for(var ass : emailToName.entrySet()) {
	        	Integer lines = LinesPerEmail.get(ass.getKey());
	        	LinesPerName.put(ass.getValue(), lines);
	        }
	        
	        System.out.println(LinesPerName);
	        
	        return result;
	    }

	    @Override
	    public void run() {
	        result = processLog(Commit.countLinesContribution(configuration.getGitPath()));
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
