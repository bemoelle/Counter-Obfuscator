/**
 * 
 */
package edu.hm.counterobfuscator.refactor;

import java.io.IOException;

import javax.script.ScriptException;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import edu.hm.counterobfuscator.HTMLUnitClient;
import edu.hm.counterobfuscator.IClient;
import edu.hm.counterobfuscator.parser.IJSParser;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.modul.VariableRemover;
import edu.hm.counterobfuscator.refactor.modul.VariableRenamer;
import edu.hm.counterobfuscator.refactor.modul.VariableReplacer;



/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 * 
 */
public class RefactorFactory {

	/**
	 * @param String
	 *           input
	 * @throws IOException 
	 * @throws ScriptException 
	 */
	public static void create(IJSParser jsParser) throws IOException, ScriptException {

		IClient client = new HTMLUnitClient("http://www.google.com/", BrowserVersion.FIREFOX_24);
		
		IProgrammTree programmTree = jsParser.getProgrammTree();
		
		//TODO read settings
			
		IRefactor interpreter = new InterpreterRefactor(programmTree, client);
		IProgrammTree tree = interpreter.process();
		
		IRefactor variableRefactor = new VariableRefactor(tree, null);
		tree = variableRefactor.process();
		
		IRefactor functionRefactor = new FunctionRefactor(tree, null);
		tree = functionRefactor.process();
		
		IRefactor loopRefactor = new LoopRefactor(tree, client, null);
		tree = loopRefactor.process();
				
		tree.prettyPrint(true);


	}

}
