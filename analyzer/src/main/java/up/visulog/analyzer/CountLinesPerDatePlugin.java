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

public class CountLinesPerDatePlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private static String howToSort = "months";
    //the plugin sort commits per months, this is a default value
    //the value change if the user wants the number of commits par weeks or per days
    private static boolean lines;
    //if the variable is true, the plugin count the lines added of commits
    //if the variable is false, the plugin count the lines deleted of commits
    private Result result;
    private boolean allBranches;
    // if allBranches is true, the plugin counts commits on all branches
    // if allBranches is false, the plugin counts commits on the remote branch
    

    // constructor
    public CountLinesPerDatePlugin(Configuration generalConfiguration, String howToSort, boolean lines, boolean allBranches) {
        this.configuration = generalConfiguration;
        this.howToSort = howToSort;
        this.lines = lines;
        this.allBranches = allBranches;
    }

    // sort commits per date
    static Result processLog(List<Commit> gitLog) {
    	var result = new Result();
    	// change the values of the object Result
    	result.setHowToSort(howToSort);
    	result.setLines(lines);
    	countLinesAddedOrDeletedPerDays(gitLog, result);
    	
    	// sort the commits per Months and per Weeks
    	if(howToSort.equals("months")) {
    		Map<LocalDate, Integer> res = new TreeMap<>();
        	for (var date : result.commitsPerDate.entrySet()) {
        		// change the key of the number of the commits
            	// for example, the key of the commits that were made in January 2020 will be January 1, 2020
        		LocalDate m = LocalDate.of(date.getKey().getYear(), date.getKey().getMonth(), 1);
            	var nb = res.getOrDefault(m, 0);
            	res.put(m, nb + date.getValue());            
            }
        	// change the key and the value of result.commitsPerDate
        	result.commitsPerDate.clear();
        	result.commitsPerDate.putAll(res);
    	}
    	
    	// sort the commits per Months and per Weeks
    	else if(howToSort.equals("weeks")) {
    		Map<String, Integer> res = new TreeMap<>();
        	for (var date : result.commitsPerDate.entrySet()) {
        		// change the key of the number of the commits in String
            	// for example, the key of the commits that were made in January 2020 will be "2020 Week 1"
            	String m = Integer.toString(date.getKey().getYear()) + " " + Integer.toString(date.getKey().getDayOfYear()/7);
            	var nb = res.getOrDefault(m, 0);
            	res.put(m, nb + date.getValue());            
            }
         // change the key and the value of result.commitsPerWeeks
            result.commitsPerWeeks.putAll(res);
    	}
    	return result;
    }
    
    // function fills the result variable with the way of sorting  
    static void countLinesAddedOrDeletedPerDays(List<Commit> gitLog, Result result) {
    	// browse the list of commits and count them according to the date of the commit
    	for (var commit : gitLog) {
    		//The plugin don't count the line added/deleted from the merged commit
        	//because the lines will added and deleted twice
    		if(commit.mergedFrom == null) {
	        	LocalDate m = commit.date.toLocalDate();
	        	var oldNbLines = result.commitsPerDate.getOrDefault(m, 0);
	        	int nbLines = 0;
	        	if(lines) {
	        		nbLines = oldNbLines + commit.linesAdded;
	        	} else {
	        		nbLines = oldNbLines + commit.linesDeleted;
	        	}
	        	// enter the values in the map
	        	result.commitsPerDate.put(m, nbLines);
    		}
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
        private Map<String, Integer> commitsPerWeeks = new TreeMap<>();
        // I chose a TreeMap<>() because the objects are sorted with the keys naturally
        // String = a -> z
        // int = 0,1,2...
        private String howToSort = "month";
        private boolean lines;
        
        // function which set the variable howToSort
        public void setHowToSort(String howToSort) {
        	this.howToSort = howToSort;
        }
        
        // function which set the variable lines
        public void setLines(boolean lines) {
        	this.lines = lines;
        }

        Map<LocalDate, Integer> getCommitsPerDate() {
            return commitsPerDate;
        }
        
        // return the results in String
        @Override
        public String getResultAsString() {
            return commitsPerDate.toString();
        }

        // return the results with HTML tags
        @Override
        public String getResultAsHtmlDiv() {
        	StringBuilder html = new StringBuilder();
        	String s = "";
        	// the variable s is filled according to the way of sorting
        	
        	//check if the user wants the number of the lines added/deleted or the number of commits
        	if(lines) {
        		s += "<div>Number of lines added of commits per " + this.howToSort + ": <ul>";
        	} else {
        		s += "<div>Number of lines deleted per " + this.howToSort + ": <ul>";
        	}
        	
        	//display the commits (or lines added/deleted) by the way of sorting
        	if(this.howToSort.equals("days")) {
        		for(var item : commitsPerDate.entrySet()) {
        			s += "<li>" + item.getKey().getDayOfMonth() + " " + item.getKey().getMonth().name() +  " " + item.getKey().getYear() + ": " + item.getValue() + "</li>";
        		}
        	} else if(this.howToSort.equals("weeks")) {
        		for(var item : commitsPerWeeks.entrySet()) {
        			// Example of a item || Key : "2020 1" | Value : 30
        			s += "<li> Week " + item.getKey().substring(item.getKey().length()-2, item.getKey().length()) + " (" + item.getKey().substring(0,4) + ") : " + item.getValue() + "</li>";
        		}
        	} else {
        		for(var item : commitsPerDate.entrySet()) {
        			s += "<li>"  + item.getKey().getMonth().name() + " (" + item.getKey().getYear() + ") : " + item.getValue() + "</li>";
        		}
        	}
        	s += "</ul></div>";
        	html.append(s);
            return html.toString();
        }
    }
}

