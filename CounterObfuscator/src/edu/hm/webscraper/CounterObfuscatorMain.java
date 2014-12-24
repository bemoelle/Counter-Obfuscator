package edu.hm.webscraper;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.javascript.host.Node;

import edu.hm.webscraper.parser.JSParser;

public class CounterObfuscatorMain {

	public static void main(String[] args) throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		//IClient client = new HTMLUnitClient("http://www.apple.com/", BrowserVersion.CHROME);

		JSParser jsparser = new JSParser(null, "packedTest", null);
		
		jsparser.process();
	}

}
