package up.visulog.gitrawdata;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Assignees {
    private Collection<Assignee> assignees;
    public Assignees(){
        this.assignees = null;
    }
    public Assignees(Collection<Assignee> a) {
    	this.assignees = a;
    }

    public Collection<Assignee> getAssigneesList() {
        return assignees;
    }
    public Collection<Assignees> parseAssigneeFromLog(String privateToken,int idProject){
        //Creating results for assignee
        Collection<Assignees> result = new ArrayList<>();
        //APIresponse to get the assignee from an issue
        APIresponse assigneesAPI = new APIresponse();
        //sending the request to the API
        assigneesAPI.createLogFileForIssues(privateToken,idProject);
        //Using GSON to parse the json file
        Gson logs = new Gson();
        try (Reader reader = new FileReader("../GitLog/resultsIssues.json")) {
            //Changing from JSON to Assignees(Object java)
            Collection<Assignees> assignees = Arrays.asList(logs.fromJson(reader, Assignees[].class));
            //Results are the assignees
            result = assignees;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public String toString() {
        return "Assignees{" +
                "assigneesList=" + assignees +
                '}';
    }
}
