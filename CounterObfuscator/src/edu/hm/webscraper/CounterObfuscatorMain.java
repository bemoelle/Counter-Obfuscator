package edu.hm.webscraper;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import edu.hm.webscraper.interpreter.JSRenamer;
import edu.hm.webscraper.parser.IJSParser;
import edu.hm.webscraper.parser.JSParserFactory;

public class CounterObfuscatorMain {

	public static void main(String[] args) throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		IJSParser jsParser = JSParserFactory.create("functionTest");
		jsParser.printAllTokens();
		
		JSRenamer jsRenamer = new JSRenamer(jsParser); 
		jsRenamer.process();
	}

}
