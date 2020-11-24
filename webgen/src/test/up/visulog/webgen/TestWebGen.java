package up.visulog.webgen;

import up.visulog.webgen.WebGen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


public class TestWebGen {
	
	@Test
	public void checkWebGen() {
		//initializing Map and String for addListAuthor
		Map<String, Integer>list = new HashMap<String, Integer>();
		list.put("Author 1",1);
		list.put("Author 2", 2);
		String title ="title";
		//initializing variables for addChart
		String type = "type";
		ArrayList<String> labels = new ArrayList<String>();
		labels.add("Label1");
		labels.add("Label2");
		ArrayList<Integer> data = new ArrayList<Integer>();
		data.add(1);
		data.add(2);
		//initializing variables for addChart(String title, ArrayList<String> labels, HashMap<String, ArrayList<Integer>> datasets)
		HashMap<String, ArrayList<Integer>> datasets = new HashMap<String, ArrayList<Integer>>();
		datasets.put("data1", data);
		
		//using these variables we will run the different functions of WebGen and test if it gives the expected result
	
		WebGen webG = new WebGen();
		webG.addListAuthor(list, title);
		webG.addChart(title, labels, datasets);
		
		String result = webG.getHtml();
		String expected = "";
		
		//checking if we get expected result
		assertEquals(result,expected);
	}	

}
