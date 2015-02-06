package edu.hm.counterobfuscator.refactor.modul;

import java.util.List;

import edu.hm.counterobfuscator.IClient;
import edu.hm.counterobfuscator.helper.Validate;
import edu.hm.counterobfuscator.mapper.Mapper;
import edu.hm.counterobfuscator.mapper.MapperElement;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
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
		
		Validate.isTrue(programmTree.isFlat());

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement actualElement = mappedElements.get(i);
						
			TryCatch tryCatch = (TryCatch) actualElement.getElement().getType();
			
			//nothing to do with catch statement 
			if("catch".equals(tryCatch.getName()))
				continue;
			
			IProgrammTree children = actualElement.getElement().getChildren().flatten();

			for (int j = 0; j < children.size(); j++) {
				
				Element element = children.get(j);
				
				try {
					interpreter.process(element);
				} catch(Exception e) {
					for(int k=j; k<children.size(); k++) {
						Element child = children.get(k);
						programmTree.removeElement(child);
						children.remove(k);
						programmTree.removeElement(actualElement.getElement());
						programmTree.removeElement(mappedElements.get(i+1).getElement());
					}
				}

			}

			// String head = loop.getHeadString();
			// int indexFirstSemicolon = head.indexOf(";");
			//
			// String var = "var " + head.substring(1, indexFirstSemicolon) + ";";
			// head = head.substring(indexFirstSemicolon + 1);
			// head = head.substring(0, head.indexOf(";"));
			//
			// Object result = client.getJSResult(var + head);
			//
			// if ((Boolean) result == false) {
			// remove(actualElement, children);
			// }

		}

		return programmTree;
	}

	/**
	 * @param actualElement
	 * @param children
	 */
	private void remove(MapperElement actualElement, IProgrammTree children) {
		for (int j = 0; j < children.size(); j++) {
			programmTree.removeElement(children.get(j));
		}

		programmTree.removeElement(actualElement.getElement());
	}
}
