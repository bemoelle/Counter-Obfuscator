package edu.hm.counterobfuscator;

import java.io.IOException;

import javax.script.ScriptException;

import org.apache.commons.codec.EncoderException;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import edu.hm.counterobfuscator.client.HTMLUnitClient;
import edu.hm.counterobfuscator.client.IClient;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;

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
	public static void main(String[] args) throws IOException, ScriptException,
			IllegalArgumentException, EncoderException {

		DeObfuscatorFactory deObfuscatorFactory = new DeObfuscatorFactory("src/Test", true,
				"http://www.daengeligeist.de/");

		IProgrammTree resultTree = deObfuscatorFactory.create();

		resultTree.printOnConsole();
		
		
//		IClient client = new HTMLUnitClient("http://www.google.de", BrowserVersion.FIREFOX_24);
//		
//		System.out.println(client.getJSResult("eval('(function(a){return a+a;})(1)')"));
//		

	}

}
