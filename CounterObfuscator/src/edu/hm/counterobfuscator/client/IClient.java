package edu.hm.counterobfuscator.client;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public interface IClient {

	/**
	 * @return the called HTML Page
	 */
	public HtmlPage getHtmlPage();

	/**
	 * @return the selected webclient
	 */
	public WebClient getWebClient();

	/**
	 * @param script
	 * @return the result as Object.class of the committed script
	 */
	public Object getJSResult(String script);
}
