package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    	if(howToSort == "months") { //Commits per Months
	        sortPerMonth(gitLog, result);
        }
    	if(howToSort == "days") { //Commits per Days    		
    		sortPerDays(gitLog, result);
        }
    	if(howToSort == "weeks") { //Commits per Weeks
    		sortPerWeeks(gitLog, result);
        }
    	return result;
    }
    
    //function fills the result variable with the way of sorting
    static void sortPerMonth(List<Commit> gitLog, Result result) {
    	for (var commit : gitLog) {
        	String m = commit.date.getMonth().name();
        	var nb = result.commitsPerDate.getOrDefault(m, 0);
        	LocalDateTime datetmp = commit.date;
        	if(commit.date.getMonth() == datetmp.getMonth()) {
        		result.commitsPerDate.put(m, nb + 1);
        	}            
        }
    }
    
    static void sortPerDays(List<Commit> gitLog, Result result) {
    	for (var commit : gitLog) {
        	String m = Integer.toString(commit.date.getDayOfMonth()) + " " + commit.date.getMonth().name();
        	var nb = result.commitsPerDate.getOrDefault(m, 0);
        	LocalDateTime datetmp = commit.date;
        	if(commit.date.getDayOfMonth() == datetmp.getDayOfMonth()) {
        		result.commitsPerDate.put(m, nb + 1);
        	}            
        }
    }

	static void sortPerWeeks(List<Commit> gitLog, Result result) {
		for (var commit : gitLog) {
        	String m = Integer.toString(getNumberOfWeek(commit.date));
        	var nb = result.commitsPerDate.getOrDefault(m, 0);
        	LocalDateTime datetmp = commit.date;
        	if(getNumberOfWeek(commit.date) == getNumberOfWeek(datetmp)) {
        		result.commitsPerDate.put(m, nb + 1);
        	}
        }
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
   
 
    
    
    
    static class Result implements AnalyzerPlugin.Result {
        private Map<String, Integer> commitsPerDate = new HashMap<>();
        private String howToSort = "month";
        
        public void setHowToSort(String howToSort) {
        	this.howToSort = howToSort;
        }

        Map<String, Integer> getCommitsPerDate() {
            return commitsPerDate;
        }
        
        // strange function that still sorts the months... ?????
        //to fix
        public void sort() {
        	String[] months = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
        	
        	if(this.howToSort.equals("months")) { //sort per months
        		/*int[] tab = new int[commitsPerDate.size()];
        		int indice = 0;
        		for(var item : commitsPerDate.entrySet()) {
        			String key = item.getKey();
        			for(int i=0; i<months.length; i++) {
        				if(key.equals(months[i])) {
        					tab[indice] = i;
        					indice++;
        				}
        			}
        		}
        		boolean change = false;
        		do {
        			change = false;
        			for(int i=0; i<tab.length-1; i++) {
        				if(tab[i]>tab[i+1]) {
        					int tmp = tab[i];
        					tab[i] = tab[i+1];
        					tab[i+1] = tmp;
        					change = true;
        				}
        			}
        		} while(change);*/
        		Map<String, Integer> copy = new HashMap<>();
        		copy.putAll(commitsPerDate); //copie de la map
        		commitsPerDate.clear(); //on efface la map pour la re remplir
        
        		/*for(var item : copy.entrySet()) {
        			System.out.println(item.getKey() + ": " + item.getValue());
        		}
        		
        		for(int i=0; i<tab.length; i++) {
        			indice = tab[i];
        			commitsPerDate.put(months[indice], copy.get(months[indice]));
        			System.out.println();
        			for(var item : commitsPerDate.entrySet()) {
            			System.out.println(item.getKey());
            		}
        			System.out.println();
        		}*/
        		commitsPerDate = copy;
        		
        		
        	}	
        }
        
        
        
        

        @Override
        public String getResultAsString() {
            return commitsPerDate.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
        	sort();
            StringBuilder html = new StringBuilder("<div>Number of Commits per " + this.howToSort + ": <ul>");
            for (var item : commitsPerDate.entrySet()) {
            	if(this.howToSort.equals("weeks")) {
            		html.append("<li>Week ").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            	} else {
            		html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            	}
            }
            html.append("</ul></div>");
            return html.toString();
        }
    }
}

