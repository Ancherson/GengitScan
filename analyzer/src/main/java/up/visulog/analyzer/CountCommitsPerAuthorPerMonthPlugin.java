package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import java.time.*;

public class CountCommitsPerAuthorPerMonthPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    public CountCommitsPerAuthorPerMonthPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        Map<LocalDate, List<Commit>> commitsPerDay = sortCommitsPerDay(gitLog);
        for (var commit : commitsPerDay.entrySet()) {
          LocalDate m = commit.getKey();
    		  var nb = result.commitsPerAuthorPerDay.getOrDefault(commit.author, 0);
            result.commitsPerAuthorPerDay.put(commit.author, nb + 1);
        }
        Map<LocalDate, Map<String, Integer>> res = new TreeMap<>();
    		// we sort the number of commits per month
        	for(var date : result.getCommitsPerAuthorPerMonth().entrySet()) {
            LocalDate m = LocalDate.of(date.getKey().getYear(), date.getKey().getMonth(), 1);
        		Map<String, Integer> res2 = authorsAndMonths(m, result);
        		res.put(m, res2);
        	}
          result.CommitsPerAuthorPerMonth.putAll(res);
          return result;
    }

    //commitsPerDay
    static Map<LocalDate, List<Commit>> sortCommitsPerDay(List<Commit> gitLog) {
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

   public Map<String, Integer> authorAndMonths(String months, Result r) {
     Map<String, Integer> res = new HashMap<>();
    	for(var date : r.commitsPerAuthorPerDay.entrySet()) {
    		LocalDate m = LocalDate.of(date.getKey().getYear(), date.getKey().getMonth(), 1);
    		if(months.equals(m)) {
    			for(var c : date.getValue().entrySet()) {
    				String a = c.getKey();
                	var nb = res.getOrDefault(a, 0);
                	res.put(a, nb + 1);
    			}
    		}
    	}
    	return res;
    }

    @Override
    public void run() {
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {
        public final Map<LocalDate, Map<String, Integer>> commitsPerAuthorPerDay = new TreeMap<>();
        public Map<String, Map<String, Integer>> commitsPerAuthorPerMonth = new TreeMap<>();

        Map<LocalDate, Map<String, Integer>> getCommitsPerAuthorPerMonth() {
            return commitsPerAuthorPerMonth;
        }

        @Override
        public String getResultAsString() {
            return commitsPerAuthorPerMonth.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Commits per author per month: <ul>");
            String s;
            for (var item : commitsPerAuthorPerMonth.entrySet()) {
        			s += "<ul>" + item.getKey().getMonth().name() + " " + item.getKey().getYear() + "<br>";
        			Map<String, Integer> commits = item.getValue();
            		for(var c : commits.entrySet()) {
            			s += "<li>" + c.getKey() + " : " + c.getValue() + "</li><br>";
            		}
            		s+= "</ul><br>";
        		}
            html.append(s).append("</ul></div>");
            return html.toString();
        }
    }
}
