/**
 * 
 */
package edu.hm.counterobfuscator.interpreter;

import java.io.IOException;
import javax.script.ScriptException;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import edu.hm.counterobfuscator.HTMLUnitClient;
import edu.hm.counterobfuscator.IClient;
import edu.hm.counterobfuscator.parser.IJSParser;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;



/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 * 
 */
public class JSInterpreterFactory {

	/**
	 * @param String
	 *           input
	 * @throws IOException 
	 * @throws ScriptException 
	 */
	public static void create(IJSParser jsParser) throws IOException, ScriptException {

		IClient client = new HTMLUnitClient("http://www.google.com/", BrowserVersion.FIREFOX_24);
		
		IProgrammTree programmTree = jsParser.getProgrammTree();
		
		IInterpreter interpreter = new JSInterpreter(programmTree, client);
		interpreter.process();
		
		IInterpreter jsFunctionRenamer = new JSFunctionRenamer(programmTree, null);
		jsFunctionRenamer.process();
				
		IInterpreter jsVarRenamer = new VariableRenamer(programmTree, null);
		jsVarRenamer.process();
		
		programmTree.prettyPrint();

	}

}