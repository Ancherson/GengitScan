package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountCommitsPerAuthorPerMonthPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    public CountCommitsPerAuthorPerMonthPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        //rester a modifier
        for (var commit : gitLog) {
            var nb = result.commitsPerAuthorPerMonth.getOrDefault(commit.author, 0);
            result.commitsPerAuthorPerMonth.put(commit.author, nb + 1);
        }
        return result;
    }
