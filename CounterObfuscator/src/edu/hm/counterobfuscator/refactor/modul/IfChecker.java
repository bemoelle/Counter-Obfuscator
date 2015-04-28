package edu.hm.counterobfuscator.refactor.modul;

import java.util.List;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.client.IClient;
import edu.hm.counterobfuscator.definitions.DEFINITION;
import edu.hm.counterobfuscator.definitions.If;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.mapper.MapperElement;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.02.2015
 * 
 *       check if the if is necessary
 * 
 *       if(false) {...}; not necessary if and body can be removed
 * 
 */
public class IfChecker implements IModul {

	private IProgrammTree			programmTree;
	private List<MapperElement>	mappedElements;
	private IClient					client;
	private Mapper						mapper;

	public IfChecker(IProgrammTree programmTree, IClient client) {

		this.programmTree = programmTree;
		this.client = client;

		mapper = new Mapper(programmTree);
		this.mappedElements = mapper.process(DEFINITION.IF);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.refactor.IRefactor#process()
	 */
	public IProgrammTree process() throws ScriptException {

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement actualElement = mappedElements.get(i);
			If ifStatement = (If) actualElement.getElement().getDefinition();
			IProgrammTree children = actualElement.getElement().getChildren();

			String head = ifStatement.getHeadString();

			Object result = client.getJSResult(head);

			if ((Boolean) result == false) {

				if (actualElement.getElement().getParent() == null) {
					remove(actualElement, children);
				} else {
					actualElement.getElement().getParent().getChildren()
							.removeElementAndAllChildren(actualElement.getElement());
				}
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
			programmTree.removeElementAndAllChildren(children.get(j));
		}

		programmTree.removeElementAndAllChildren(actualElement.getElement());
	}
}
