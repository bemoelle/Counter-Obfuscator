/**
 * 
 */
package edu.hm.counterobfuscator.refactor;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.helper.Setting;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.modul.FunctionRenamer;
import edu.hm.counterobfuscator.refactor.modul.FunctionVariableRenamer;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.02.2015
 * 
 * 
 */
public class FunctionRefactor implements IRefactor {

	private IProgrammTree	programmTree;
	private Setting			setting;

	public FunctionRefactor(IProgrammTree programmTree, Setting setting) {
		
		this.programmTree = programmTree;
		this.setting = setting;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.refactor.IRefactor#process()
	 */
	@Override
	public IProgrammTree process() throws ScriptException {

		FunctionRenamer renamer = new FunctionRenamer(programmTree);
		IProgrammTree tree = renamer.process();
		
		FunctionVariableRenamer variableRenamer = new FunctionVariableRenamer(tree);
		return variableRenamer.process();
		
	}

}
