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
	public static void create(IJSParser jsParser) throws IOException, ScriptException {

		IClient client = new HTMLUnitClient("http://www.google.com/", BrowserVersion.FIREFOX_24);
		
		IProgrammTree programmTree = jsParser.getProgrammTree();
		
		
		
		IRefactor interpreter = new Refactor(programmTree, client);
		interpreter.process();
		
//		IRefactor jsFunctionRenamer = new JSFunctionRenamer(programmTree, null);
//		jsFunctionRenamer.process();
				
		IRefactor varReplacer = new VariableReplacer(programmTree, null);
		varReplacer.process();
		
		IRefactor varRemover = new VariableRemover(programmTree, null);
		IProgrammTree removed = varRemover.process();
		
		IRefactor varRenamer = new VariableRenamer(removed, null);
		IProgrammTree renamed = varRenamer.process();
		renamed.prettyPrint();


	}

}
