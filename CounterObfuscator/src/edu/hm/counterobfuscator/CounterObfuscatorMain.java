package edu.hm.counterobfuscator;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.script.ScriptException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import edu.hm.counterobfuscator.interpreter.IInterpreter;
import edu.hm.counterobfuscator.interpreter.JSInterpreter;
import edu.hm.counterobfuscator.parser.IJSParser;
import edu.hm.counterobfuscator.parser.JSParserFactory;

public class CounterObfuscatorMain {

	public static void main(String[] args) throws FailingHttpStatusCodeException,
			MalformedURLException, IOException, ScriptException {
		
		IJSParser jsParser = JSParserFactory.create("functionTest");

		IClient client = new HTMLUnitClient("http://www.google.com/", BrowserVersion.FIREFOX_24);
	
		IInterpreter jsRenamer = new JSInterpreter(jsParser, client); 
		jsRenamer.process();
		
		
		
	}

}
