/**
 * 
 */
package edu.hm.counterobfuscator.refactor;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.client.IClient;
import edu.hm.counterobfuscator.helper.Setting;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.modul.FunctionInterpreter;
import edu.hm.counterobfuscator.refactor.modul.FunctionRenamer;
import edu.hm.counterobfuscator.refactor.modul.FunctionVariableRenamer;
import edu.hm.counterobfuscator.refactor.modul.IModul;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.02.2015
 * 
 *       calls all module which a responsible to refactor functions in the
 *       programm
 */
public class FunctionRefactor implements IRefactor {

	private IProgrammTree	programmTree;
	private Setting			setting;
	private IClient			client;

	public FunctionRefactor(IProgrammTree programmTree, IClient client, Setting setting) {

		this.programmTree = programmTree;
		this.client = client;
		this.setting = setting;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.refactor.IRefactor#process()
	 */
	@Override
	public IProgrammTree process() throws ScriptException {

		IProgrammTree tree = programmTree;
		
		IModul functionInterpreter = new FunctionInterpreter(tree, client);
		tree = functionInterpreter.process();

		if(setting.isConfigured("FunctionRenamer")) {
			IModul renamer = new FunctionRenamer(tree);
			tree = renamer.process();
		}

		if(setting.isConfigured("FunctionVariableRenamer")) {
			IModul variableRenamer = new FunctionVariableRenamer(tree);
			tree = variableRenamer.process();
		}
				
		return tree;

	}

}
