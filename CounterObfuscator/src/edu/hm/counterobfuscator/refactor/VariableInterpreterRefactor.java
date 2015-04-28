package edu.hm.counterobfuscator.refactor;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.client.IClient;
import edu.hm.counterobfuscator.helper.Setting;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.modul.IModul;
import edu.hm.counterobfuscator.refactor.modul.VariableInterpreter;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.04.2015
 * 
 *       calls all modules which a responsible to refactor variables in the
 *       programm
 */
public class VariableInterpreterRefactor implements IRefactor {

	private IProgrammTree	programmTree;
	private IClient			client;

	public VariableInterpreterRefactor(IProgrammTree programmTree, IClient client, Setting setting) {

		this.programmTree = programmTree;
		this.client = client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.interpreter.IInterpreter#process()
	 */
	public IProgrammTree process() throws ScriptException {

		IProgrammTree tree = programmTree;

		IModul varInterpreter = new VariableInterpreter(tree, client);
		tree = varInterpreter.process();

		return tree;
	}

}
