package up.visulog.webgen;

import htmlflow.HtmlView;
import htmlflow.StaticHtml;

public class WebGen {
	private HtmlView view = StaticHtml.view();
	
	public void open() {
		//TO DO : add head (with the link to the css, and title, ...)
		view.html().body();
	}
	
	public void close() {
		//Close the html and body
		view.__().__();
	}
	
}
