package up.visulog.analyzer;

import java.util.ArrayList;
import java.util.List;


import org.junit.Test;
import up.visulog.gitrawdata.Comment;
import up.visulog.gitrawdata.Comments;
import up.visulog.gitrawdata.Author;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class TestCountCommentsPerAuthorPlugin {
    /* Let's check whether the number of authors is preserved and that the sum of the comments of each author is equal to the total number of comments */
    @Test
    public void checkCommentsSum() {
        Collection<Comments> comments = new ArrayList<>();
        
        String[] authors = {"author1", "author2", "author3"};
        int entries = 20;
        for(int i=0;i<entries;i++){
            List<Comment> comment= new ArrayList<>();
            comment.add(new Comment(null,new Author(i,authors[i%3],null),"comment",null));
            comments.add(new Comments(comment,null));
    }

        var res = CountCommentsPerAuthorPlugin.processLog(comments);
        assertEquals(authors.length, res.getCommentsPerAuthor().size());
        var sum = res.getCommentsPerAuthor().values().stream().reduce(0, Integer::sum);
        assertEquals(entries, sum.longValue());
    }
}
