package edu.hm.counterobfuscator.refactor.modul;

import java.util.Iterator;
import java.util.List;

import edu.hm.counterobfuscator.mapper.Mapper;
import edu.hm.counterobfuscator.mapper.MapperElement;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.ValueExtractor;
import edu.hm.counterobfuscator.types.TYPE;
import edu.hm.counterobfuscator.types.Variable;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 22.01.2015
 * 
 * 
 */
public class VariableRemover implements IModul {

	private IProgrammTree			programmTree;
	private List<MapperElement>	mappedElements;

	public VariableRemover(IProgrammTree programmTree) {

		this.programmTree = programmTree;

		this.mappedElements = Mapper.process(programmTree, TYPE.VARIABLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.refactor.IRefactor#process()
	 */
	public IProgrammTree process() {

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement me = mappedElements.get(i);
			String nameLookingFor = me.getElement().getType().getName();

			int refCounter = 0;

			Iterator<Element> it = programmTree.reverseIterator();

			while (it.hasNext()) {

				Element actualElement = it.next();

				if (me.getElement() == actualElement) {
					if (refCounter == 0) {
						it.remove();
					}
					continue;
				}

				String value = ValueExtractor.getValue(actualElement);
				if (value.indexOf(nameLookingFor) > -1) {
					refCounter++;
				}

				String name = ValueExtractor.getName(actualElement);
				if (name.indexOf(nameLookingFor) > -1) {

					if (actualElement.getType().getType() == TYPE.VARIABLE) {

						Variable var = (Variable) actualElement.getType();

						if (!var.isExecutable()) {
							refCounter++;
						}
					} else {
						refCounter++;
					}
				}
			}
		}

		return programmTree;

	}

}
