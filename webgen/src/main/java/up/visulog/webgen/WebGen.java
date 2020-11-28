package up.visulog.webgen;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.xmlet.htmlapifaster.Body;
import org.xmlet.htmlapifaster.Html;

import htmlflow.HtmlView;
import htmlflow.StaticHtml;

public class WebGen {
	private Body<Html<HtmlView>> view = StaticHtml.view()
			.html()
				.head()
					.meta().attrCharset("utf-8").__()
					.title().text("GenGit Scan").__()
					.link().addAttr("rel", "stylesheet").addAttr("href", "style.css").__()
					.script().attrSrc("chartsMin.js").__()
					.script().attrSrc("chartGen.js").__()
				.__()
				.body();
	
	public void addListAuthor(Map<String, Integer>list, String title) {
		var body = view.ul().text(title);
		for(var item : list.entrySet()) {
			String content = item.getKey() + ": " + item.getValue();
			
			body.li().text(content).__();
		}
		body.__().__();		
	}

	public void addListAuthor(ArrayList<String>list, String title){
		var body = view.div().ul().text(title);
		for(String author : list){
			body.li().text(author).__();
		}
		body.__().__();
	}

	public void addChart(String type, String title, String label, ArrayList<String> labels, ArrayList<Integer> data){
		String labelsJs = "var labels = [";
		for(String l : labels){
			labelsJs += "'" + l + "',";
		}
		labelsJs = labelsJs.substring(0, labelsJs.length()-1);
		labelsJs += "];";

		String dataJs = "var data = [";
		for(Integer d : data){
			dataJs += d+",";
		}
		dataJs = dataJs.substring(0, dataJs.length()-1);
		dataJs += "];";

		String genChartJs = "genChart('"+type+"','"+title+"','"+label+"', labels, data);";

		String js = labelsJs+"\n"+dataJs+"\n"+genChartJs+"\n";

		view.div()
				.script().text(js).__()
			.__();
	}

	public void addChartDouble(String type, String title, String label, ArrayList<String> labels, ArrayList<Double> data){
		String labelsJs="var labels = [";
		for(String l : labels){
			labelsJs += "'" + l + "',";
		}
		labelsJs = labelsJs.substring(0, labelsJs.length()-1);
		labelsJs += "];";

		String dataJs = "var data = [";
		for(Double d : data){
			dataJs+= d+",";
		}
		dataJs = dataJs.substring(0, dataJs.length()-1);
		dataJs+="];";

		String genChartJs = "genChart('"+type+"','"+title+"','"+label+"', labels, data);";

		String js = labelsJs+"\n"+dataJs+"\n"+genChartJs+"\n";

		view.div()
				.script().text(js).__()
			.__();
	}

	public void addChart(String title, ArrayList<String> labels, HashMap<String, ArrayList<Integer>> datasets){
		String labelsJs = "var labels = [";
		for(String l : labels){
			labelsJs += "'" + l + "',";
		}
		labelsJs = labelsJs.substring(0, labelsJs.length()-1);
		labelsJs += "];";

		String datasetsJs = "var datasets = [";
		for(var d : datasets.entrySet()){
			datasetsJs += "['" + d.getKey() + "', [";
			for(int n : d.getValue()){
				datasetsJs += n + ",";
			}
			datasetsJs = datasetsJs.substring(0, datasetsJs.length()-1)+"]],";
		}
		datasetsJs = datasetsJs.substring(0, datasetsJs.length()-1)+"];";

		String genChartJs = "genChart2('"+title+"', labels, datasets);";

		String js = labelsJs+"\n"+datasetsJs+"\n"+genChartJs+"\n";

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
