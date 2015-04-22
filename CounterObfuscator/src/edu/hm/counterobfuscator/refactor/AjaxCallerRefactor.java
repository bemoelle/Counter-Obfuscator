package edu.hm.counterobfuscator.refactor;

import java.io.IOException;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.client.IClient;
import edu.hm.counterobfuscator.helper.Setting;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.modul.AjaxCaller;
import edu.hm.counterobfuscator.refactor.modul.DotNotation;
import edu.hm.counterobfuscator.refactor.modul.IModul;
import edu.hm.counterobfuscator.refactor.modul.VariableInterpreter;
import edu.hm.counterobfuscator.refactor.modul.VariableRemover;
import edu.hm.counterobfuscator.refactor.modul.VariableRenamer;
import edu.hm.counterobfuscator.refactor.modul.VariableReplacer;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.04.2015
 * 
 *       calls all module which a responsible to refactor variables in the
 *       programm
 */
public class AjaxCallerRefactor implements IRefactor {

	private IProgrammTree programmTree;
	private Setting setting;
	private IClient client;

	public AjaxCallerRefactor(IProgrammTree programmTree, IClient client,
			Setting setting) {

		this.programmTree = programmTree;
		this.client = client;
		this.setting = setting;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.interpreter.IInterpreter#process()
	 */
	public IProgrammTree process() throws ScriptException {

		IProgrammTree tree = programmTree;

		IModul ajaxCaller = null;
		try {
			ajaxCaller = new AjaxCaller(tree, client);
		} catch (IOException e) {
			e.printStackTrace();
		}
		tree = ajaxCaller.process();

		return tree;
	}

}
