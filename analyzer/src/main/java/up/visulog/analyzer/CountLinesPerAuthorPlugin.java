package up.visulog.analyzer;

import up.visulog.analyzer.CountCommitsPerAuthorPlugin.Result;
import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.webgen.WebGen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountLinesPerAuthorPlugin implements AnalyzerPlugin{
	 private final Configuration configuration;
	    private Result result;
	    
	    // true means sort lines added, false means sort lines deleted
	    private boolean sortLineAdded;
	    private boolean allBranchs;

	    public CountLinesPerAuthorPlugin(Configuration generalConfiguration, boolean sortLineAdded, boolean allBranchs) {
	        this.configuration = generalConfiguration;
	        this.sortLineAdded = sortLineAdded;
	        this.allBranchs = allBranchs;
	    }

	    Result processLog(List<Commit> gitLog) {
	        var result = new Result();
	        result.sortLineAdded = this.sortLineAdded;
	        Map<String,String>emailToName = new HashMap<String,String>();
	        for (var commit : gitLog) {
	        	// I decide to not count the line added/deleted from the merged commit
	        	// because the person who merges a branch, collects all the lines added and deleted 
	        	if(commit.mergedFrom == null) {
	        		String[] author = commit.author.split(" ");
	            	String email = author[author.length - 1];
	            	if(emailToName.get(email) == null) {
	            		emailToName.put(email, commit.author);
	            	}
	        		var oldNbLines = result.linesPerAuthor.getOrDefault(email, 0);
		        	Integer nbLines;
		        	if(sortLineAdded) nbLines = oldNbLines + commit.linesAdded;
		        	else nbLines = oldNbLines + commit.linesDeleted;
		        	result.linesPerAuthor.put(email, nbLines);
	        	}
	        	
	        }
	        for(var e : emailToName.entrySet()) {
             	int nbCommit = result.linesPerAuthor.remove(e.getKey());
             	result.linesPerAuthor.put(e.getValue(), nbCommit);
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
	        
	        @Override
	        public void getResultAsHtmlDiv(WebGen wg) {
	        	
	        }
	    }
}
