package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.*;

public class CountCommitsPerAuthorPerDayPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    public CountCommitsPerAuthorPerDayPlugin(Configuration generalConfiguration) {
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
        private final Map<LocalDate, Map<String, Integer>> commitsPerAuthorPerDay = new HashMap<>();

        Map<LocalDate, Map<String, Integer>> getCommitsPerAuthorPerDay() {
            return commitsPerAuthorPerDay;
        }

        @Override
        public String getResultAsString() {
            return commitsPerAuthorPerDay.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Commits per author per day: <ul>");
            String s;
            for (var item : commitsPerAuthorPerDay.entrySet()) {
                s += "<ul>" + item.getKey().getDayOfMonth() + " " + item.getKey().getMonth().name() +  " " + item.getKey().getYear() + "<br>";
            }
            html.append(s).append("</ul></div>");
            return html.toString();
        }
    }
}
