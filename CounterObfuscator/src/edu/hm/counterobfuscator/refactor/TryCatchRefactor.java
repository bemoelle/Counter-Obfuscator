/**
 * 
 */
package edu.hm.counterobfuscator.refactor;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.client.IClient;
import edu.hm.counterobfuscator.helper.Setting;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.modul.IModul;
import edu.hm.counterobfuscator.refactor.modul.TryCatchChecker;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.02.2015
 * 
 * 
 */
public class TryCatchRefactor implements IRefactor {

	private IProgrammTree	programmTree;
	private Setting			setting;
	private IClient			client;

	public TryCatchRefactor(IProgrammTree programmTree, IClient client, Setting setting) {

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

		IModul checker = new TryCatchChecker(programmTree, client);
		return checker.process();
	}

}
