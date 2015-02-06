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
	public static IProgrammTree create(IJSParser jsParser) throws IOException, ScriptException {

		IClient client = new HTMLUnitClient("http://www.google.com/", BrowserVersion.FIREFOX_24);
		
		IProgrammTree programmTree = jsParser.getProgrammTree().flatten();
		
		//TODO read settings
		
		IRefactor variableRefactor = new VariableRefactor(programmTree, client, null);
		IProgrammTree tree = variableRefactor.process();
		
		IRefactor tryCatchRefactor = new TryCatchRefactor(tree, client, null);
		tree = tryCatchRefactor.process();
		
		IRefactor functionRefactor = new FunctionRefactor(tree, client, null);
		tree = functionRefactor.process();
		
		IRefactor loopRefactor = new LoopRefactor(tree, client, null);
		tree = loopRefactor.process();
		
		
				
		tree.prettyPrint(true);

		return tree;
		
	}

}
