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
