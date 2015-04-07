package edu.hm.counterobfuscator.refactor.modul;

import java.util.List;

import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.mapper.MapperElement;
import edu.hm.counterobfuscator.types.DEFINITION;

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

	private IProgrammTree programmTree;
	private List<MapperElement> mappedElements;
	private Mapper mapper;

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

			String name = actualElement.getElement().getType().getName();
			String value = actualElement.getElement().getType().getValue();

			List<MapperElement> elementsWithName = mapper
					.searchForNameOfElement(name, actualElement.getScope());

			for (int j = 0; j < elementsWithName.size(); j++) {

				Element type = elementsWithName.get(j).getElement();

				type.getType().replaceNameWith(name, value);

				// String toReplace = ValueExtractor.getValue(type);
				//
				// // hack if not ++ or --
				// if (!toReplace.contains(name + "++")) {
				// toReplace = toReplace.replace(name, value);
				// ValueExtractor.setValue(type, toReplace);
				// }
			}

			// //is AssoArray
			// if(value.matches("\\{.*\\}")) {
			//
			// System.out.println("sdsdsdsdsd");
			// }
			//
			// List<MapperElement> elementsWithOldName =
			// mapper.searchForNameOfElement(
			// actualElement.getElement(), actualElement.getScope());
			// //
			// //TODO not same in list<MapperElements>
			// for (int j = 0; j < elementsWithOldName.size(); j++) {
			//
			// Element type = elementsWithOldName.get(j).getElement();
			// String toReplace = ValueExtractor.getValue(type);
			//
			// //hack if not ++ or --
			// if(!toReplace.contains(name+"++")) {
			// toReplace = toReplace.replace(name, value);
			// ValueExtractor.setValue(type, toReplace);
			// }
			// }
		}
		return programmTree;
	}
}
