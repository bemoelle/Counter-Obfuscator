package edu.hm.counterobfuscator.refactor.modul;

import java.util.List;

import edu.hm.counterobfuscator.definitions.DEFINITION;
import edu.hm.counterobfuscator.definitions.ForWhile;
import edu.hm.counterobfuscator.definitions.Variable;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.mapper.MapperElement;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.02.2015
 * 
 * renames variable in loop body which a defined in loop head
 */
public class LoopVariableRenamer implements IModul {

	private IProgrammTree			programmTree;
	private List<MapperElement>	mappedElements;
	private int							number	= 1;

	public LoopVariableRenamer(IProgrammTree programmTree) {

		this.programmTree = programmTree;

		Mapper mapper = new Mapper(programmTree);
		this.mappedElements = mapper.process(DEFINITION.FOR);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.refactor.IRefactor#process()
	 */
	public IProgrammTree process() {

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement actualElement = mappedElements.get(i);
			ForWhile loop = (ForWhile) actualElement.getElement().getDefinition();
			IProgrammTree children = actualElement.getElement().getChildren();

			Variable var = loop.getHead();

			// only look in the children
			Mapper childrenMapper = new Mapper(children);
			List<MapperElement> elementsWithNewName = childrenMapper.searchForName(var.getName());

			String newName = "forVar" + number++;

			for (int k = 0; k < elementsWithNewName.size(); k++) {

				Element type = elementsWithNewName.get(k).getElement();
				
				type.getDefinition().replaceValueWith(var.getName(), newName);

//				ValueExtractor.setName(type,
//						ValueExtractor.getName(type).replaceAll(var.getName(), newName));
//				ValueExtractor.setValue(type,
//						ValueExtractor.getValue(type).replaceAll(var.getName(), newName));

			}

			loop.replaceValueWith(var.getName(), newName);
//			loop.setHeadString(loop.getHeadString().replaceAll(var.getName(), newName));

		}

		return programmTree;
	}
}
