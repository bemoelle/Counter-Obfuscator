package edu.hm.counterobfuscator.refactor.modul;

import java.util.Iterator;
import java.util.List;

import edu.hm.counterobfuscator.definitions.DEFINITION;
import edu.hm.counterobfuscator.definitions.Variable;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.mapper.MapperElement;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 22.01.2015
 * 
 *       removes variable definition, which are no longer neeeded in the
 *       programm
 */
public class VariableRemover implements IModul {

	private IProgrammTree			programmTree;
	private List<MapperElement>	mappedElements;

	public VariableRemover(IProgrammTree programmTree) {

		this.programmTree = programmTree;

		Mapper mapper = new Mapper(programmTree);
		this.mappedElements = mapper.process(DEFINITION.VARIABLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.refactor.IRefactor#process()
	 */
	public IProgrammTree process() {
		
		//nothing do when programmtree has only one element
		if(programmTree.size() == 1 && !programmTree.get(0).hasChildren()) {
			return programmTree;
		}

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement me = mappedElements.get(i);
			String nameLookingFor = me.getElement().getDefinition().getName();

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

//				if (actualElement.getDefinition().hasNameInIt(nameLookingFor)) {
//					refCounter++;
//				}

				//String name = actualElement.getDefinition().getName();
				
				if (actualElement.getDefinition().hasNameInIt(nameLookingFor)) {

					if (actualElement.getDefinition().getDefinition() == DEFINITION.VARIABLE) {

						Variable var = (Variable) actualElement.getDefinition();

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
