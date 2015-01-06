package edu.hm.counterobfuscator;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;

import org.mozilla.javascript.NativeArray;

import net.sourceforge.htmlunit.corejs.javascript.IdFunctionObject;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

public class HTMLUnitClient implements IClient {

	private WebClient				webClient;
	private HtmlPage				currentPage;
	private JavaScriptEngine	engine;

	public HTMLUnitClient(String url, BrowserVersion browser) throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		// TODO refactor as SINGELTON of FACTORY
		webClient = new WebClient(browser);
		engine = new JavaScriptEngine(webClient);
		webClient.setJavaScriptEngine(engine);

		webClient.getOptions().setThrowExceptionOnScriptError(true);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getCookieManager().setCookiesEnabled(true);

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
		if (result.getClass() == Window.class) {
			return "window";
		}
		else if (result.getClass() == IdFunctionObject.class) {
			return ((IdFunctionObject) result).getFunctionName();
		}
		else if (result.getClass() == String.class) {
			return "'" + result + "'";
		}
		else if (result.getClass() == Double.class) {
			return result;
		}
		else if (result.getClass() == net.sourceforge.htmlunit.corejs.javascript.NativeArray.class) {
			
			net.sourceforge.htmlunit.corejs.javascript.NativeArray array = (net.sourceforge.htmlunit.corejs.javascript.NativeArray)result;
			
			Object[] test = array.toArray();		
			
			Object test2 = "[";
			
			for(int i=0; i<test.length; i++) {
				String o = (String) test[i];
				test2 += "'"+o+"'";
				if(i < test.length-1)
					test2 += ",";
			}
			
			
			test2 += "]";
					
			return test2;
		}
		else {
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
