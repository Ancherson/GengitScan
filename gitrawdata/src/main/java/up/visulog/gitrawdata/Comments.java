package up.visulog.gitrawdata;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Comments {
    private final String id;
    private final List<Comment> notes;
    public Comments(){
        this.id = null;
        this.notes = null;
    }
    public Comments(List<Comment> notes,String id){
        this.notes = notes;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public List<Comment> getNotes() {
        return notes;
    }
    public Collection<Comments> parseCommentsFromLog(String privateToken,int id,int idProject){
        //Creating results for Comments
        Collection<Comments> result = new ArrayList<Comments>();
        //APIresponse to get the comments from an issue
        APIresponse comments = new APIresponse();
        //sending the request to the API
        comments.createLogFileForOneIssue(privateToken,idProject,id);
        //Using GSON to parse the json file
        Gson logs = new Gson();
        try (Reader reader = new FileReader("../GitLog/resultsIssue"+String.valueOf(id)+".json")) {
            //Changing from JSON to Comments(Object java)
            Collection<Comments> commentsList = Arrays.asList(logs.fromJson(reader, Comments[].class));
            //Results are the notes of the comment list
            result = commentsList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public Collection<Comments> parseCommentsFromLog(int id,int idProject){
        return parseCommentsFromLog("1m1pdKszBNnTtCHS9KtS",idProject,id);
    }
    @Override
    public String toString() {
        return "Comments{" +
                "notes=" + notes +
                '}';
    }
}
