package edu.hm.counterobfuscator.refactor.modul;

import java.util.List;

import edu.hm.counterobfuscator.definitions.DEFINITION;
import edu.hm.counterobfuscator.definitions.Variable;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.mapper.MapperElement;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.04.2015
 * 
 *       replace variable in the programm with there value.
 * 
 *       var var1=0; var2 = var1+var1; result is: var2 = 0+0;
 * 
 */
public class VariableReplacer implements IModul {

	private IProgrammTree			programmTree;
	private List<MapperElement>	mappedElements;
	private Mapper						mapper;

	public VariableReplacer(IProgrammTree programmTree) {

		this.programmTree = programmTree;

		this.mapper = new Mapper(programmTree);
		this.mappedElements = mapper.process(DEFINITION.VARIABLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.interpreter.IInterpreter#process()
	 */
	public IProgrammTree process() {

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement actualElement = mappedElements.get(i);

			// replace not if variable is an object e.g. var test = new Object();
			if (((Variable) actualElement.getElement().getDefinition()).isObject())
				continue;

			String name = actualElement.getElement().getDefinition().getName();
			String value = actualElement.getElement().getDefinition().getValue();

			List<MapperElement> elementsWithName = mapper.searchForNameOfElement(name,
					actualElement.getScope());

			for (int j = 0; j < elementsWithName.size(); j++) {

				Element type = elementsWithName.get(j).getElement();

				if (type.getDefinition().getDefinition() == DEFINITION.VARIABLE
						|| type.getDefinition().getDefinition() == DEFINITION.FOR) {
					type.getDefinition().replaceValueWith(name, value);
				}

			}

		}
		return programmTree;
	}
}
