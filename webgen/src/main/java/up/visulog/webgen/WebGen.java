package up.visulog.webgen;

import java.util.HashMap;
import java.util.Map;

import org.xmlet.htmlapifaster.Body;
import org.xmlet.htmlapifaster.Html;

import htmlflow.HtmlView;
import htmlflow.StaticHtml;

public class WebGen {
	//Ajouter le head
	private Body<Html<HtmlView>> view = StaticHtml.view().html().body();
	
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
	
	//Faire la fonction write en utilisant getHtml()
	
	
	
	
}
