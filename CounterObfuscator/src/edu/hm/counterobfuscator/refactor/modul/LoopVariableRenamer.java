package edu.hm.counterobfuscator.refactor.modul;

import java.util.List;

import edu.hm.counterobfuscator.mapper.Mapper;
import edu.hm.counterobfuscator.mapper.MapperElement;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.ValueExtractor;
import edu.hm.counterobfuscator.types.ForWhile;
import edu.hm.counterobfuscator.types.TYPE;
import edu.hm.counterobfuscator.types.Variable;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.02.2015
 * 
 * 
 */
public class LoopVariableRenamer implements IModul {

	private IProgrammTree			programmTree;
	private List<MapperElement>	mappedElements;
	private int							number	= 1;

	public LoopVariableRenamer(IProgrammTree programmTree) {

		this.programmTree = programmTree;

		// TODO refactor to Factory
		Mapper mapper = new Mapper(programmTree, TYPE.FOR);
		mapper.process();

		this.mappedElements = mapper.getMappedElements();

		// ------------
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.refactor.IRefactor#process()
	 */
	public IProgrammTree process() {

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement actualElement = mappedElements.get(i);
			ForWhile loop = (ForWhile) actualElement.getElement().getType();
			IProgrammTree children = actualElement.getElement().getChildren().flatten();

			Variable var = loop.getHead();


			// only look in the children
			List<Element> elementsWithNewName = children.searchForName(var.getName());

			String newName = "forVar" + number++;

			for (int k = 0; k < elementsWithNewName.size(); k++) {

				Element type = elementsWithNewName.get(k);

				ValueExtractor.setName(type,
						ValueExtractor.getName(type).replaceAll(var.getName(), newName));
				ValueExtractor.setValue(type,
						ValueExtractor.getValue(type).replaceAll(var.getName(), newName));

			}

			loop.setHeadString(loop.getHeadString().replaceAll(var.getName(), newName));

		}

		return programmTree;
	}
}
