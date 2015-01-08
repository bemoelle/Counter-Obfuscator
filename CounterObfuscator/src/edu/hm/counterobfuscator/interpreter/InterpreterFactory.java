/**
 * 
 */
package edu.hm.counterobfuscator.interpreter;

import java.io.IOException;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.IClient;
import edu.hm.counterobfuscator.parser.IJSParser;
import edu.hm.counterobfuscator.parser.token.trees.ITypeTree;



/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 * 
 */
public class InterpreterFactory {

	/**
	 * @param String
	 *           input
	 * @return TokenAnalyser
	 * @throws IOException 
	 * @throws ScriptException 
	 */
	public static void create(IJSParser jsParser, IClient client) throws IOException, ScriptException {

		IInterpreter interpreter = new JSInterpreter(jsParser, client);
		ITypeTree programmTree = interpreter.process();
		
		IInterpreter jsVarRenamer = new JSVarRenamer(programmTree);
		jsVarRenamer.process();
		
	}

}
