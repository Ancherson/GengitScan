package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Analyzer {
    private final Configuration config;

    private AnalyzerResult result;

    public Analyzer(Configuration config) {
        this.config = config;
    }

    public AnalyzerResult computeResults() {
        List<AnalyzerPlugin> plugins = new ArrayList<>();
        for (var pluginConfigEntry: config.getPluginConfigs().entrySet()) {
            var pluginName = pluginConfigEntry.getKey();
            var pluginConfig = pluginConfigEntry.getValue();
            var plugin = makePlugin(pluginName, pluginConfig);
            plugin.ifPresent(plugins::add);
        }
        // run all the plugins
        // TODO: try running them in parallel
        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (var plugin: plugins) {
        	//plugin implements the AnalyzerPlugin interface which inherits from Runnable
        	//To create a Thread, you need an object that implements Runnable
        	//A Thread runs autonomously, in parallel with the rest of the program
        	Thread t = new Thread(plugin);
        	threads.add(t);  	
        	//Execute the plugin run method
        	t.start();
        }
        
        //We need to check if all the Thread are finished
        //because the line "return new AnalyserResult ..." calls the function getResult(), 
        //who calls run() and if run() hasn't finished there are two calls of run()
        while(threads.size() != 0) {
        	for(int i = threads.size() - 1; i >= 0; i--) {
        		if(!threads.get(i).isAlive()) threads.remove(i);
        	}
        }
        
        // store the results together in an AnalyzerResult instance and return it
        return new AnalyzerResult(plugins.stream().map(AnalyzerPlugin::getResult).collect(Collectors.toList()));
    }

    // TODO: find a way so that the list of plugins is not hardcoded in this factory
    private Optional<AnalyzerPlugin> makePlugin(String pluginName, PluginConfig pluginConfig) {
        switch (pluginName) {
            case "countCommits" : return Optional.of(new CountCommitsPerAuthorPlugin(config));
            case "countMergeCommits" : return Optional.of(new CountMergeCommitsPerAuthorPlugin(config));
            case "countCommitsPerMonths" : return Optional.of(new CommitsPerDatePlugin(config));
            case "countCommitsPerDays" : return Optional.of(new CommitsPerDatePlugin(config, "days"));
            case "countCommitsPerWeeks" : return Optional.of(new CommitsPerDatePlugin(config, "weeks"));
            case "countComments" : return Optional.of(new CountCommentsPerAuthorPlugin(config));
            case "getMembers" : return Optional.of(new GetMembersPerProjectPlugin(config));
            case "countIssues" : return Optional.of(new CountIssuesPerMemberPlugin(config));
            case "countLinesAddedPerDays" : return Optional.of(new CountLinesAddedOrDeletedPerDatePlugin(config, "days", true));
            case "countLinesDeletedPerDays" : return Optional.of(new CountLinesAddedOrDeletedPerDatePlugin(config, "days", false));
            case "countLinesAddedPerMonths" : return Optional.of(new CountLinesAddedOrDeletedPerDatePlugin(config, "months", true));
            case "countLinesDeletedPerMonths" : return Optional.of(new CountLinesAddedOrDeletedPerDatePlugin(config, "months", false));
            case "countLinesAddedPerWeeks" : return Optional.of(new CountLinesAddedOrDeletedPerDatePlugin(config, "weeks", true));
            case "countLinesDeletedPerWeeks" : return Optional.of(new CountLinesAddedOrDeletedPerDatePlugin(config, "weeks", false));
            case "countLinesAdded" : return Optional.of(new CountLinesPerAuthorPlugin(config, true));
            case "countLinesDeleted" : return Optional.of(new CountLinesPerAuthorPlugin(config, false));
            case "countContribution" : return Optional.of(new CountContributionPlugin(config));
            default : return Optional.empty();
        }
    }

}
