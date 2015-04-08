/**
 * 
 */
package edu.hm.counterobfuscator.refactor;

import java.io.IOException;

import javax.script.ScriptException;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import edu.hm.counterobfuscator.client.HTMLUnitClient;
import edu.hm.counterobfuscator.client.IClient;
import edu.hm.counterobfuscator.helper.Setting;
import edu.hm.counterobfuscator.parser.IParser;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 *       factory class to create all necesaary refactor steps
 */
public class RefactorFactory {

	/**
	 * @param String
	 *           input
	 * @throws IOException
	 * @throws ScriptException
	 */
	public static IProgrammTree create(IParser jsParser, String url) throws IOException,
			ScriptException {

		IClient client = new HTMLUnitClient(url, BrowserVersion.FIREFOX_24);

		IProgrammTree tree = jsParser.getProgrammTree();
		Setting settings = new Setting();

		IRefactor functionRefactor = new FunctionRefactor(tree, client, settings);
		tree = functionRefactor.process();

		IRefactor variableRefactor = new VariableRefactor(tree, client, settings);
		tree = variableRefactor.process();

		IRefactor tryCatchRefactor = new TryCatchRefactor(tree, client, settings);
		tree = tryCatchRefactor.process();

		IRefactor loopRefactor = new LoopRefactor(tree, client, settings);
		tree = loopRefactor.process();

		IRefactor ajaxCaller = new AjaxCallerRefactor(tree, client, settings);
		tree = ajaxCaller.process();

		return tree;

	}

}
