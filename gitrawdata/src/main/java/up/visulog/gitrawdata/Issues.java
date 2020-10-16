package up.visulog.gitrawdata;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Issues {
    private Collection<Issue> issues;
    public Issues(){
        this.issues = null;
    }

    public Collection<Issue> getIssues() {
        return issues;
    }
    public Collection<Issue> getIssues(String privateToken,int idProject){
        //Creating results for Issue
        Collection<Issue> result = new ArrayList<Issue>();
        //APIresponse to get issues
        APIresponse comments = new APIresponse();
        //sending the request to the API
        comments.createLogFileForIssues(privateToken,idProject);
        //Using GSON to parse the json file
        Gson logs = new Gson();
        try (Reader reader = new FileReader("../GitLog/resultsIssues.json")) {
            //Changing from JSON to Issues(Object java)
            Collection<Issue> issuesList = Arrays.asList(logs.fromJson(reader, Issue[].class));
            //Results are the notes of the comment list
            result = issuesList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public Collection<Issue> getIssues(int idProject){
        return getIssues("1m1pdKszBNnTtCHS9KtS",idProject);
    }
    @Override
    public String toString() {
        return "Issues{" +
                "issues=" + issues +
                '}';
    }
}
