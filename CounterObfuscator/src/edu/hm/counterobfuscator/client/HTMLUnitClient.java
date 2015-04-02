package edu.hm.counterobfuscator.client;

import java.io.IOException;
import java.net.MalformedURLException;
import net.sourceforge.htmlunit.corejs.javascript.IdFunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.WindowProxy;

public class HTMLUnitClient implements IClient {

	private WebClient				webClient;
	private HtmlPage				currentPage;
	private JavaScriptEngine	engine;

	public HTMLUnitClient(String url, BrowserVersion browser) throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

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

	public Object getJSResult(String script) {

		Object test = executeJS(script);
		Object result = ((ScriptResult) test).getJavaScriptResult();
		if (result.getClass() == Window.class || result.getClass() == WindowProxy.class) {
			return "window";
		} else if (result.getClass() == IdFunctionObject.class) {
			return ((IdFunctionObject) result).getFunctionName();
		} else if (result.getClass() == String.class) {
			return "'" + result + "'";
		} else if (result.getClass() == Double.class) {
			
			double doubleResult = Double.parseDouble(result.toString());
			double doubleResultDecimalPlaces = (doubleResult *100) % 100;
			
			if(doubleResultDecimalPlaces == 0) {
				result = (int)doubleResult;
			}
			
			return result;
		} else if (result.getClass() == Integer.class) {
			return result;
		} else if (result.getClass() == NativeArray.class) {

			NativeArray array = (NativeArray) result;

			Object[] arrayToProcess = array.toArray();

			Object resultArray = "[";

			for (int i = 0; i < arrayToProcess.length; i++) {
				String o = (String) arrayToProcess[i];
				resultArray += "'" + o + "'";
				if (i < arrayToProcess.length - 1)
					resultArray += ",";
			}

			resultArray += "]";

			return resultArray;
			
		} else if(result.getClass() == NativeObject.class) {
		
			return processNativeObject((NativeObject) result);
			
		} else {
			return result;
		}
	}
	
	private ScriptResult executeJS(String script) {

		return currentPage.executeJavaScript(script);
	}
	
	private String processNativeObject(NativeObject object) {
		
		String result = "{";
		
		Object values[] = object.getAllIds();
		
		for(int i=0; i<values.length; i++) {
			String key = values[i].toString();
			Object value = object.get(values[i]);
			String valueString = "";
			
			if(value.getClass() == NativeObject.class) {
				valueString += processNativeObject((NativeObject)value);				
			} else {
				valueString = value.toString();
				valueString = valueString.replaceAll("\\n", "");
				if(value.getClass() == String.class) {
					valueString = "'" + valueString + "'";
				} 				
			}
			
			result += "'" + key + "' :" + valueString;
			
			if(i != values.length-1) {
				result += ",";
			}
		}
	
		result += "}";		
		
		return result;
	}
}
