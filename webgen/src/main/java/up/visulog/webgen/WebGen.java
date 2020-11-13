package up.visulog.webgen;

import java.util.HashMap;
import java.util.Map;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.xmlet.htmlapifaster.Body;
import org.xmlet.htmlapifaster.Html;

import htmlflow.HtmlView;
import htmlflow.StaticHtml;

public class WebGen {
	//Ajouter le head
	private Body<Html<HtmlView>> view = StaticHtml.view()
			.html()
				.head()
					.title().text("GenGit Scan").__()
					.script().attrSrc("chartsMin.js").__()
					.script().attrSrc("chartGenjs").__()
				.__()
				.body();
	
	public void addListAuthor(Map<String, Integer>list, String title) {
		var body = view.ul().text(title);
		for(var item : list.entrySet()) {
			String content = item.getKey() + ": " + item.getValue();
			
			body.li().text(content).__();
		}		
	}
	
	public void addChart(String type, String title, ArrayList<String> labels, ArrayList<Integer> data){
		String labelsJs = "var labels = [";
		for(String l : labels){
			labelsJs += l;
		}
		labelsJs = labelsJs.substring(0, labelsJs.length-1);
		labelsJs += "];";

		String dataJs = "var data = [";
		for(String d : data){
			dataJs += d;
		}
		dataJs = dataJs.substring(0, dataJs.length-1);
		dataJs += "];";

		String genChartJs = "genChart("+type+","+title+", labels, data);";

		String js = labelsJs+"\n"+dataJs+"\n"+genChartJs+"\n";

		view.div()
				.script().text(js).__()
			.__();
	}


	public String getHtml() {
		return view.__().__().render();
	}
	
	//Write in the file index.html the result of the different plugin
	public void write(){
		try{
			FileWriter file = new FileWriter("../htmlResult/index.html");
			file.write(getHtml());
			file.close();
		}catch(IOException e){
			throw new RuntimeException("Error SaveConfig", e);
		}

	}
	
	
	
	
}
