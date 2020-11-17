package up.visulog.analyzer;
import org.junit.Test;
import up.visulog.gitrawdata.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
public class TestCountIssuesPerMemberPlugin {
    @Test
    public void checkCountIssues(){
        String[] members = {"member1","member2","member3"};
        Collection<Assignees> assignees = new ArrayList<>();
        int entries = 20;
        for(int i=0;i<entries;i++){
            Collection<Assignee> assignee = new ArrayList<>();
            assignee.add(new Assignee(i,members[i%3],null));
            assignees.add(new Assignees(assignee));
        }
        var res = CountIssuesPerMemberPlugin.processLog(assignees);
        assertEquals(members.length, res.getIssuesPerMember().size());
        var sum = res.getIssuesPerMember().values().stream().reduce(0, Integer::sum);
        assertEquals(entries, sum.longValue());
    }

}
