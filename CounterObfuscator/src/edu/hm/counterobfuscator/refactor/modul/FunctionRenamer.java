package edu.hm.counterobfuscator.refactor.modul;

import java.util.List;
import javax.script.ScriptException;

import edu.hm.counterobfuscator.mapper.Mapper;
import edu.hm.counterobfuscator.mapper.MapperElement;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.ValueExtractor;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.types.TYPE;

public class FunctionRenamer implements IModul {

	private IProgrammTree			programmTree;
	private List<MapperElement>	mappedElements;
	private String						funcName		= "function";
	private int							funcNumber	= 1;

	public FunctionRenamer(IProgrammTree programmTree) {

		this.programmTree = programmTree;

		this.mappedElements = Mapper.process(programmTree, TYPE.FUNCTION);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.refactor.IRefactor#process()
	 */
	public IProgrammTree process() {

		// for (int j = 0; j < func.getHead().size(); j++) {
		// Variable var = func.getHead().get(j);
		// String newName = funcVarName + varNumber++;
		// mappedNames.put(var.getName(), newName);
		// var.setName(newName);
		// }

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement actualElement = mappedElements.get(i);
			Function function = (Function) actualElement.getElement().getType();

			// nothing to do when function has no name
			if ("".equals(function.getName())) {
				continue;
			}

			List<Element> elementsWithNewName = programmTree.searchForNameOfElement(
					actualElement.getElement(), actualElement.getScope());

			String newName = funcName + funcNumber++;
			function.setName(newName);

			for (int k = 0; k < elementsWithNewName.size(); k++) {

				Element type = elementsWithNewName.get(k);

				ValueExtractor.setValue(type, newName);

			}

		}

		return programmTree;
	}

}
