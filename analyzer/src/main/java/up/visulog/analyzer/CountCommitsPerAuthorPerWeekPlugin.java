package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountCommitsPerAuthorPerWeekPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    public CountCommitsPerAuthorPerWeekPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }
    
    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        //rester a modifier
        //ajouter la date
        for (var commit : gitLog) {
            var nb = result.commitsPerAuthorPerDay.getOrDefault(commit.author, 0);
            result.commitsPerAuthorPerDay.put(commit.author, nb + 1);
        }
        return result;
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
