package up.visulog.gitrawdata;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Comment {
    private final String id;
    private final Author author;
    private final String body;
    private final Date date;
    public Comment(String id, Author author, String body, Date date){
        this.id = id;
        this.author = author;
        this.body = body;
        this.date = date;
    }

    public Author getAuthor() {
        return author;
    }

    public Date getDate() {
        return date;
    }

    public String getBody() {
        return body;
    }

    public String getId() {
        return id;
    }



    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", author=" + author.toString() +
                ", body='" + body + '\'' +
                ", date=" + date +
                '}';
    }
}
