package edu.hm.counterobfuscation.client;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import edu.hm.counterobfuscator.client.HTMLUnitClient;
import edu.hm.counterobfuscator.client.IClient;

public class HTMLUnitTest {

	@Test
	public void getJSResultTest() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		IClient client = new HTMLUnitClient("http://www.google.de", BrowserVersion.FIREFOX_24);
		
		String script = "10+10";
		String result = "";
		
		result = client.getJSResult(script).toString();
		
		assertEquals("20", result);
		
		script = "var test = 10;";
		script += "test+test";
		
		result = client.getJSResult(script).toString();
		
		assertEquals("20", result);
		
	}
	
	@Test
	public void getJSResultDoubleTest() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		IClient client = new HTMLUnitClient("http://www.google.de", BrowserVersion.FIREFOX_24);
		
		String script = "1.0+1.1";
		String result = "";
		
		result = client.getJSResult(script).toString();
		
		assertEquals("2.1", result);
		
		script = "1.0+1.0";
		
		result = client.getJSResult(script).toString();
		
		assertEquals("2", result);

	}
	
	@Test
	public void getJSResultArrayTest() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		IClient client = new HTMLUnitClient("http://www.google.de", BrowserVersion.FIREFOX_24);
		
		String script = "['\\x42\\x42', '\\x41\\x41']";
		String result = "";
		
		result = client.getJSResult(script).toString();
		
		assertEquals("['BB','AA']", result);

	}
	
	@Test
	public void getJSResultObjectTest() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		IClient client = new HTMLUnitClient("http://www.google.de", BrowserVersion.FIREFOX_24);
		
		String script = "var buch = { 'titel':'Ab die Post','isbn':'344254565X','autor':'Pratchet','pubdate':'15.8.2005'};";
		script += "buch.titel;";
		String result = "";
		
		result = client.getJSResult(script).toString();
		
		assertEquals("'Ab die Post'", result);

	}
	
	@Test
	public void getJSResultUndefined() throws FailingHttpStatusCodeException, MalformedURLException, IOException {

			HTMLUnitClient client = new HTMLUnitClient("http://www.google.de", BrowserVersion.FIREFOX_24);
			
			String script = "var test = 10;";
			String result = "";
						
			result = client.getJSResult(script).toString();
			
			assertEquals("undefined", result);
			
		
		
	}

}
