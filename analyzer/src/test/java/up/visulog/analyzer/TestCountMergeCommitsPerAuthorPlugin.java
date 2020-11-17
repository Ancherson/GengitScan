package up.visulog.analyzer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import up.visulog.gitrawdata.Commit;

public class TestCountMergeCommitsPerAuthorPlugin{
	@Test
	public void checkPluginMonthsAdded() {
		LocalDateTime date1 = LocalDateTime.of(2020, 11, 13, 20, 20, 20);
		LocalDateTime date2 = LocalDateTime.of(2020, 11, 14, 20, 20, 20);
		LocalDateTime date3 = LocalDateTime.of(2020, 11, 14, 20, 30, 20);
		
		Commit c1 = new Commit("1", "Author 1", date1, "description", "MergedFromSource");
		c1.linesAdded = 10;
		Commit c2 = new Commit("2", "Author 2", date2, "description", null);
		c2.linesAdded = 20;
		Commit c3 = new Commit("2", "Author 1", date3, "description", null);
		c3.linesAdded = 30;
		
		ArrayList<Commit> L = new ArrayList<Commit>();
		L.add(c1);
		L.add(c2);
		L.add(c3);

		
        var res = CountMergeCommitsPerAuthorPlugin.processLog(L);
        var sum = res.getMergeCommitsPerAuthor().values()
                .stream().reduce(0, Integer::sum);
		assertEquals(String.valueOf(1), String.valueOf(sum));

		}
}