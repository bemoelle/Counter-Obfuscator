/**
 * 
 */
package edu.hm.counterobfuscator.refactor;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.client.IClient;
import edu.hm.counterobfuscator.helper.Setting;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.modul.ForLoopChecker;
import edu.hm.counterobfuscator.refactor.modul.LoopVariableRenamer;
import edu.hm.counterobfuscator.refactor.modul.WhileLoopChecker;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.02.2015
 * 
 *       calls all module which a responsible to refactor loops in the programm
 */
public class LoopRefactor implements IRefactor {

	private IProgrammTree programmTree;
	private Setting setting;
	private IClient client;

	public LoopRefactor(IProgrammTree programmTree, IClient client,
			Setting setting) {

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

		LoopVariableRenamer variableRenamer = new LoopVariableRenamer(tree);
		tree = variableRenamer.process();

		ForLoopChecker forChecker = new ForLoopChecker(tree, client);
		tree = forChecker.process();

		WhileLoopChecker whileChecker = new WhileLoopChecker(tree, client);
		tree = whileChecker.process();

		return tree;
	}

}
