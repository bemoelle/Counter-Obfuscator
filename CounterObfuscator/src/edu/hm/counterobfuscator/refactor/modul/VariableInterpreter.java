package edu.hm.counterobfuscator.refactor.modul;

import java.util.List;

import edu.hm.counterobfuscator.IClient;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.mapper.MapperElement;
import edu.hm.counterobfuscator.types.TYPE;

public class VariableInterpreter implements IModul {

	private IProgrammTree			programmTree;
	private List<MapperElement>	mappedElements;
	private InterpreterModul		interpreter;

	public VariableInterpreter(IProgrammTree programmTree, IClient client) {

		this.programmTree = programmTree;

		interpreter = new InterpreterModul(client, true);

		Mapper mapper = new Mapper(programmTree);
		this.mappedElements = mapper.process( TYPE.VARIABLE, TYPE.THIS, TYPE.CALL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.interpreter.IInterpreter#process()
	 */
	public IProgrammTree process() {

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement actualElement = mappedElements.get(i);

			try {
				interpreter.process(actualElement.getElement());
			}
			catch (Exception e) {

				e.printStackTrace();
			}

		}

		return programmTree;

	}
}
