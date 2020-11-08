package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.time.Month;

public class CountLinesPerAuthorPerDatePlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private static String howToSort = "months";
    // the plugin sort commits per months, this is a default value
    // the value change if the user wants the number of commits par weeks or per days
    private static boolean lines;
    // if the variable is true, the plugin count the lines added of commits
    // if the variable is false, the plugin count the lines deleted of commits
    private Result result;
    private boolean allBranchs;
    // if the variable is true, the plugin count the lines for all branchs
    // if the variable is false, the plugin count the lines for the branch where the user is
    
    
    // Constructor
    public CountLinesPerAuthorPerDatePlugin(Configuration generalConfiguration, String howToSort, boolean lines, boolean allBranchs) {
        this.configuration = generalConfiguration;
        this.howToSort = howToSort;
        this.lines = lines;
        this.allBranchs = allBranchs;
    }
    
    // when the program starts, the first function is called, it starts the plugin
    static Result processLog(List<Commit> gitLog) {
    	var result = new Result();
    	result.setHowToSort(howToSort);
    	result.setLines(lines);	
    	Map<LocalDate, List<Commit>> commitsPerDate = sortCommitsPerDays(gitLog);
    	for(var commitsPerDays : commitsPerDate.entrySet()) {
    		LocalDate m = commitsPerDays.getKey();
    		Map<String, Integer> linesAddedDeleted = linesPerAuthor(commitsPerDays.getValue());
    		result.linesPerAuthorPerDate.put(m, linesAddedDeleted);
    	}
    	return result;
    }
    
    // to get the list of commits on a specific day --> Returns Map<LocalDate, List<Commit>>
    static Map<LocalDate, List<Commit>> sortCommitsPerDays(List<Commit> gitLog) {
    	Map<LocalDate, List<Commit>> res = new TreeMap<>();
    	for (var commit : gitLog) {
    		LocalDate m = commit.date.toLocalDate();
	        res.put(m, makeCommitsList(m, gitLog));
        }
    	return res;
    }
    
    static List<Commit> makeCommitsList(LocalDate m, List<Commit> gitLog) {
    	List<Commit> list = new LinkedList<>();
    	for (var commit : gitLog) {
    		LocalDate c = commit.date.toLocalDate();
    		if(c.equals(m)) {
    			list.add(commit);
    		}
        }
    	return list;
    }
    
    // to sort commits by author, it adds the added or deleted lines
    static Map<String, Integer> linesPerAuthor(List<Commit> listCommits) {
    	Map<String, Integer> res = new HashMap<>();
        for (var commit : listCommits) {
    		//The plugin don't count the line added/deleted from the merged commit
        	//because the lines will added and deleted twice
    		if(commit.mergedFrom == null) {
	        	String m = commit.author;
	        	var oldNbLines = res.getOrDefault(m, 0);
	        	int nbLines = 0;
	        	if(lines) {
	        		nbLines = oldNbLines + commit.linesAdded;
	        	} else {
	        		nbLines = oldNbLines + commit.linesDeleted;
	        	}
	        	res.put(m, nbLines);
    		}
        }
        return res;
    }
    
    // function which executes the plugin
    @Override
    public void run() {
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath(), allBranchs));
    }

    // function which returns the results of the analysis
    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }    
    
    
    static class Result implements AnalyzerPlugin.Result {
        private Map<LocalDate, Map<String, Integer>> linesPerAuthorPerDate = new TreeMap<>();
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
        
        // if you want to test the plugin, here s a function to print the map linesPerAuthorPerDate
        public void afficher() {
        	for(var commits : linesPerAuthorPerDate.entrySet()) {
        		System.out.println(commits.getKey());
        		Map<String, Integer> lines = commits.getValue();
        		for(var l : lines.entrySet()) {
            		System.out.println(l.getKey() + " : " + l.getValue());
            	}
        		System.out.println();        		
        	}
        }
        
        // get linesPerAuthorPerDate
        Map<LocalDate, Map<String, Integer>> getLinesPerAuthorPerDate() {
            return linesPerAuthorPerDate;
        }
        
        //function which sort the lines Added/Deleted per months
        public Map<LocalDate, Map<String, Integer>> resultPerMonths() {
        	Map<LocalDate, Map<String, Integer>> res = new TreeMap<>();
        	for(var date : linesPerAuthorPerDate.entrySet()) {
        		LocalDate m = LocalDate.of(date.getKey().getYear(), date.getKey().getMonth(), 1);		
        		Map<String, Integer> res2 = authorsAndMonths(m);
        		res2 = sameAuthor(res2);
        		res.put(m, res2);
        	}
        	return res;
        }
        
        public Map<String, Integer> authorsAndMonths(LocalDate months) {
        	Map<String, Integer> res = new HashMap<>();
        	for(var date : linesPerAuthorPerDate.entrySet()) {
        		LocalDate m = LocalDate.of(date.getKey().getYear(), date.getKey().getMonth(), 1);
        		if(months.equals(m)) {
        			for(var lines : date.getValue().entrySet()) {
        				String a = lines.getKey();
                    	var nb = res.getOrDefault(a, 0);
                    	res.put(a, nb + lines.getValue());
        			}
        		}
        	}
        	return res;
        }
        
     // to sort commits by author, it adds the added or deleted lines
        static Map<String, Integer> sameAuthor(Map<String, Integer> listCommits) {
        	Map<String, Integer> res = new HashMap<>();
            Map<String,String> emailToName = new HashMap<String,String>();
            for (var commit : listCommits.entrySet()) {
            	String[] author = commit.getKey().split(" ");
                String email = author[author.length-1];
                if(emailToName.get(email) == null) {
                	emailToName.put(email, commit.getKey());
                }
            	var lines = res.getOrDefault(email, 0);
    	    	res.put(email, lines + commit.getValue());
            }
            for(var e : emailToName.entrySet()) {
             	int nbCommit = res.remove(e.getKey());
             	res.put(e.getValue(), nbCommit);
             }
            return res;
        }
        
        // function which sort the lines Added/Deleted per weeks
        public Map<String, Map<String, Integer>> resultPerWeeks() {
        	Map<String, Map<String, Integer>> res = new TreeMap<>();
        	for(var date : linesPerAuthorPerDate.entrySet()) {
        		String m = Integer.toString(date.getKey().getYear()) + " Week " + Integer.toString(date.getKey().getDayOfYear()/7);	
        		Map<String, Integer> res2 = authorAndWeeks(m);
        		res2 = sameAuthor(res2);
        		res.put(m, res2);
        	}
        	return res;
        }
        
        public Map<String, Integer> authorAndWeeks(String week) {
        	Map<String, Integer> res = new HashMap<>();
        	for(var date : linesPerAuthorPerDate.entrySet()) {
        		String m = Integer.toString(date.getKey().getYear()) + " Week " + Integer.toString(date.getKey().getDayOfYear()/7);
        		if(week.equals(m)) {
        			for(var lines : date.getValue().entrySet()) {
        				String a = lines.getKey();
                    	var nb = res.getOrDefault(a, 0);
                    	res.put(a, nb + lines.getValue());
        			}
        		}
        	}
        	return res;
        }
        
        @Override
        public String getResultAsString() {
            return linesPerAuthorPerDate.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
//        	this.afficher();
        	StringBuilder html = new StringBuilder();
        	String s = ""; //I create this variable to change the output easily
        	
        	//check if the user wants the number of the lines added/deleted or the number of commits
        	if(lines) {
        		s += "<div>Number of lines added of commits per " + this.howToSort + "and per author : <ul><br>";
        	} else {
        		s += "<div>Number of lines deleted per " + this.howToSort + "and per author : <ul><br>";
        	}
        	
        	//display the commits (or lines added/deleted) by the way of sorting
        	if(this.howToSort.equals("days")) {
        		for(var item : linesPerAuthorPerDate.entrySet()) {
        			s += "<ul>" + item.getKey().getDayOfMonth() + " " + item.getKey().getMonth().name() +  " " + item.getKey().getYear() + "<br>";
        			Map<String, Integer> lines = item.getValue();
            		for(var l : lines.entrySet()) {
            			s += "<li>" + l.getKey() + " : " + l.getValue() + "</li><br>";
            		}
            		s+= "</ul><br>";
        		}
        	} else if(this.howToSort.equals("weeks")) {
        		Map<String, Map<String, Integer>> res = resultPerWeeks();
        		for(var item : res.entrySet()) {
        			s += "<ul>Week " + item.getKey().substring(item.getKey().length()-2, item.getKey().length()) + " (" + item.getKey().substring(0,4) + ")<br>";
        			Map<String, Integer> lines = item.getValue();
            		for(var l : lines.entrySet()) {
            			s += "<li>" + l.getKey() + " : " + l.getValue() + "</li><br>";
            		}
            		s+= "</ul><br>";
        		}
        	} else {
        		Map<LocalDate, Map<String, Integer>> res = resultPerMonths();
        		for(var item : res.entrySet()) {
        			s += "<ul>" + item.getKey().getMonth().name() + " " + item.getKey().getYear() + "<br>";
        			Map<String, Integer> lines = item.getValue();
            		for(var l : lines.entrySet()) {
            			s += "<li>" + l.getKey() + " : " + l.getValue() + "</li><br>";
            		}
            		s+= "</ul><br>";
        		}
        	}
        	s += "</ul></div>";
        	html.append(s);
            return html.toString();
        }
    }
}

