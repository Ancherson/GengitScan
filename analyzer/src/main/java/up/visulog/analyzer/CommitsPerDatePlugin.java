package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.webgen.WebGen;

import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.time.Month;

public class CommitsPerDatePlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private static String howToSort = "months";
    //the plugin sort commits per months, this is a default value 
    private Result result;
    private boolean allBranches;
    // if allBranches is true, the plugin counts commits on all branches
    // if allBranches is false, the plugin counts commits on the remote branch 

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
    	
    	// sort the commits per Months and per Weeks
    	if(howToSort.equals("months")) {
    		Map<LocalDate, Integer> res = new TreeMap<>();
        	// change the key of the number of the commits
        	// for example, the key of the commits that were made in January 2020 will be January 1, 2020
        	for (var date : result.commitsPerDate.entrySet()) {
        		LocalDate m = LocalDate.of(date.getKey().getYear(), date.getKey().getMonth(), 1);
            	var nb = res.getOrDefault(m, 0);
            	res.put(m, nb + 1);
            }
        	// change the key and the value of result.commitsPerDate
        	result.commitsPerDate.clear();
        	result.commitsPerDate.putAll(res);
    	}
    	
    	// sort the commits per Months and per Weeks
    	else if(howToSort.equals("weeks")) {
    		Map<String, Integer> res = new TreeMap<>();
            // change the key of the number of the commits in String
            // for example, the key of the commits that were made in January 2020 will be "2020 Week 1"
            for (var date : result.commitsPerDate.entrySet()) {
               	String m = Integer.toString(date.getKey().getYear()) + " " + Integer.toString(date.getKey().getDayOfYear()/7);
               	var nb = res.getOrDefault(m, 0);
               	res.put(m, nb + 1);            
            }
         // change the key and the value of result.commitsPerWeeks
            result.commitsPerWeeks.putAll(res);
    	}
    	
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
        private Map<String, Integer> commitsPerWeeks = new TreeMap<>();
        // I chose a TreeMap<>() because the objects are sorted with the keys naturally
        // String = a -> z
        // int = 0,1,2...
        private String howToSort = "month";
        
        public void setHowToSort(String howToSort) {
        	this.howToSort = howToSort;
        }

        // get the variable commitsPerDate
        Map<LocalDate, Integer> getCommitsPerDate() {
            return commitsPerDate;
        }
        
        // return the results in String
        @Override
        public String getResultAsString() {
        	if(commitsPerWeeks.size() != 0) {
        		return commitsPerWeeks.toString();
        	}
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
        		for(var item : commitsPerWeeks.entrySet()) {
        			// Example of a item || Key : "2020 1" | Value : 30
        			s += "<li> Week " + item.getKey().substring(item.getKey().length()-2, item.getKey().length()) + " (" + item.getKey().substring(0,4) + ") : " + item.getValue() + "</li>";
        		}
        	} else {
        		for(var item : commitsPerDate.entrySet()) {
        			s += "<li>"  + item.getKey().getMonth().name() + " " + item.getKey().getYear() + ": " + item.getValue() + "</li>";
        		}
        	}
        	s += "</ul></div>";
        	html.append(s);
            return html.toString();
        }
        
        @Override
        public void getResultAsHtmlDiv(WebGen wg) {
            ArrayList<String> labels = new ArrayList<String>();
            ArrayList<Integer> data = new ArrayList<Integer>();

            if(this.howToSort.equals("days")) {
            	var labels0 = commitsPerDate.entrySet().iterator().next().getKey();
        		LocalDate cmp = labels0;
        		LocalDate expected = cmp;
        		for(var item : commitsPerDate.entrySet()) {
        			cmp = item.getKey();
        			if(cmp.equals(expected)) {
        				labels.add(item.getKey().getDayOfMonth() + " " + item.getKey().getMonth().name() + " " + item.getKey().getYear());
                        data.add(item.getValue());
        			} else {
        				while(!expected.equals(cmp)) {
        					labels.add(expected.getDayOfMonth() + " " + expected.getMonth().name() + " " + expected.getYear());
            				data.add(0);
            				expected = expected.plusDays(1);
        				}
        				labels.add(item.getKey().getDayOfMonth() + " " + item.getKey().getMonth().name() + " " + item.getKey().getYear());
                        data.add(item.getValue());
        			}
        			expected = expected.plusDays(1);
        		}
        	} else if(this.howToSort.equals("weeks")) {
        		var labels0 = commitsPerWeeks.entrySet().iterator().next().getKey();
        		int cmp = Integer.parseInt(labels0.substring(labels0.length()-2, labels0.length()));
        		int expected = cmp;
        		for(var item : commitsPerWeeks.entrySet()) {
        			cmp = Integer.parseInt(item.getKey().substring(item.getKey().length()-2, item.getKey().length()));
        			if(cmp == expected) {
        				labels.add("Week " + item.getKey().substring(item.getKey().length()-2, item.getKey().length()) + " (" + item.getKey().substring(0,4) + ")");
                        data.add(item.getValue());
        			} else {
        				while(expected != cmp) {
        					labels.add("Week " + expected + " (" + item.getKey().substring(0,4) + ")");
            				data.add(0);
            				expected++;
        				}
        				labels.add("Week " + item.getKey().substring(item.getKey().length()-2, item.getKey().length()) + " (" + item.getKey().substring(0,4) + ")");
                        data.add(item.getValue());
        				expected++;
        			}
        			expected++;
        		}
        	} else {
        		var labels0 = commitsPerDate.entrySet().iterator().next().getKey();
        		LocalDate cmp = labels0;
        		LocalDate expected = cmp;
        		for(var item : commitsPerDate.entrySet()) {
        			cmp = item.getKey();
        			if(cmp.equals(expected)) {
        				labels.add(item.getKey().getMonth().name() + " " + item.getKey().getYear());
                        data.add(item.getValue());
        			} else {
        				while(!expected.equals(cmp)) {
        					labels.add(item.getKey().getMonth().name() + " " + item.getKey().getYear());
            				data.add(0);
            				expected = expected.plusMonths(1);
        				}
        				labels.add(item.getKey().getMonth().name() + " " + item.getKey().getYear());
                        data.add(item.getValue());
        			}
        			expected = expected.plusMonths(1);
        		}
            }
            
            wg.addChart("line", "Number of commits", labels, data);
        }
    }
}
