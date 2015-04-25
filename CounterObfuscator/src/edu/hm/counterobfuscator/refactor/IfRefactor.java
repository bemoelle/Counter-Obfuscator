/**
 * 
 */
package edu.hm.counterobfuscator.refactor;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.client.IClient;
import edu.hm.counterobfuscator.helper.Setting;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.modul.IfChecker;
import edu.hm.counterobfuscator.refactor.modul.ForLoopChecker;
import edu.hm.counterobfuscator.refactor.modul.ForLoopVariableRenamer;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.02.2015
 * 
 *       calls all module which a responsible to refactor loops in the programm
 */
public class IfRefactor implements IRefactor {

	private IProgrammTree programmTree;
	private Setting setting;
	private IClient client;

	public IfRefactor(IProgrammTree programmTree, IClient client,
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

		IfChecker checker = new IfChecker(programmTree, client);
		return checker.process();

	}

}
