package up.visulog.analyzer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import up.visulog.gitrawdata.Commit;

public class TestCountLinesAddedOrDeletedPerDatePlugin{
	@Test
	public void checkPluginMonthsAdded() {
		LocalDateTime date1 = LocalDateTime.of(2020, 11, 13, 20, 20, 20);
		LocalDateTime date2 = LocalDateTime.of(2020, 11, 14, 20, 20, 20);
		LocalDateTime date3 = LocalDateTime.of(2020, 11, 14, 20, 30, 20);
		
		Commit c1 = new Commit("1", "Author 1", date1, "description", null);
		c1.linesAdded = 10;
		Commit c2 = new Commit("2", "Author 2", date2, "description", null);
		c2.linesAdded = 20;
		Commit c3 = new Commit("2", "Author 3", date3, "description", null);
		c3.linesAdded = 30;
		
		ArrayList<Commit> L = new ArrayList<Commit>();
		L.add(c1);
		L.add(c2);
		L.add(c3);

		
		var plugin = new CountLinesAddedOrDeletedPerDatePlugin(null, "months", true, false);
		String expected = "{2020-11-13=10,2020-11-14=50}";

		var result = plugin.processLog(L);
		assertEquals(expected, result.getResultAsString());

		}
	
	@Test
	public void checkPluginWeekshAdded() {
		LocalDateTime date1 = LocalDateTime.of(2020, 11, 13, 20, 20, 20);
		LocalDateTime date2 = LocalDateTime.of(2020, 11, 14, 20, 20, 20);
		LocalDateTime date3 = LocalDateTime.of(2020, 11, 14, 20, 30, 20);
		
		Commit c1 = new Commit("1", "Author 1", date1, "description", null);
		c1.linesAdded = 10;
		Commit c2 = new Commit("2", "Author 2", date2, "description", null);
		c2.linesAdded = 20;
		Commit c3 = new Commit("2", "Author 3", date3, "description", null);
		c3.linesAdded = 30;
		
		ArrayList<Commit> L = new ArrayList<Commit>();
		L.add(c1);
		L.add(c2);
		L.add(c3);

		
		var plugin = new CountLinesAddedOrDeletedPerDatePlugin(null, "weeks", true, false);
		String expected = "{2020-11-13=10,2020-11-14=50}";

		var result = plugin.processLog(L);
		assertEquals(expected, result.getResultAsString());

		}
	
	@Test
	public void checkPluginDayssAdded() {
		LocalDateTime date1 = LocalDateTime.of(2020, 11, 13, 20, 20, 20);
		LocalDateTime date2 = LocalDateTime.of(2020, 11, 14, 20, 20, 20);
		LocalDateTime date3 = LocalDateTime.of(2020, 11, 14, 20, 30, 20);
		
		Commit c1 = new Commit("1", "Author 1", date1, "description", null);
		c1.linesAdded = 10;
		Commit c2 = new Commit("2", "Author 2", date2, "description", null);
		c2.linesAdded = 20;
		Commit c3 = new Commit("2", "Author 3", date3, "description", null);
		c3.linesAdded = 30;
		
		ArrayList<Commit> L = new ArrayList<Commit>();
		L.add(c1);
		L.add(c2);
		L.add(c3);

		
		var plugin = new CountLinesAddedOrDeletedPerDatePlugin(null, "days", true, false);
		String expected = "{2020-11-13=10,2020-11-14=50}";

		var result = plugin.processLog(L);
		assertEquals(expected, result.getResultAsString());

		}
	
	//Now we test for lines Deleted
	@Test
	public void checkPluginMonthsDeleted() {
		LocalDateTime date1 = LocalDateTime.of(2020, 11, 13, 20, 20, 20);
		LocalDateTime date2 = LocalDateTime.of(2020, 11, 14, 20, 20, 20);
		LocalDateTime date3 = LocalDateTime.of(2020, 11, 14, 20, 30, 20);
		
		Commit c1 = new Commit("1", "Author 1", date1, "description", null);
		c1.linesAdded = 10;
		Commit c2 = new Commit("2", "Author 2", date2, "description", null);
		c2.linesAdded = 20;
		Commit c3 = new Commit("2", "Author 3", date3, "description", null);
		c3.linesAdded = 30;
		
		ArrayList<Commit> L = new ArrayList<Commit>();
		L.add(c1);
		L.add(c2);
		L.add(c3);

		
		var plugin = new CountLinesAddedOrDeletedPerDatePlugin(null, "months", false, false);
		String expected = "{2020-11-13=0,2020-11-14=0}";

		var result = plugin.processLog(L);
		assertEquals(expected, result.getResultAsString());

		}
	
	@Test
	public void checkPluginWeeksDeleted() {
		LocalDateTime date1 = LocalDateTime.of(2020, 11, 13, 20, 20, 20);
		LocalDateTime date2 = LocalDateTime.of(2020, 11, 14, 20, 20, 20);
		LocalDateTime date3 = LocalDateTime.of(2020, 11, 14, 20, 30, 20);
		
		Commit c1 = new Commit("1", "Author 1", date1, "description", null);
		c1.linesAdded = 10;
		Commit c2 = new Commit("2", "Author 2", date2, "description", null);
		c2.linesAdded = 20;
		Commit c3 = new Commit("2", "Author 3", date3, "description", null);
		c3.linesAdded = 30;
		
		ArrayList<Commit> L = new ArrayList<Commit>();
		L.add(c1);
		L.add(c2);
		L.add(c3);

		
		var plugin = new CountLinesAddedOrDeletedPerDatePlugin(null, "weeks", false, false);
		String expected = "{2020-11-13=0,2020-11-14=0}";

		var result = plugin.processLog(L);
		assertEquals(expected, result.getResultAsString());

		}
	
	@Test
	public void checkPluginDaysDeleted() {
		LocalDateTime date1 = LocalDateTime.of(2020, 11, 13, 20, 20, 20);
		LocalDateTime date2 = LocalDateTime.of(2020, 11, 14, 20, 20, 20);
		LocalDateTime date3 = LocalDateTime.of(2020, 11, 14, 20, 30, 20);
		
		Commit c1 = new Commit("1", "Author 1", date1, "description", null);
		c1.linesAdded = 10;
		Commit c2 = new Commit("2", "Author 2", date2, "description", null);
		c2.linesAdded = 20;
		Commit c3 = new Commit("2", "Author 3", date3, "description", null);
		c3.linesAdded = 30;
		
		ArrayList<Commit> L = new ArrayList<Commit>();
		L.add(c1);
		L.add(c2);
		L.add(c3);

		
		var plugin = new CountLinesAddedOrDeletedPerDatePlugin(null, "days", false, false);
		String expected = "{2020-11-13=0,2020-11-14=0}";

		var result = plugin.processLog(L);
		assertEquals(expected, result.getResultAsString());

		}
		
	
}

