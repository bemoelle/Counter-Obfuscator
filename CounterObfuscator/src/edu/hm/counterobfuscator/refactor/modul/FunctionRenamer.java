package edu.hm.counterobfuscator.refactor.modul;

import java.util.List;

import edu.hm.counterobfuscator.definitions.DEFINITION;
import edu.hm.counterobfuscator.definitions.Function;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.mapper.MapperElement;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.04.2015
 * 
 *       renames functions
 * 
 */
public class FunctionRenamer implements IModul {

	private IProgrammTree			programmTree;
	private List<MapperElement>	mappedElements;
	private String						functionName	= "function";
	private String						objectName		= "object";
	private int							funcNumber		= 1;
	private int							objectNumber	= 1;
	private Mapper						mapper;

	public FunctionRenamer(IProgrammTree programmTree) {

		this.programmTree = programmTree;
		this.mapper = new Mapper(programmTree);

		this.mappedElements = mapper.process(DEFINITION.FUNCTION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.refactor.IRefactor#process()
	 */
	public IProgrammTree process() {

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement actualElement = mappedElements.get(i);
			Function function = (Function) actualElement.getElement().getDefinition();

			// nothing to do when function has no name
			if ("".equals(function.getName())) {
				continue;
			}

			List<MapperElement> elementsWithOldName = mapper.searchForNameOfElement(
					function.getName(), actualElement.getScope());

			String oldName = function.getName();
			String newName = "";

			if (elementsWithOldName.size() == 0) {
				newName = functionName + funcNumber++;
			} else {
				newName = objectName + objectNumber++;
			}

			function.setName(newName);

			for (int k = 0; k < elementsWithOldName.size(); k++) {

				Element type = elementsWithOldName.get(k).getElement();

				type.getDefinition().replaceValueWith(oldName, newName);

			}

		}

		return programmTree;
	}

}
