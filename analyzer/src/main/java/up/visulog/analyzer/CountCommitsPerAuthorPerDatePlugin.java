package up.visulog.analyzer;
import java.time.LocalDate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.webgen.WebGen;

public class CountCommitsPerAuthorPerDatePlugin implements AnalyzerPlugin {
	
	private final Configuration configuration;
    private String howToSort = "months";
    // the plugin sort commits per months, this is a default value
    // the value change if the user wants the number of commits par weeks or per day;
    private Result result;
    private boolean allBranches;
    // if the variable is true, the plugin count the lines for all branches
    // if the variable is false, the plugin count the lines for the branch where the user is
    
    // Constructor
    public CountCommitsPerAuthorPerDatePlugin(Configuration generalConfiguration, String howToSort, boolean allBranches) {
        this.configuration = generalConfiguration;
        this.howToSort = howToSort;
        this.allBranches = allBranches;
    }
    
    // when the program starts, the first function is called, it starts the plugin
    public Result processLog(List<Commit> gitLog) {
    	List<Commit> gitLog2 = sameAuthor2(gitLog);
    	var result = new Result();
    	// change the values of the object Result
    	result.howToSort = this.howToSort;
    	
    	// first, we find the commits sorting per the days
    	Map<LocalDate, List<Commit>> commitsPerDate = sortCommitsPerDays(gitLog2);
    	
    	// then, we create a new Map with a Key which associates an author and his number of commits
    	for(var commitsPerDays : commitsPerDate.entrySet()) {
    		LocalDate m = commitsPerDays.getKey();
    		Map<String, Integer> commitsPerAuthor = commitsPerAuthor(commitsPerDays.getValue());
    		result.commitsPerAuthorPerDate.put(m, commitsPerAuthor);
    	}
    	// we just have to sort them by month or week
    	
    	// sort the commits per Months
    	if(howToSort.equals("months")) {
    		// first, we create a new Map
    		Map<LocalDate, Map<String, Integer>> res = new TreeMap<>();
    		// we sort the number of added/deleted lines per months
        	for(var date : result.commitsPerAuthorPerDate.entrySet()) {
        		LocalDate m = LocalDate.of(date.getKey().getYear(), date.getKey().getMonth(), 1);
        		// we create a new Map which gives the right number of lines to the authors
        		Map<String, Integer> res2 = authorsAndMonths(m, result);
        		res.put(m, res2);
        	}
        	
        	// change the key and the value of result.commitsPerDate
        	result.commitsPerAuthorPerDate.clear();
        	result.commitsPerAuthorPerDate.putAll(res);
    	}
    	
    	// sort the commits per Weeks
    	else if(howToSort.equals("weeks")) {
    		// first, we create a new Map
    		Map<String, Map<String, Integer>> res = new TreeMap<>();
    		// we sort the number of added/deleted lines per weeks
        	for(var date : result.commitsPerAuthorPerDate.entrySet()) {
        		String m = Integer.toString(date.getKey().getYear()) + " Week " + Integer.toString(date.getKey().getDayOfYear()/7);
        		// we create a new Map which gives the right number of lines to the authors
        		Map<String, Integer> res2 = authorAndWeeks(m, result);
        		res.put(m, res2);
        	}
    		
         // change the key and the value of result.commitsPerWeeks
            result.commitsPerAuthorPerWeeks.putAll(res);
    	}
    	return result;
    }
    
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
    
    public Map<String, Integer> commitsPerAuthor(List<Commit> listCommits) {
    	Map<String, Integer> res = new HashMap<>();
        for (var commit : listCommits) {
    		String author = commit.author;
    		var nb = res.getOrDefault(author, 0);
    		res.put(author, nb + 1);
        }
        return res;
    }
	
 // to sort the commits by authors and per months
    public Map<String, Integer> authorsAndMonths(LocalDate months, Result r) {
    	Map<String, Integer> res = new HashMap<>();
    	for(var date : r.commitsPerAuthorPerDate.entrySet()) {
    		LocalDate m = LocalDate.of(date.getKey().getYear(), date.getKey().getMonth(), 1);
    		if(months.equals(m)) {
    			for(var commitsPerAuthor : date.getValue().entrySet()) {
    				String author = commitsPerAuthor.getKey();
                	var nb = res.getOrDefault(author, 0);
                	res.put(author, nb + commitsPerAuthor.getValue());
    			}
    		}
    	}
    	return res;
    }
    // to sort the commits by authors and per weeks
    public Map<String, Integer> authorAndWeeks(String week, Result r) {
    	Map<String, Integer> res = new HashMap<>();
    	for(var date : r.commitsPerAuthorPerDate.entrySet()) {
    		String m = Integer.toString(date.getKey().getYear()) + " Week " + Integer.toString(date.getKey().getDayOfYear()/7);
    		if(week.equals(m)) {
    			for(var commitsPerAuthor : date.getValue().entrySet()) {
    				String author = commitsPerAuthor.getKey();
                	var nb = res.getOrDefault(author, 0);
                	res.put(author, nb + commitsPerAuthor.getValue());
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
    			gitLog2.add(m);
            }
    	}
    	return gitLog2;
    }

 // function which executes the plugin
    public void run() {
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath(), allBranches));
    }

    // function which returns the results of the analysis
    public Result getResult() {
        if (result == null) run();
        return result;
    }    
	
	public class Result implements AnalyzerPlugin.Result {
		private Map<LocalDate, Map<String, Integer>> commitsPerAuthorPerDate = new TreeMap<>();
        private Map<String, Map<String, Integer>> commitsPerAuthorPerWeeks = new TreeMap<>();
        private String howToSort = "months";

	
		public String getResultAsString() {
			if(howToSort.equals("weeks")) {
				return commitsPerAuthorPerWeeks.toString();
			}
			return commitsPerAuthorPerDate.toString();
		}

		public String getResultAsHtmlDiv() {
			String s = "<div>Number of Commits per " + this.howToSort + " and per author : <ul><br>";
        	// display the commits by the way of sorting
        	if(this.howToSort.equals("days")) {
        		for(var item : commitsPerAuthorPerDate.entrySet()) {
        			s += "<ul>" + item.getKey().getDayOfMonth() + " " + item.getKey().getMonth().name() +  " " + item.getKey().getYear() + "<br>";
        			Map<String, Integer> commits = item.getValue();
            		for(var c : commits.entrySet()) {
            			s += "<li>" + c.getKey() + " : " + c.getValue() + "</li><br>";
            		}
            		s+= "</ul><br>";
        		}
        	} else if(this.howToSort.equals("weeks")) {
        		for(var item : commitsPerAuthorPerWeeks.entrySet()) {
        			s += "<ul>Week " + item.getKey().substring(item.getKey().length()-2, item.getKey().length()) + " (" + item.getKey().substring(0,4) + ")<br>";
        			Map<String, Integer> commits = item.getValue();
            		for(var c : commits.entrySet()) {
            			s += "<li>" + c.getKey() + " : " + c.getValue() + "</li><br>";
            		}
            		s+= "</ul><br>";
        		}
        	} else {
        		for(var item : commitsPerAuthorPerDate.entrySet()) {
        			s += "<ul>" + item.getKey().getMonth().name() + " " + item.getKey().getYear() + "<br>";
        			Map<String, Integer> commits = item.getValue();
            		for(var c : commits.entrySet()) {
            			s += "<li>" + c.getKey() + " : " + c.getValue() + "</li><br>";
            		}
            		s+= "</ul><br>";
        		}
        	}
        	s += "</ul></div>";
        	return s;
		}


		public void getResultAsHtmlDiv(WebGen wg) {
			ArrayList<String> labels = new ArrayList<String>();
        	HashMap<String, ArrayList<Integer>> datasets = new HashMap<String, ArrayList<Integer>>();
        	int nbr = 0;
        	
            if(this.howToSort.equals("days") || this.howToSort.equals("months")) {
            	var labels0 = commitsPerAuthorPerDate.entrySet().iterator().next().getKey();
        		LocalDate cmp = labels0;
        		LocalDate expected = cmp;
        		for(var item : commitsPerAuthorPerDate.entrySet()) {
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
            		Map<String, Integer> commits = item.getValue();
            		for(var c : commits.entrySet()) {
            			String author = c.getKey();
            			var authorAndInteger = datasets.getOrDefault(author, new ArrayList<Integer>());
            			while(authorAndInteger.size() != nbr) {
            				authorAndInteger.add(0);
            			}
            			authorAndInteger.add(c.getValue());
            			datasets.put(author, authorAndInteger);
            		}
            		nbr++;
        		}
            } else {
            	var labels0 = commitsPerAuthorPerWeeks.entrySet().iterator().next().getKey();
        		int cmp = Integer.parseInt(labels0.substring(labels0.length()-2, labels0.length()));
        		int expected = cmp;
        		for(var item : commitsPerAuthorPerWeeks.entrySet()) {
        			cmp = Integer.parseInt(item.getKey().substring(item.getKey().length()-2, item.getKey().length()));
        			if(cmp != expected) {
        				while(expected != cmp) {
        					labels.add("Week " + expected + " (" + item.getKey().substring(0,4) + ")");
            				expected++;
            				nbr++;
        				}
        			}
        			labels.add("Week " + item.getKey().substring(item.getKey().length()-2, item.getKey().length()) + " (" + item.getKey().substring(0,4) + ")");
    				Map<String, Integer> commits = item.getValue();
            		for(var c : commits.entrySet()) {
            			String author = c.getKey();
            			var authorAndInteger = datasets.getOrDefault(author, new ArrayList<Integer>());
            			while(authorAndInteger.size() != nbr) {
            				authorAndInteger.add(0);
            			}
            			authorAndInteger.add(c.getValue());
            			datasets.put(author, authorAndInteger);
            		}
            		nbr++;
        			expected++;
        		}
            }
            
            wg.addChart("Number of commits per author and per " + this.howToSort, labels, datasets);
			
		}
		
	}

}
