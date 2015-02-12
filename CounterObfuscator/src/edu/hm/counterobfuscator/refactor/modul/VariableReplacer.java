package edu.hm.counterobfuscator.refactor.modul;

import java.util.List;

import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.ValueExtractor;
import edu.hm.counterobfuscator.parser.tree.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.mapper.MapperElement;
import edu.hm.counterobfuscator.types.TYPE;

public class VariableReplacer implements IModul {

	private IProgrammTree			programmTree;
	private List<MapperElement>	mappedElements;
	private Mapper mapper;

	public VariableReplacer(IProgrammTree programmTree) {

		this.programmTree = programmTree;

		this.mapper = new Mapper(programmTree);
		this.mappedElements = mapper.process( TYPE.VARIABLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.interpreter.IInterpreter#process()
	 */
	public IProgrammTree process() {

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement actualElement = mappedElements.get(i);

			String name = ValueExtractor.getName(actualElement.getElement());
			String value = ValueExtractor.getValue(actualElement.getElement());

			List<MapperElement> elementsWithOldName = mapper.searchForNameOfElement(
					actualElement.getElement(), actualElement.getScope());

			for (int j = 0; j < elementsWithOldName.size(); j++) {

				Element type = elementsWithOldName.get(j).getElement();
				String toReplace = ValueExtractor.getValue(type);
				toReplace = toReplace.replace(name, value);
				ValueExtractor.setValue(type, toReplace);
			}
		}
		return programmTree;
	}
}
