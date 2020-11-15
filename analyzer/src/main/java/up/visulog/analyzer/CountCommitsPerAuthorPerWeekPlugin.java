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
