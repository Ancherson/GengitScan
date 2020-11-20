package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.webgen.WebGen;

import java.util.ArrayList;
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
    private String howToSort = "months";
    // the plugin sort commits per months, this is a default value
    // the value change if the user wants the number of commits par weeks or per days
    private boolean lines;
    // if the variable is true, the plugin count the lines added of commits
    // if the variable is false, the plugin count the lines deleted of commits
    private Result result;
    private boolean allBranches;
    // if the variable is true, the plugin count the lines for all branches
    // if the variable is false, the plugin count the lines for the branch where the user is
    
    // Constructor
    public CountLinesPerAuthorPerDatePlugin(Configuration generalConfiguration, String howToSort, boolean lines, boolean allBranches) {
        this.configuration = generalConfiguration;
        this.howToSort = howToSort;
        this.lines = lines;
        this.allBranches = allBranches;
    }
    
    // when the program starts, the first function is called, it starts the plugin
    public Result processLog(List<Commit> gitLog) {
    	List<Commit> gitLog2 = sameAuthor2(gitLog);
    	var result = new Result();
    	// change the values of the object Result
    	result.setHowToSort(howToSort);
    	result.setLines(lines);
    	
    	// first, we find the commits sorting per the days
    	Map<LocalDate, List<Commit>> commitsPerDate = sortCommitsPerDays(gitLog2);
    	
    	// then, we create a new Map with a Key which associates an author and his number of added/deleted lines
    	for(var commitsPerDays : commitsPerDate.entrySet()) {
    		LocalDate m = commitsPerDays.getKey();
    		Map<String, Integer> linesAddedDeleted = linesPerAuthor(commitsPerDays.getValue());
    		result.linesPerAuthorPerDate.put(m, linesAddedDeleted);
    	}
    	
    	// now, the linesPerAuthorPerDate attribute associates the list of authors (and its number of added/deleted lines) per day
    	// we just have to sort them by month or week
    	
    	// sort the commits per Months
    	if(howToSort.equals("months")) {
    		// first, we create a new Map
    		Map<LocalDate, Map<String, Integer>> res = new TreeMap<>();
    		// we sort the number of added/deleted lines per months
        	for(var date : result.linesPerAuthorPerDate.entrySet()) {
        		LocalDate m = LocalDate.of(date.getKey().getYear(), date.getKey().getMonth(), 1);
        		// we create a new Map which gives the right number of lines to the authors
        		Map<String, Integer> res2 = authorsAndMonths(m, result);
        		res.put(m, res2);
        	}
        	
        	// change the key and the value of result.commitsPerDate
        	result.linesPerAuthorPerDate.clear();
        	result.linesPerAuthorPerDate.putAll(res);
    	}
    	
    	// sort the commits per Weeks
    	else if(howToSort.equals("weeks")) {
    		// first, we create a new Map
    		Map<String, Map<String, Integer>> res = new TreeMap<>();
    		// we sort the number of added/deleted lines per weeks
        	for(var date : result.linesPerAuthorPerDate.entrySet()) {
        		String m = Integer.toString(date.getKey().getYear()) + " Week " + Integer.toString(date.getKey().getDayOfYear()/7);
        		// we create a new Map which gives the right number of lines to the authors
        		Map<String, Integer> res2 = authorAndWeeks(m, result);
        		res.put(m, res2);
        	}
    		
         // change the key and the value of result.commitsPerWeeks
            result.linesPerAuthorPerWeeks.putAll(res);
    	}
    	return result;
    }
    
    // to get the list of commits on a specific day --> Returns Map<LocalDate, List<Commit>>
    public Map<LocalDate, List<Commit>> sortCommitsPerDays(List<Commit> gitLog) {
    	Map<LocalDate, List<Commit>> res = new TreeMap<>();
    	for (var commit : gitLog) {
    		LocalDate m = commit.date.toLocalDate();
	        res.put(m, makeCommitsList(m, gitLog));
        }
    	return res;
    }
    
    public List<Commit> makeCommitsList(LocalDate m, List<Commit> gitLog) {
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
    public Map<String, Integer> linesPerAuthor(List<Commit> listCommits) {
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
    
    // to sort the lines by authors and per months
    public Map<String, Integer> authorsAndMonths(LocalDate months, Result r) {
    	Map<String, Integer> res = new HashMap<>();
    	for(var date : r.linesPerAuthorPerDate.entrySet()) {
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

    // it is the same list of commits, but the authors do not appear twice.
    public List<Commit> sameAuthor2(List<Commit> gitLog) {
    	List<Commit> gitLog2 = new LinkedList<Commit>();
    	Map<String,String> emailToName = new HashMap<String,String>();
    	for(var commit : gitLog) {
    		String[] a = commit.author.split(" ");
    		String email = a[a.length-1];
    		if(emailToName.get(email) == null) {
            	emailToName.put(email, commit.author);
            }
    	}
    	for(var commit : gitLog) {
    		String[] a = commit.author.split(" ");
    		String email = a[a.length-1];
    		if(emailToName.containsKey(email)) {
    			var name = emailToName.get(email);
    			Commit m = new Commit(commit.id, name, commit.date, commit.description, commit.mergedFrom);
    			m.linesAdded = commit.linesAdded;
    			m.linesDeleted = commit.linesDeleted;
    			gitLog2.add(m);
            }
    	}
    	return gitLog2;
    }
    
    public Map<String, Integer> authorAndWeeks(String week, Result r) {
    	Map<String, Integer> res = new HashMap<>();
    	for(var date : r.linesPerAuthorPerDate.entrySet()) {
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
    
    public class Result implements AnalyzerPlugin.Result {
        private Map<LocalDate, Map<String, Integer>> linesPerAuthorPerDate = new TreeMap<>();
        private Map<String, Map<String, Integer>> linesPerAuthorPerWeeks = new TreeMap<>();
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
        
        // get linesPerAuthorPerDate
        Map<LocalDate, Map<String, Integer>> getLinesPerAuthorPerDate() {
            return linesPerAuthorPerDate;
        }
        
        @Override
        public String getResultAsString() {
        	if(linesPerAuthorPerWeeks.size() != 0) {
        		return linesPerAuthorPerWeeks.toString();
        	}
        	return linesPerAuthorPerDate.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
        	StringBuilder html = new StringBuilder();
        	String s = "";
        	//I create this variable to change the output easily
        	// the variable s is filled according to the way of sorting
        	
        	// check if the user wants the number of the lines added/deleted or the number of commits
        	if(lines) {
        		s += "<div>Number of lines added of commits per " + this.howToSort + " and per author : <ul><br>";
        	} else {
        		s += "<div>Number of lines deleted per " + this.howToSort + " and per author : <ul><br>";
        	}
        	
        	// display the commits (or lines added/deleted) by the way of sorting
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
        		for(var item : linesPerAuthorPerWeeks.entrySet()) {
        			s += "<ul>Week " + item.getKey().substring(item.getKey().length()-2, item.getKey().length()) + " (" + item.getKey().substring(0,4) + ")<br>";
        			Map<String, Integer> lines = item.getValue();
            		for(var l : lines.entrySet()) {
            			s += "<li>" + l.getKey() + " : " + l.getValue() + "</li><br>";
            		}
            		s+= "</ul><br>";
        		}
        	} else {
        		for(var item : linesPerAuthorPerDate.entrySet()) {
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
        
        @Override
        public void getResultAsHtmlDiv(WebGen wg) {
        	ArrayList<String> labels = new ArrayList<String>();
        	HashMap<String, ArrayList<Integer>> datasets = new HashMap<String, ArrayList<Integer>>();
        	int nbr = 0;
        	
            if(this.howToSort.equals("days") || this.howToSort.equals("months")) {
            	var labels0 = linesPerAuthorPerDate.entrySet().iterator().next().getKey();
        		LocalDate cmp = labels0;
        		LocalDate expected = cmp;
        		for(var item : linesPerAuthorPerDate.entrySet()) {
        			cmp = item.getKey();
        			if(!cmp.equals(expected)) {
        				while(!expected.equals(cmp)) {
        					if(howToSort.equals("days")) {
        						labels.add(expected.getDayOfMonth() + " " + expected.getMonth().name() + " " + expected.getYear());
        						expected = expected.plusDays(1);
        					} else {
        						labels.add(expected.getMonth().name() + " " + expected.getYear());
        						expected = expected.plusMonths(1);
        					}
            				nbr++;
        				}
        			}
        			if(howToSort.equals("days")) {
        				labels.add(item.getKey().getDayOfMonth() + " " + item.getKey().getMonth().name() +  " " + item.getKey().getYear());
        				expected = expected.plusDays(1);
					} else {
						labels.add(item.getKey().getMonth().name() + " " + item.getKey().getYear());
						expected = expected.plusMonths(1);
					}
            		Map<String, Integer> lines = item.getValue();
            		for(var l : lines.entrySet()) {
            			String author = l.getKey();
            			var authorAndInteger = datasets.getOrDefault(author, new ArrayList<Integer>());
            			while(authorAndInteger.size() != nbr) {
            				authorAndInteger.add(0);
            			}
            			authorAndInteger.add(l.getValue());
            			datasets.put(author, authorAndInteger);
            		}
            		nbr++;
        		}
            } else {
            	var labels0 = linesPerAuthorPerWeeks.entrySet().iterator().next().getKey();
        		int cmp = Integer.parseInt(labels0.substring(labels0.length()-2, labels0.length()));
        		int expected = cmp;
        		for(var item : linesPerAuthorPerWeeks.entrySet()) {
        			cmp = Integer.parseInt(item.getKey().substring(item.getKey().length()-2, item.getKey().length()));
        			if(cmp != expected) {
        				while(expected != cmp) {
        					labels.add("Week " + expected + " (" + item.getKey().substring(0,4) + ")");
            				expected++;
            				nbr++;
        				}
        			}
        			labels.add("Week " + item.getKey().substring(item.getKey().length()-2, item.getKey().length()) + " (" + item.getKey().substring(0,4) + ")");
    				Map<String, Integer> lines = item.getValue();
            		for(var l : lines.entrySet()) {
            			String author = l.getKey();
            			var authorAndInteger = datasets.getOrDefault(author, new ArrayList<Integer>());
            			while(authorAndInteger.size() != nbr) {
            				authorAndInteger.add(0);
            			}
            			authorAndInteger.add(l.getValue());
            			datasets.put(author, authorAndInteger);
            		}
            		nbr++;
        			expected++;
        		}
            }
            
            wg.addChart("Number of lines " +(this.lines ? "added" : "deleted") + " per author and per " + this.howToSort, labels, datasets);
        }
        
    }
}


