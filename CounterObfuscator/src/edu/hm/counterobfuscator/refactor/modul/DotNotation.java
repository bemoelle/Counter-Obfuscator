package edu.hm.counterobfuscator.refactor.modul;

import java.util.Iterator;
import java.util.List;

import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.mapper.MapperElement;
import edu.hm.counterobfuscator.types.DEFINITION;
import edu.hm.counterobfuscator.types.This;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 22.01.2015
 * 
 *       tranform array notation to dot notation
 * 
 *       from: this['add'] = function() {..}; to: this.add = function() {...};
 */
public class DotNotation implements IModul {

	private IProgrammTree programmTree;
	private List<MapperElement> mappedElements;

	public DotNotation(IProgrammTree programmTree) {

		this.programmTree = programmTree;

		Mapper mapper = new Mapper(programmTree);
		this.mappedElements = mapper.process(DEFINITION.THIS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.refactor.IRefactor#process()
	 */
	public IProgrammTree process() {

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement me = mappedElements.get(i);

			Iterator<Element> it = programmTree.reverseIterator();

			while (it.hasNext()) {

				Element actualElement = it.next();

				if (me.getElement() == actualElement) {

					String name = actualElement.getType().getName();
					if (!name.contains(".")) {
						name = name.replaceAll("[\\[\\]\\']", "");
						This thisStatement = ((This) actualElement.getType());
						thisStatement.setName(name);
						thisStatement.setNotation(".");
					}
				}

			}
		}
		return programmTree;
	}
}
