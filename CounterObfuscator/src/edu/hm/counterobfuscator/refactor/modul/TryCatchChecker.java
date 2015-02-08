package edu.hm.counterobfuscator.refactor.modul;

import java.util.Iterator;
import java.util.List;

import edu.hm.counterobfuscator.IClient;
import edu.hm.counterobfuscator.helper.Validate;
import edu.hm.counterobfuscator.mapper.Mapper;
import edu.hm.counterobfuscator.mapper.MapperElement;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.ProgrammTree;
import edu.hm.counterobfuscator.types.AbstractType;
import edu.hm.counterobfuscator.types.Default;
import edu.hm.counterobfuscator.types.TYPE;
import edu.hm.counterobfuscator.types.TryCatch;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.02.2015
 * 
 * 
 */
public class TryCatchChecker implements IModul {

	private IProgrammTree			programmTree;
	private List<MapperElement>	mappedElements;
	private InterpreterModul		interpreter;

	public TryCatchChecker(IProgrammTree programmTree, IClient client) {

		this.programmTree = programmTree;

		interpreter = new InterpreterModul(client, false);

		this.mappedElements = Mapper.process(programmTree, TYPE.TRYCATCH);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.refactor.IRefactor#process()
	 */
	public IProgrammTree process() {

		Validate.notNull(programmTree);

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement actualElement = mappedElements.get(i);

			TryCatch tryCatch = (TryCatch) actualElement.getElement().getType();

			// nothing to do with catch statement
			if ("catch".equals(tryCatch.getName()))
				continue;

			IProgrammTree children = actualElement.getElement().getChildren();

			for (int j = 0; j < children.size(); j++) {

				Element element = children.get(j);

				try {
					interpreter.process(element);
				} catch (Exception e) {

					// for(int k=j; k<children.size(); k++) {
					Element next = element;
					while (next != null) {

						children.removeElementAndAllChildren(next);
						next = next.getNext();
					}
					
				//	programmTree.add(new Element(null, new Default(null, "dfdfdfd"),2 ));
										
					programmTree.remove(actualElement.getElement());
					programmTree.remove(mappedElements.get(i+1).getElement());

				}

			}

		}

		return programmTree;
	}
}
