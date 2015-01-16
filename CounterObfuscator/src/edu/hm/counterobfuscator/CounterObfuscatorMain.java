package edu.hm.counterobfuscator;

import java.io.IOException;
import javax.script.ScriptException;

import edu.hm.counterobfuscator.interpreter.InterpreterFactory;
import edu.hm.counterobfuscator.parser.IJSParser;
import edu.hm.counterobfuscator.parser.JSParserFactory;

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
	 */
	public static void main(String[] args) throws IOException, ScriptException {

		IJSParser jsParser = JSParserFactory.create("functionTest");

		InterpreterFactory.create(jsParser);
	}

}
