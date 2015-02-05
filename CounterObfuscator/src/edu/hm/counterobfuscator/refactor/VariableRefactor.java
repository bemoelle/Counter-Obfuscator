package edu.hm.counterobfuscator.refactor;

import java.util.List;
import javax.script.ScriptException;

import edu.hm.counterobfuscator.helper.Setting;
import edu.hm.counterobfuscator.mapper.Mapper;
import edu.hm.counterobfuscator.mapper.MapperElement;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.modul.IModul;
import edu.hm.counterobfuscator.refactor.modul.VariableRemover;
import edu.hm.counterobfuscator.refactor.modul.VariableRenamer;
import edu.hm.counterobfuscator.refactor.modul.VariableReplacer;
import edu.hm.counterobfuscator.types.TYPE;

public class VariableRefactor implements IRefactor {

	private IProgrammTree			programmTree;
	private List<MapperElement>	mappedElements;
	private Setting					setting;


	public VariableRefactor(IProgrammTree programmTree, Setting setting) {
		
		this.programmTree = programmTree;
		this.setting = setting;

		// TODO refactor to Factory
		Mapper mapper = new Mapper(programmTree, TYPE.VARIABLE);
		mapper.process();
		this.mappedElements = mapper.getMappedElements();

	}

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.interpreter.IInterpreter#process()
	 */
	public IProgrammTree process() throws ScriptException {
	
		IModul varReplacer = new VariableReplacer(programmTree);
		IProgrammTree tree = varReplacer.process();
		
		IModul varRemover = new VariableRemover(tree);
		tree = varRemover.process();
		
		IModul varRenamer = new VariableRenamer(tree);
		return tree = varRenamer.process();
	}

}
