package edu.hm.counterobfuscator.refactor;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.IClient;
import edu.hm.counterobfuscator.helper.Setting;
import edu.hm.counterobfuscator.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.modul.IModul;
import edu.hm.counterobfuscator.refactor.modul.VariableInterpreter;
import edu.hm.counterobfuscator.refactor.modul.VariableRemover;
import edu.hm.counterobfuscator.refactor.modul.VariableRenamer;
import edu.hm.counterobfuscator.refactor.modul.VariableReplacer;
import edu.hm.counterobfuscator.types.TYPE;

public class VariableRefactor implements IRefactor {

	private IProgrammTree	programmTree;
	private Setting			setting;
	private IClient			client;

	public VariableRefactor(IProgrammTree programmTree, IClient client, Setting setting) {

		this.programmTree = programmTree;
		this.client = client;
		this.setting = setting;

		Mapper.process(programmTree, TYPE.VARIABLE);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.interpreter.IInterpreter#process()
	 */
	public IProgrammTree process() throws ScriptException {

		IModul varInterpreter = new VariableInterpreter(programmTree, client);
		IProgrammTree tree = varInterpreter.process();

		IModul varReplacer = new VariableReplacer(tree);
		tree = varReplacer.process();

		IModul varRemover = new VariableRemover(tree);
		tree = varRemover.process();

		IModul varRenamer = new VariableRenamer(tree);
		tree = varRenamer.process();

		return tree;
	}

}
