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

public class CommitsPerDatePlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private static String howToSort = "months"; //the plugin sort commits per months, this is a default value 
    private Result result;

    
    public CommitsPerDatePlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }
    
    //if you want to change the kind of sort, you can use other sort of spell
    public CommitsPerDatePlugin(Configuration generalConfiguration, String howToSort) {
        this.configuration = generalConfiguration;
        this.howToSort = howToSort;
    }

    //Sort commits per month and per date
    static Result processLog(List<Commit> gitLog) {
    	var result = new Result();
    	result.setHowToSort(howToSort);
    	sortPerDays(gitLog, result);
    	return result;
    }
    
    //function fills the result variable with the way of sorting    
    static void sortPerDays(List<Commit> gitLog, Result result) {
    	for (var commit : gitLog) {
    		LocalDate m = commit.date.toLocalDate();
        	var nb = result.commitsPerDate.getOrDefault(m, 0);
        	result.commitsPerDate.put(m, nb + 1);
        }
    }

    //function which executes the plugin
    @Override
    public void run() {
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));
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
        
        public void setHowToSort(String howToSort) {
        	this.howToSort = howToSort;
        }

        Map<LocalDate, Integer> getCommitsPerDate() {
            return commitsPerDate;
        }
        
        public Map<Month, Integer> resultPerMonths() {
        	Map<Month, Integer> res = new TreeMap<>();
        	for (var date : commitsPerDate.entrySet()) {
            	Month m = date.getKey().getMonth();
            	var nb = res.getOrDefault(m, 0);
            	res.put(m, nb + 1);            
            }
        	return res;
        }
        
        public Map<Integer, Integer> resultPerWeeks() {
        	Map<Integer, Integer> res = new TreeMap<>();
        	for (var date : commitsPerDate.entrySet()) {
            	int m = date.getKey().getDayOfYear()/7;
            	var nb = res.getOrDefault(m, 0);
            	res.put(m, nb + 1);            
            }
        	return res;
        }

       
        
        @Override
        public String getResultAsString() {
            return commitsPerDate.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
        	StringBuilder html = new StringBuilder("<div>Number of Commits per " + this.howToSort + ": <ul>");
        	String s = "";
        	
        	if(this.howToSort.equals("days")) {
        		for(var item : commitsPerDate.entrySet()) {
        			s += "<li>" + item.getKey().getDayOfMonth() + " " + item.getKey().getMonth().name() + ": " + item.getValue() + "</li>";
        		}
        	} else if(this.howToSort.equals("weeks")) {
        		Map<Integer, Integer> res = resultPerWeeks();
        		for(var item : res.entrySet()) {
        			s += "<li> Week " + item.getKey() + " : " + item.getValue() + "</li>";
        		}
        	} else {
        		Map<Month, Integer> res = resultPerMonths();
        		for(var item : res.entrySet()) {
        			s += "<li>" + item.getKey() + ": " + item.getValue() + "</li>";
        		}
        	}
        	s += "</ul></div>";
        	html.append(s);
            return html.toString();
        }
    }
}

