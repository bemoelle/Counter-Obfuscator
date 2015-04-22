package edu.hm.counterobfuscator.refactor.modul;

import java.util.List;

import edu.hm.counterobfuscator.client.IClient;
import edu.hm.counterobfuscator.definitions.DEFINITION;
import edu.hm.counterobfuscator.definitions.Variable;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.mapper.MapperElement;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.04.2015
 * 
 */
public class FunctionInterpreter implements IModul {

	private IProgrammTree programmTree;
	private List<MapperElement> mappedElements;
	private InterpreterModul interpreter;
	private Mapper mapper;
	private IClient client;

	public FunctionInterpreter(IProgrammTree programmTree, IClient client) {

		this.programmTree = programmTree;
		this.client = client;

		interpreter = new InterpreterModul(client, true);

		mapper = new Mapper(programmTree);
		this.mappedElements = mapper.process(DEFINITION.FUNCTION,
				DEFINITION.THIS);// TYPE.VARIABLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.interpreter.IInterpreter#process()
	 */
	public IProgrammTree process() {

//		List<MapperElement> mappedVars = mapper.process(DEFINITION.VARIABLE);
//
//		for (int i = 0; i < mappedVars.size(); i++) {
//
//			Variable var = (Variable) mappedVars.get(i).getElement()
//					.getDefinition();
//			client.getJSResult("var " + var.getName() + "=" + var.getValue());
//		}

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement actualElement = mappedElements.get(i);

			try {
				IProgrammTree newTree = interpreter.process(actualElement
						.getElement());

				if (newTree != null) {
					programmTree.replaceElementWithTree(
							actualElement.getElement(), newTree);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}

		}

		return programmTree;

	}
}
