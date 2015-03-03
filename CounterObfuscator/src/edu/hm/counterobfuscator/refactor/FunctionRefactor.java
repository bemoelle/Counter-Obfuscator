/**
 * 
 */
package edu.hm.counterobfuscator.refactor;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.client.IClient;
import edu.hm.counterobfuscator.helper.Setting;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.modul.DotNotation;
import edu.hm.counterobfuscator.refactor.modul.FunctionInterpreter;
import edu.hm.counterobfuscator.refactor.modul.FunctionRenamer;
import edu.hm.counterobfuscator.refactor.modul.FunctionVariableRenamer;
import edu.hm.counterobfuscator.refactor.modul.IModul;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.02.2015
 * 
 * 
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

		IModul functionInterpreter = new FunctionInterpreter(programmTree, client);
		IProgrammTree tree = functionInterpreter.process();

		IModul renamer = new FunctionRenamer(tree);
		tree = renamer.process();

		IModul variableRenamer = new FunctionVariableRenamer(tree);
		tree = variableRenamer.process();
		
		IModul dotNotation = new DotNotation(tree);
		return dotNotation.process();

	}

}
