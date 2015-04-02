package edu.hm.counterobfuscator.refactor.modul;

import java.util.List;

import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.mapper.MapperElement;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.types.TYPE;
import edu.hm.counterobfuscator.types.Variable;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.02.2015
 * 
 * 
 */
public class FunctionVariableRenamer implements IModul {

	private IProgrammTree			programmTree;
	private List<MapperElement>	mappedElements;
	private int							number	= 1;

	public FunctionVariableRenamer(IProgrammTree programmTree) {

		this.programmTree = programmTree;

		Mapper mapper = new Mapper(programmTree);
		this.mappedElements = mapper.process(TYPE.FUNCTION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.refactor.IRefactor#process()
	 */
	public IProgrammTree process() {

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement actualElement = mappedElements.get(i);
			Function function = (Function) actualElement.getElement().getType();
			IProgrammTree children = actualElement.getElement().getChildren();

			List<Variable> vars = function.getHead();

			// nothing to do when function has no head variables
			if (vars.size() == 0) {
				continue;
			}

			for (int j = 0; j < vars.size(); j++) {

				// only look at the children
				Mapper childrenMapper = new Mapper(children);
				List<MapperElement> elementsWithNewName = childrenMapper.searchForName(vars.get(j).getName());

				String newName = "functionVar" + number++;

				for (int k = 0; k < elementsWithNewName.size(); k++) {

					Element type = elementsWithNewName.get(k).getElement();

					type.getType().replaceNameWith(vars.get(j).getName(), newName);
					
//					ValueExtractor.setName(type,
//							ValueExtractor.getName(type).replaceAll(vars.get(j).getName(), newName));
//					ValueExtractor.setValue(type,
//							ValueExtractor.getValue(type).replaceAll(vars.get(j).getName(), newName));

				}

				vars.get(j).setName(newName);
			}

		}

		return programmTree;
	}
}
