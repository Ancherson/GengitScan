package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.webgen.WebGen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Counts the number of commits for each different author of the git project.
 */
public class CountCommitsPerAuthorPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;
    private boolean allBranches;

    /**
     * Constructor
     * @param generalConfiguration stores the path of the git project to analyze
     * @param allBranches if true, the result will be computed on all the branches of the git project, if false, just on the current branch
     */
    public CountCommitsPerAuthorPlugin(Configuration generalConfiguration, boolean allBranches) {
        this.configuration = generalConfiguration;
        this.allBranches = allBranches;
    }

    /**
     * Goes through a list of commits in order to count the number of commits for each author whose commits appear in the list.
     * @param gitLog a list of commits
     * @return a Result object which contains a HashMap which links authors to the number of commits they have done
     */
    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        Map<String,String>emailToName = new HashMap<String,String>();
        for (var commit : gitLog) {
        	String[] author = commit.author.split(" ");
        	String email = author[author.length - 1];
        	if(emailToName.get(email) == null) {
        		emailToName.put(email, commit.author);
        	}
            var nb = result.commitsPerAuthor.getOrDefault(email, 0);
            result.commitsPerAuthor.put(email, nb + 1);
        }
        for(var e : emailToName.entrySet()) {
        	int nbCommit = result.commitsPerAuthor.remove(e.getKey());
        	result.commitsPerAuthor.put(e.getValue(), nbCommit);
        }
        return result;
    }

    /**
     * Computes the result for the git project specified in configuration.
     */
    @Override
    public void run() {
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath(), allBranches));
    }

    /**
     * Computes the result if it has not already been done, and returns it
     * @return the result
     */
    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    /**
     * Stores the number of commits for each author, and manages how this data is outputted.
     */
    static class Result implements AnalyzerPlugin.Result {
        /**
         * Links the authors to the number of commits they have done.
         */
        private final Map<String, Integer> commitsPerAuthor = new HashMap<>();

        Map<String, Integer> getCommitsPerAuthor() {
            return commitsPerAuthor;
        }

        @Override
        public String getResultAsString() {
            return commitsPerAuthor.toString();
        }

        /**
         * Generates an HTML div containing a list of authors and their number of commits
         * @return the html div as a String
         */
        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Commits per author: <ul>");
            for (var item : commitsPerAuthor.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
        
        /**
         * Formats the result into a list of labels (the authors) and a list of data (the number of commits)
         * and passes them to a WebGen object so it generates a chart in an HTML div.
         * @param wg the WebGen object which will generate the output HTML page
         */
        @Override
        public void getResultAsHtmlDiv(WebGen wg) {
            ArrayList<String> authorOfCommits = new ArrayList<String>();
            ArrayList<Integer> numberOfCommits= new ArrayList<Integer>();
            for(var data : getCommitsPerAuthor().entrySet()){
            	String[] nameTab = data.getKey().split(" ");
            	String name = "";
            	for(int i=0; i<nameTab.length-1; i++) {
            		name += nameTab[i] + " ";
            	}
                authorOfCommits.add(name);
                numberOfCommits.add(data.getValue());    
            }

            wg.addChart("bar", "Number of commits per member", "Number of commits", authorOfCommits, numberOfCommits);
        }
    }
}
