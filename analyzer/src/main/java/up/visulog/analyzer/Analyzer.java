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
            case "countCommits" : return Optional.of(new CountCommitsPerAuthorPlugin(config, false));
            case "countCommitsForAllBranches" : return Optional.of(new CountCommitsPerAuthorPlugin(config, true));
            
            case "countMergeCommits" : return Optional.of(new CountMergeCommitsPerAuthorPlugin(config));
            
            case "countCommitsPerMonths" : return Optional.of(new CommitsPerDatePlugin(config, "months", false));
            case "countCommitsPerMonthsForAllBranches" : return Optional.of(new CommitsPerDatePlugin(config, "months", true));
            case "countCommitsPerDays" : return Optional.of(new CommitsPerDatePlugin(config, "days", false));
            case "countCommitsPerDaysForAllBranches" : return Optional.of(new CommitsPerDatePlugin(config, "days", true));
            case "countCommitsPerWeeks" : return Optional.of(new CommitsPerDatePlugin(config, "weeks", false));
            case "countCommitsPerWeeksForAllBranches" : return Optional.of(new CommitsPerDatePlugin(config, "weeks", true));
            
            case "countComments" : return Optional.of(new CountCommentsPerAuthorPlugin(config));
            case "getMembers" : return Optional.of(new GetMembersPerProjectPlugin(config));
            case "countIssues" : return Optional.of(new CountIssuesPerMemberPlugin(config));
            
            case "countLinesAddedPerDays" : return Optional.of(new CountLinesPerDatePlugin(config, "days", true, false));
            case "countLinesAddedPerDaysForAllBranches" : return Optional.of(new CountLinesPerDatePlugin(config, "days", true, true));
            case "countLinesDeletedPerDays" : return Optional.of(new CountLinesPerDatePlugin(config, "days", false, false));
            case "countLinesDeletedPerDaysForAllBranches" : return Optional.of(new CountLinesPerDatePlugin(config, "days", false, true));
            case "countLinesAddedPerMonths" : return Optional.of(new CountLinesPerDatePlugin(config, "months", true, false));
            case "countLinesAddedPerMonthsForAllBranches" : return Optional.of(new CountLinesPerDatePlugin(config, "months", true, true));
            case "countLinesDeletedPerMonths" : return Optional.of(new CountLinesPerDatePlugin(config, "months", false, false));
            case "countLinesDeletedPerMonthsForAllBranches" : return Optional.of(new CountLinesPerDatePlugin(config, "months", false, true));
            case "countLinesAddedPerWeeks" : return Optional.of(new CountLinesPerDatePlugin(config, "weeks", true, false));
            case "countLinesAddedPerWeeksForAllBranches" : return Optional.of(new CountLinesPerDatePlugin(config, "weeks", true, true));
            case "countLinesDeletedPerWeeks" : return Optional.of(new CountLinesPerDatePlugin(config, "weeks", false, false));
            case "countLinesDeletedPerWeeksForAllBranches" : return Optional.of(new CountLinesPerDatePlugin(config, "weeks", false, true));
            
            case "getExtensions" : return Optional.of(new GetExtensionsPerProjectPlugin(config));
        
            case "countLinesAdded" : return Optional.of(new CountLinesPerAuthorPlugin(config, true, false));
            case "countLinesAddedForAllBranches" : return Optional.of(new CountLinesPerAuthorPlugin(config, true, true));
            case "countLinesDeleted" : return Optional.of(new CountLinesPerAuthorPlugin(config, false, false));
            case "countLinesDeletedForAllBranches" : return Optional.of(new CountLinesPerAuthorPlugin(config, false, true));
            
            case "countLinesAddedPerAuthorPerDays": return Optional.of(new CountLinesPerAuthorPerDatePlugin(config, "days", true, false));
            case "countLinesAddedPerAuthorPerDaysForAllBranches": return Optional.of(new CountLinesPerAuthorPerDatePlugin(config, "days", true, true));
            case "countLinesAddedPerAuthorPerWeeks": return Optional.of(new CountLinesPerAuthorPerDatePlugin(config, "weeks", true, false));
            case "countLinesAddedPerAuthorPerWeeksForAllBranches": return Optional.of(new CountLinesPerAuthorPerDatePlugin(config, "weeks", true, true));
            case "countLinesAddedPerAuthorPerMonths": return Optional.of(new CountLinesPerAuthorPerDatePlugin(config, "months", true, false));
            case "countLinesAddedPerAuthorPerMonthsForAllBranches": return Optional.of(new CountLinesPerAuthorPerDatePlugin(config, "months", true, true));
            case "countLinesDeletedPerAuthorPerDays": return Optional.of(new CountLinesPerAuthorPerDatePlugin(config, "days", false, false));
            case "countLinesDeletedPerAuthorPerDaysForAllBranches": return Optional.of(new CountLinesPerAuthorPerDatePlugin(config, "days", false, true));
            case "countLinesDeletedPerAuthorPerWeeks": return Optional.of(new CountLinesPerAuthorPerDatePlugin(config, "weeks", false, false));
            case "countLinesDeletedPerAuthorPerWeeksForAllBranches": return Optional.of(new CountLinesPerAuthorPerDatePlugin(config, "weeks", false, true));
            case "countLinesDeletedPerAuthorPerMonths": return Optional.of(new CountLinesPerAuthorPerDatePlugin(config, "months", false, false));
            case "countLinesDeletedPerAuthorPerMonthsForAllBranches": return Optional.of(new CountLinesPerAuthorPerDatePlugin(config, "months", false, true));
            
            case "countContribution" : return Optional.of(new CountContributionPlugin(config));
            default : return Optional.empty();
        }
    }

}
