package up.visulog.analyzer;

import up.visulog.analyzer.CountCommitsPerAuthorPlugin.Result;
import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.webgen.WebGen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class CountContributionPlugin implements AnalyzerPlugin{
	 private final Configuration configuration;
	    private Result result;

	    public CountContributionPlugin(Configuration generalConfiguration) {
	        this.configuration = generalConfiguration;
	    }
	    
	    public HashMap<String, String> getName() {
	    	List<Commit> commits = Commit.parseLogFromCommand(configuration.getGitPath(), false);
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

	    public Result processLog(HashMap<String, Integer>LinesPerEmail, HashMap<String, String>emailToName) {
	        var result = new Result();
	        ;
	        double tot = 0;
	        for(var ass : emailToName.entrySet()) {
	        	double lines;
	        	Integer l = LinesPerEmail.get(ass.getKey());
	        	if(l == null) lines = 0;
	        	else lines = l;
	        	result.contributionPerAuthor.put(ass.getValue(), lines);
	        	tot += lines;
	        }
	        
	        
	        for(var ass : result.contributionPerAuthor.entrySet()) {
	        	double percent = (ass.getValue() / tot) * 100;
	        	ass.setValue(percent);
	        }
	       
	        //System.out.println(LinesPerName);
	        
	        return result;
	    }

	    @Override
	    public void run() {
	        result = processLog(Commit.countLinesContribution(configuration.getGitPath()), getName());
	    }

	    @Override
	    public Result getResult() {
	        if (result == null) run();
	        return result;
	    }

	    static class Result implements AnalyzerPlugin.Result {
	        private final Map<String,Double> contributionPerAuthor = new HashMap<>();

	        Map<String, Double> getCommitsPerAuthor() {
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
	            	String percent;
	            	Double value = item.getValue();
	            	if(value == 0.0)
	            		percent = String.valueOf(value).substring(0,3);
	            	else if(value < 10)
	            		percent = String.valueOf(value).substring(0,4);
	            	else 
	            		percent = String.valueOf(value).substring(0,5);
	            	percent += " %";
	            	
	                html.append("<li>").append(item.getKey()).append(": ").append(percent).append("</li>");
	            }
	            html.append("</ul></div>");
	            return html.toString();
	        }
	        
	        @Override
	        public void getResultAsHtmlDiv(WebGen wg) {
				ArrayList<String> authorOfCommit = new ArrayList<String>();
				ArrayList<Double> percentageOfContribution = new ArrayList<Double>();
				for(var data : getCommitsPerAuthor().entrySet()){
					String[] nameTab = data.getKey().split(" ");
	            	String name = "";
	            	for(int i=0; i<nameTab.length-1; i++) {
	            		name += nameTab[i] + " ";
	            	}
					authorOfCommit.add(name);
					percentageOfContribution.add(data.getValue());
				}
				wg.addChartDouble("pie","Countribution",authorOfCommit,percentageOfContribution);
	        }
	    }
	    
}
