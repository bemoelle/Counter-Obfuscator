package edu.hm.counterobfuscator;

import java.io.IOException;

import javax.script.ScriptException;

import org.apache.commons.codec.EncoderException;

import edu.hm.counterobfuscator.interpreter.IInterpreter;
import edu.hm.counterobfuscator.interpreter.JSInterpreterFactory;
import edu.hm.counterobfuscator.parser.IJSParser;
import edu.hm.counterobfuscator.parser.JSParserFactory;
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
	public static void main(String[] args) throws IOException, ScriptException, IllegalArgumentException, EncoderException {

		IJSParser jsParser = JSParserFactory.create("functionTest");
		
		IProgrammTree tree = jsParser.getProgrammTree();
		
		tree.print(false);
		tree.prettyPrint();

		JSInterpreterFactory.create(jsParser);
		
		tree.prettyPrint();
	}

}
