package up.visulog.analyzer;

import up.visulog.analyzer.CountCommitsPerAuthorPlugin.Result;
import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountLinesPerAuthorPlugin implements AnalyzerPlugin{
	 private final Configuration configuration;
	    private Result result;
	    
	    // true means sort lines added, false means sort lines deleted
	    private boolean sortLineAdded;

	    public CountLinesPerAuthorPlugin(Configuration generalConfiguration, boolean sortLineAdded) {
	        this.configuration = generalConfiguration;
	        this.sortLineAdded = sortLineAdded;
	    }

	    Result processLog(List<Commit> gitLog) {
	        var result = new Result();
	        result.sortLineAdded = this.sortLineAdded;
	        for (var commit : gitLog) {
	        	// I decide to not count the line added/deleted from the merged commit
	        	// because the person who merges a branch, collects all the lines added and deleted 
	        	if(commit.mergedFrom == null) {
	        		var oldNbLines = result.linesPerAuthor.getOrDefault(commit.author, 0);
		        	Integer nbLines;
		        	if(sortLineAdded) nbLines = oldNbLines + commit.linesAdded;
		        	else nbLines = oldNbLines + commit.linesDeleted;
		        	result.linesPerAuthor.put(commit.author, nbLines);
	        	}
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
	        private final Map<String, Integer> linesPerAuthor = new HashMap<>();
	        private boolean sortLineAdded;
	        

	        Map<String, Integer> getLinesPerAuthor () {
	            return linesPerAuthor;
	        }

	        @Override
	        public String getResultAsString() {
	            return linesPerAuthor.toString();
	        }

	        @Override
	        public String getResultAsHtmlDiv() {
	            StringBuilder html = new StringBuilder("<div>Commits per author: <ul>");
	            for (var item : linesPerAuthor.entrySet()) {
	                html.append("<li>"  + (sortLineAdded ? "Lines Added by " : "Lines Deleted by ")).append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
	            }
	            html.append("</ul></div>");
	            return html.toString();
	        }
	    }
}
