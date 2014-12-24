package edu.hm.webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public interface IClient {
	
	public HtmlPage getHtmlPage();
	
	public WebClient getWebClient();
	
	public Object getJSResult(String script);
	
	public boolean isUndefined(String script);
	
	boolean isFalse(String script);
	
	//TODO delete this
	public Object executeJS(String string);

}
