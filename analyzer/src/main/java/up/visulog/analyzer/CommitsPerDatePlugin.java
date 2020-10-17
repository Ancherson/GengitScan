package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class CommitsPerDatePlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private static String howToSort= "months"; //the plugin sort commits per months, this is a default value 
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
    	if(howToSort == "months") { //Commits per Months
	        for (var commit : gitLog) {
	        	String m = commit.date.getMonth().name();
	        	var nb = result.commitsPerDate.getOrDefault(m, 0);
	        	LocalDateTime datetmp = commit.date;
	        	if(commit.date.getMonth() == datetmp.getMonth()) {
	        		result.commitsPerDate.put(m, nb + 1);
	        	}            
	        }
	        return result;
        }
    	if(howToSort == "days") { //Commits per Days
	        for (var commit : gitLog) {
	        	String m = Integer.toString(commit.date.getDayOfMonth()) + " " + commit.date.getMonth().name();
	        	var nb = result.commitsPerDate.getOrDefault(m, 0);
	        	LocalDateTime datetmp = commit.date;
	        	if(commit.date.getDayOfMonth() == datetmp.getDayOfMonth()) {
	        		result.commitsPerDate.put(m, nb + 1);
	        	}            
	        }
	        return result;
        }
    	if(howToSort == "weeks") { //Commits per Weeks
	        for (var commit : gitLog) {
	        	String m = "Week " + Integer.toString(getNumberOfWeek(commit.date));
	        	var nb = result.commitsPerDate.getOrDefault(m, 0);
	        	LocalDateTime datetmp = commit.date;
	        	if(getNumberOfWeek(commit.date) == getNumberOfWeek(datetmp)) {
	        		result.commitsPerDate.put(m, nb + 1);
	        	}            
	        }
	        return result;
        }
    	
    	return result;
    }
    
    //function which returns the number of the week
    static int getNumberOfWeek(LocalDateTime date) {
    	return date.getDayOfYear()/7;
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
    
    //
    
    
    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> commitsPerDate = new HashMap<>();

        Map<String, Integer> getCommitsPerDate() {
            return commitsPerDate;
        }

        @Override
        public String getResultAsString() {
            return commitsPerDate.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Number of Commits per Days/Month: <ul>");
            for (var item : commitsPerDate.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
    }
}

