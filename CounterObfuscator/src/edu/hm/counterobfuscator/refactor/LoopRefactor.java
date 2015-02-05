/**
 * 
 */
package edu.hm.counterobfuscator.refactor;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.IClient;
import edu.hm.counterobfuscator.helper.Setting;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.modul.LoopChecker;
import edu.hm.counterobfuscator.refactor.modul.LoopVariableRenamer;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.02.2015
 * 
 * 
 */
public class LoopRefactor implements IRefactor {

	private IProgrammTree	programmTree;
	private Setting			setting;
	private IClient			client;

	public LoopRefactor(IProgrammTree programmTree, IClient client, Setting setting) {

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

		LoopVariableRenamer variableRenamer = new LoopVariableRenamer(programmTree);
		IProgrammTree tree = variableRenamer.process();
		
		LoopChecker checker = new LoopChecker(tree, client);
		return checker.process();

	}

}
