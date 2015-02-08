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
		
		IProgrammTree tree = jsParser.getProgrammTree();
		
		//TODO read settings
		
		IRefactor variableRefactor = new VariableRefactor(tree, client, null);
		tree = variableRefactor.process();
		
		IRefactor tryCatchRefactor = new TryCatchRefactor(tree, client, null);
		tree = tryCatchRefactor.process();
		
		IRefactor functionRefactor = new FunctionRefactor(tree, client, null);
		tree = functionRefactor.process();
		
		IRefactor loopRefactor = new LoopRefactor(tree, client, null);
		tree = loopRefactor.process();
						
		tree.prettyPrint();

		return tree;
		
	}

}
