package edu.hm.counterobfuscator;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import edu.hm.counterobfuscator.interpreter.IInterpreter;
import edu.hm.counterobfuscator.interpreter.JSRenamer;
import edu.hm.counterobfuscator.parser.IJSParser;
import edu.hm.counterobfuscator.parser.JSParserFactory;

public class CounterObfuscatorMain {

	public static void main(String[] args) throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		IJSParser jsParser = JSParserFactory.create("functionTest");
		jsParser.printAllTokens();
		
		IClient client = new HTMLUnitClient("http://www.google.com/", BrowserVersion.FIREFOX_24);
		
		IInterpreter jsRenamer = new JSRenamer(jsParser, client); 
		jsRenamer.process();
		
		//jsParser.printAllTokens();
	}

}
