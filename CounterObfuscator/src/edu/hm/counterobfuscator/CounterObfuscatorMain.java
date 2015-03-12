package edu.hm.counterobfuscator;

import java.io.IOException;

import javax.script.ScriptException;

import org.apache.commons.codec.EncoderException;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import edu.hm.counterobfuscator.client.HTMLUnitClient;
import edu.hm.counterobfuscator.client.IClient;
import edu.hm.counterobfuscator.helper.Setting;
import edu.hm.counterobfuscator.parser.IParser;
import edu.hm.counterobfuscator.parser.ParserFactory;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.RefactorFactory;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 13.01.2015
 * 
 * 
 */
public class CounterObfuscatorMain {

	/**
	 * @param args
	 * @throws IOException
	 * @throws ScriptException
	 * @throws EncoderException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws IOException, ScriptException, IllegalArgumentException, EncoderException {

		IParser jsParser = ParserFactory.create("varsTest", true);
		
		IProgrammTree tree = jsParser.getProgrammTree();
		
	//	tree.print();
		
//				
		RefactorFactory.create(jsParser);
		
//		IClient test = new HTMLUnitClient("http://www.google.com/", BrowserVersion.FIREFOX_24);
//		System.out.println(test.getJSResult("1.0+1.1"));
	}

}
