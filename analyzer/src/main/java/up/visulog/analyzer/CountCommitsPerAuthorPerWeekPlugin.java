package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.*;

public class CountCommitsPerAuthorPerWeekPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    public CountCommitsPerAuthorPerWeekPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        Map<LocalDate, List<Commit>> commitsPerDay = sortCommitsPerDay(gitLog);
        for (var commit : commitsPerDay.entrySet()) {
          LocalDate m = commit.getKey();
    		  nb = result.commitsPerAuthorPerDay.getOrDefault(commit.author, 0);
            result.commitsPerAuthorPerDay.put(commit.author, nb + 1);
        }
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
        private final Map<LocalDate, Map<String, Integer>> commitsPerAuthorPerWeek = new HashMap<>();

        Map<LocalDate, Map<String, Integer>> getCommitsPerAuthorPerWeek() {
            return commitsPerAuthorPerWeek;
        }

        @Override
        public String getResultAsString() {
            return commitsPerAuthorPerWeek.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Commits per author per week: <ul>");
            String s;
            for (var item : commitsPerAuthorPerWeek.entrySet()) {
                s += "<ul>Week " + item.getKey().substring(item.getKey().length-2, item.getKey().length()) + " (" + item.getKey().substring(0, 4) + ")<br>";
                Map<String, Integer> commits = item.getValue();
            		for(var c : commits.entrySet()) {
            			s += "<li>" + c.getKey() + " : " + c.getValue() + "</li><br>";
            		}
            }
            html.append(s).append("</ul></div>");
            return html.toString();
        }
    }
}
