package edu.hm.counterobfuscator.refactor.modul;

import java.util.List;

import edu.hm.counterobfuscator.IClient;
import edu.hm.counterobfuscator.mapper.Mapper;
import edu.hm.counterobfuscator.mapper.MapperElement;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.types.ForWhile;
import edu.hm.counterobfuscator.types.TYPE;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.02.2015
 * 
 * 
 */
public class LoopChecker implements IModul {

	private IProgrammTree			programmTree;
	private List<MapperElement>	mappedElements;
	private IClient					client;

	public LoopChecker(IProgrammTree programmTree, IClient client) {

		this.programmTree = programmTree;
		this.client = client;

		this.mappedElements = Mapper.process(programmTree, TYPE.FOR);
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

			String head = loop.getHeadString();
			int indexFirstSemicolon = head.indexOf(";");
			
			String var = "var " + head.substring(1, indexFirstSemicolon) + ";";
			head = head.substring(indexFirstSemicolon + 1);
			head = head.substring(0, head.indexOf(";"));

			Object result = client.getJSResult(var + head);

			if ((Boolean) result == false) {
				remove(actualElement, children);
			}

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
