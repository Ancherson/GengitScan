package up.visulog.gitrawdata;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Members {
    private Collection<Member> members;
    public Members(){
        this.members = null;
    }

    public Collection<Member> getMembers() {
        return members;
    }
    public Collection<Member> getMembersFromJson(String privateToken,int idProject){
        //Creating results for members
        Collection<Member> result = new ArrayList<Member>();
        //APIresponse to get members
        APIresponse membersAPI = new APIresponse();
        //sending the request to the API
        membersAPI.createLogFileForMembers(privateToken,idProject);
        //Using GSON to parse the json file
        Gson logs = new Gson();
        try (Reader reader = new FileReader("../GitLog/resultsMembers.json")) {
            //Changing from JSON to Issues(Object java)
            Collection<Member> membersList = Arrays.asList(logs.fromJson(reader, Member[].class));
            //Results are the notes of the comment list
            result = membersList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
