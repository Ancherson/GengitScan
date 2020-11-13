package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.time.Month;
import java.util.LinkedList;

public class CommitsPerDatePlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private static String howToSort = "months";
    //the plugin sort commits per months, this is a default value 
    private Result result;
    private boolean allBranches;
    // if allBranches is true, the plugin counts commits on all branches
    // ifallBranches is false, the plugin counts commits on the remote branch 

    // constructor
    public CommitsPerDatePlugin(Configuration generalConfiguration, String howToSort, boolean allBranches) {
        this.configuration = generalConfiguration;
        this.howToSort = howToSort;
        this.allBranches = allBranches;
    }

    // sort commits per month and per date
    static Result processLog(List<Commit> gitLog) {
    	var result = new Result();
    	// change the values of the object Result
    	result.setHowToSort(howToSort);
    	sortPerDays(gitLog, result);
    	return result;
    }
    
    // function fills the result variable with the way of sorting    
    static void sortPerDays(List<Commit> gitLog, Result result) {
    	// browse the list of commits and count them according to the date of the commit
    	for (var commit : gitLog) {
    		LocalDate m = commit.date.toLocalDate();
        	var nb = result.commitsPerDate.getOrDefault(m, 0);
        	// enter the values in the map
        	result.commitsPerDate.put(m, nb + 1);
        }
    }

    // function which executes the plugin
    @Override
    public void run() {
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath(), allBranches));
    }

    // function which returns the results of the analysis
    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }    
    
    
    static class Result implements AnalyzerPlugin.Result {
        private Map<LocalDate, Integer> commitsPerDate = new TreeMap<>();
        // I chose a TreeMap<>() because the objects are sorted with the keys naturally
        // String = a -> z
        // int = 0,1,2...
        private String howToSort = "month";
        
        // constructor
        public void setHowToSort(String howToSort) {
        	this.howToSort = howToSort;
        }

        // get the variable commitsPerDate
        Map<LocalDate, Integer> getCommitsPerDate() {
            return commitsPerDate;
        }
        
        // function which sort the commits per Months
        public Map<LocalDate, Integer> resultPerMonths() {
        	Map<LocalDate, Integer> res = new TreeMap<>();
        	// change the key of the number of the commits
        	// for example, the key of the commits that were made in January 2020 will be January 1, 2020
        	for (var date : commitsPerDate.entrySet()) {
        		LocalDate m = LocalDate.of(date.getKey().getYear(), date.getKey().getMonth(), 1);
            	var nb = res.getOrDefault(m, 0);
            	res.put(m, nb + 1);            
            }
        	return res;
        }
        
        //function which sort the commits per Weeks
        public Map<String, Integer> resultPerWeeks() {
        	Map<String, Integer> res = new TreeMap<>();
        	// change the key of the number of the commits in String
        	// for example, the key of the commits that were made in January 2020 will be "2020 Week 1"
        	for (var date : commitsPerDate.entrySet()) {
            	String m = Integer.toString(date.getKey().getYear()) + " " + Integer.toString(date.getKey().getDayOfYear()/7);
            	var nb = res.getOrDefault(m, 0);
            	res.put(m, nb + 1);            
            }
        	return res;
        }
        
        // return the results in String
        @Override
        public String getResultAsString() {
            return commitsPerDate.toString();
        }

        // return the results with HTML tags
        @Override
        public String getResultAsHtmlDiv() {
        	StringBuilder html = new StringBuilder("<div>Number of Commits per " + this.howToSort + ": <ul>");
        	// the variable s is filled according to the way of sorting
        	String s = "";
        	
        	// test the content of the variable howToSort
        	if(this.howToSort.equals("days")) {
        		for(var item : commitsPerDate.entrySet()) {
        			s += "<li>" + item.getKey().getDayOfMonth() + " " + item.getKey().getMonth().name() + " " + item.getKey().getYear() + ": " + item.getValue() + "</li>";
        		}
        	} else if(this.howToSort.equals("weeks")) {
        		// initialize a new Map sorted per weeks
        		Map<String, Integer> res = resultPerWeeks();
        		for(var item : res.entrySet()) {
        			// Example of a item || Key : "2020 Week 1" | Value : 30
        			s += "<li> Week " + item.getKey().substring(item.getKey().length()-2, item.getKey().length()) + " (" + item.getKey().substring(0,4) + ") : " + item.getValue() + "</li>";
        		}
        	} else {
        		// initialize a new Map sorted per months
        		Map<LocalDate, Integer> res = resultPerMonths();
        		for(var item : res.entrySet()) {
        			s += "<li>"  + item.getKey().getMonth().name() + " " + item.getKey().getYear() + ": " + item.getValue() + "</li>";
        		}
        	}
        	s += "</ul></div>";
        	html.append(s);
            return html.toString();
        }
    }
}

