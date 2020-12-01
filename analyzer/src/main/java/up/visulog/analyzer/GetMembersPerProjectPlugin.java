package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.*;
import up.visulog.webgen.WebGen;

import java.util.*;

public class GetMembersPerProjectPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    public GetMembersPerProjectPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(Collection<Member> membersLog) {
        var result = new Result();
        result.membersList = membersLog;
        return result;
    }

    @Override
    public void run() {
        //Create an api to get Members
        Members apiMembers = new Members();
        //Get results of members
        Collection<Member> resMembers =  apiMembers.getMembersFromJson(configuration.getPrivateToken(),configuration.getIdProject());
        //Process the members
        result = processLog(resMembers);
    }
    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }
    static class Result implements AnalyzerPlugin.Result {
        private Collection<Member> membersList = new ArrayList<>();

        Collection<Member> getMembers() {
            return membersList;
        }

        @Override
        public String getResultAsString() {
            return membersList.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Members:");
            html.append("<table class=\"members\"");
            html.append("<tr><th>Avatar</th><th>Full Name</th><th>Username</th></tr>");
            for (var item : membersList) {
                html.append("<tr><td><img src=\""+item.getAvatar_url()+"\"></td><td>"+item.getUsername()+"</td><td><a href=\""+item.getWeb_url()+"\">"+item.getUsername()+"</a></tr>");
            }
            html.append("</table>");
            return html.toString();
        }
        
        @Override
        public void getResultAsHtmlDiv(WebGen wg) {
            wg.addListMembers(membersList, "List of members");
        }
        
    }
}
