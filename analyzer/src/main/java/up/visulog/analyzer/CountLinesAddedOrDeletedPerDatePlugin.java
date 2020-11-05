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

public class CountLinesAddedOrDeletedPerDatePlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private static String howToSort = "months";
    //the plugin sort commits per months, this is a default value
    //the value change if the user wants the number of commits par weeks or per days
    private static boolean lines;
    //if the variable is true, the plugin count the lines added of commits
    //if the variable is false, the plugin count the lines deleted of commits
    private Result result;
    private boolean allBranchs;

  //if the user wants to count the number of lines added/deleted, the plugin use this constructor
    public CountLinesAddedOrDeletedPerDatePlugin(Configuration generalConfiguration, String howToSort, boolean lines, boolean allBranchs) {
        this.configuration = generalConfiguration;
        this.howToSort = howToSort;
        this.lines = lines;
        this.allBranchs = allBranchs;
    }

    //Sort commits per date
    static Result processLog(List<Commit> gitLog) {
    	var result = new Result();
    	result.setHowToSort(howToSort);
    	result.setLines(lines);
    	countLinesAddedOrDeletedPerDays(gitLog, result);
    	return result;
    }
     
    static void countLinesAddedOrDeletedPerDays(List<Commit> gitLog, Result result) {
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
	        	result.commitsPerDate.put(m, nbLines);
    		}
        }
    }
   
    //function which executes the plugin
    @Override
    public void run() {
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath(), allBranchs));
    }

    //function which returns the results of the analysis
    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }    
    
    
    
    
    static class Result implements AnalyzerPlugin.Result {
        private Map<LocalDate, Integer> commitsPerDate = new TreeMap<>();
        private String howToSort = "month";
        private boolean lines;
        
        //function which set the variable howToSort
        public void setHowToSort(String howToSort) {
        	this.howToSort = howToSort;
        }
        
        //function which set the variable lines
        public void setLines(boolean lines) {
        	this.lines = lines;
        }

        Map<LocalDate, Integer> getCommitsPerDate() {
            return commitsPerDate;
        }
        
        //function which sort the lines Added/Deleted per months
        public Map<LocalDate, Integer> resultPerMonths() {
        	Map<LocalDate, Integer> res = new TreeMap<>();
        	for (var date : commitsPerDate.entrySet()) {
        		LocalDate m = LocalDate.of(date.getKey().getYear(), date.getKey().getMonth(), 1);
            	var nb = res.getOrDefault(m, 0);
            	res.put(m, nb + date.getValue());            
            }
        	return res;
        }
        
        //function which sort the lines Added/Deleted per weeks
        public Map<String, Integer> resultPerWeeks() {
        	Map<String, Integer> res = new TreeMap<>();
        	for (var date : commitsPerDate.entrySet()) {
            	String m = Integer.toString(date.getKey().getYear()) + " Week " + Integer.toString(date.getKey().getDayOfYear()/7);
            	var nb = res.getOrDefault(m, 0);
            	res.put(m, nb + date.getValue());            
            }
        	return res;
        }
        
        
        @Override
        public String getResultAsString() {
            return commitsPerDate.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
        	StringBuilder html = new StringBuilder();
        	String s = ""; //I create this variable to change the output easily
        	
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
        		Map<String, Integer> res = resultPerWeeks();
        		for(var item : res.entrySet()) {
        			s += "<li> Week " + item.getKey().substring(item.getKey().length()-2, item.getKey().length()) + " (" + item.getKey().substring(0,4) + ") : " + item.getValue() + "</li>";
        		}
        	} else {
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

