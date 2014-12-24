package edu.hm.webscraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.IdFunctionObject;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

public class HTMLUnitClient implements IClient {

	private WebClient				webClient;
	private HtmlPage				currentPage;
	private JavaScriptEngine	engine;

	public HTMLUnitClient(String url, BrowserVersion browser)  throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		// TODO refactor as SINGELTON of FACTORY
		webClient = new WebClient(browser);
		engine = new JavaScriptEngine(webClient);
		webClient.setJavaScriptEngine(engine);

		webClient.getOptions().setThrowExceptionOnScriptError(true);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);

		currentPage = webClient.getPage(url);
	}

	public HtmlPage getHtmlPage() {
		return currentPage;
	}

	public WebClient getWebClient() {
		return webClient;
	}

	public JavaScriptEngine getEngine() {
		return engine;
	}
	
	public ScriptResult executeJS(String script) { 
		return currentPage.executeJavaScript(script); 		
	}
	
	public Object getJSResult(String script) {
		Object result = executeJS(script).getJavaScriptResult();
		if(result.getClass() == Window.class) {
			return "window";
		} else if(result.getClass() == IdFunctionObject.class) {
			return ((IdFunctionObject) result).getFunctionName();
		} else {
			return result;
		}	
	}
	
	public boolean isUndefined(String script) {
		return ScriptResult.isUndefined(executeJS(script));
	}
	
	public boolean isFalse(String script) {
		return ScriptResult.isFalse(executeJS(script));
	}


}
