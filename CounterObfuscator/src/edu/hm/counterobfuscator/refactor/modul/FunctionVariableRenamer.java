package edu.hm.counterobfuscator.refactor.modul;

import java.util.Iterator;
import java.util.List;

import edu.hm.counterobfuscator.definitions.DEFINITION;
import edu.hm.counterobfuscator.definitions.Function;
import edu.hm.counterobfuscator.definitions.Variable;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.mapper.MapperElement;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.02.2015
 * 
 *       renames the variable within a function
 */
public class FunctionVariableRenamer implements IModul {

	private IProgrammTree programmTree;
	private List<MapperElement> mappedElements;
	private int number = 1;

	public FunctionVariableRenamer(IProgrammTree programmTree) {

		this.programmTree = programmTree;

		Mapper mapper = new Mapper(programmTree);
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
			IProgrammTree children = actualElement.getElement().getChildren();

			List<Variable> vars = function.getHead();

			// nothing to do when function has no head variables
			if (vars.size() == 0) {
				continue;
			}

			for (int j = 0; j < vars.size(); j++) {

				// only look at the children
//				Mapper childrenMapper = new Mapper(children);
//				List<MapperElement> elementsWithNewName = childrenMapper
//						.searchForName(vars.get(j).getName());
				
				String newName = "functionVar" + number++;
				
				Iterator<Element> it = children.iterator();

				while (it.hasNext()) {
								
					Element element = it.next();
					element.getDefinition().replaceNameWith(vars.get(j).getName(),
							newName);
					
				}
				
				vars.get(j).setName(newName);
				
				

				

//				for (int k = 0; k < elementsWithNewName.size(); k++) {
//
//					Element type = elementsWithNewName.get(k).getElement();
//
//					type.getDefinition().replaceNameWith(vars.get(j).getName(),
//							newName);
//				}
//
//				vars.get(j).setName(newName);
			}

		}

		return programmTree;
	}
}
