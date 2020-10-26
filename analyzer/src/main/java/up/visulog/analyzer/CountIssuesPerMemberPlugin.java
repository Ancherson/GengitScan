package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.*;

import java.util.*;

public class CountIssuesPerMemberPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    public CountIssuesPerMemberPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(Collection<Assignees> assigneesLog) {
        var result = new Result();
        for (var assignees : assigneesLog) {
            //check every issue to get the assignee
                for(var assignee : assignees.getAssigneesList()){
                var nb = result.issuesPerMember.getOrDefault(assignee.getName(), 0);
                result.issuesPerMember.put(assignee.getName(), nb + 1);
            }
        }
        return result;
    }

    @Override
    public void run() {
        //Create an api to get Issues
        Assignees apiIssues = new Assignees();
        //Create an api to get Comments
        //Get results of issues
        Collection<Assignees> resAssignee =  apiIssues.parseAssigneeFromLog(configuration.getPrivateToken(),configuration.getIdProject());

        result = processLog(resAssignee);
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> issuesPerMember = new HashMap<>();

        Map<String, Integer> getCommitsPerAuthor() {
            return issuesPerMember;
        }

        @Override
        public String getResultAsString() {
            return issuesPerMember.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Issues per members: <ul>");
            for (var item : issuesPerMember.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
    }
}
