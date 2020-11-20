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
        for (Member member : membersLog) {
            //Get all the members
                result.membersList.add(member.getName());
            }
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
        private final List<String> membersList = new ArrayList<>();

        List<String> getMembersList() {
            return membersList;
        }

        @Override
        public String getResultAsString() {
            return membersList.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Members: <ul>");
            for (var item : membersList) {
                html.append("<li>").append(item).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }
        
        @Override
        public void getResultAsHtmlDiv(WebGen wg) {
        	
        }
        
    }
}
