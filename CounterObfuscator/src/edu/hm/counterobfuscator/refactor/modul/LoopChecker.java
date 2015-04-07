package edu.hm.counterobfuscator.refactor.modul;

import java.util.List;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.client.IClient;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.mapper.MapperElement;
import edu.hm.counterobfuscator.types.ForWhile;
import edu.hm.counterobfuscator.types.DEFINITION;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.02.2015
 * 
 *       check if the loop is necessary
 * 
 *       for(i=0; i<0; i++) {...}; not necessary loop and body can be removed
 *       for(i=0; i<1; i++) {...}; loop head is not necessary code can replaced
 *       with code in body 
 *       while(false) {...}; not necessary loop and body can
 *       be removed
 * 
 */
public class LoopChecker implements IModul {

	private IProgrammTree programmTree;
	private List<MapperElement> mappedElements;
	private IClient client;

	public LoopChecker(IProgrammTree programmTree, IClient client) {

		this.programmTree = programmTree;
		this.client = client;

		Mapper mapper = new Mapper(programmTree);
		this.mappedElements = mapper.process(DEFINITION.FOR);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.refactor.IRefactor#process()
	 */
	public IProgrammTree process() throws ScriptException {

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement actualElement = mappedElements.get(i);
			ForWhile loop = (ForWhile) actualElement.getElement().getType();
			IProgrammTree children = actualElement.getElement().getChildren();

			String head = loop.getHeadString();
			int indexFirstSemicolon = head.indexOf(";");

			String var = "var " + head.substring(1, indexFirstSemicolon) + ";";
			head = head.substring(indexFirstSemicolon + 1);
			head = head.substring(0, head.indexOf(";"));

			Object result = client.getJSResult(var + head);

			if ((Boolean) result == false) {
				remove(actualElement, children);
			} else {

				int index = head.indexOf("<");
				int index2 = head.indexOf("=");
				String headStatement = "";

				if (index < 0) {
					index = head.indexOf(">");
				}

				if (index2 > 0) {
					headStatement = head.substring(index2 + 1);
				} else {
					headStatement = head.substring(index + 1);
				}

				// remove whitespaces in statement
				headStatement = headStatement.replaceAll(" ", "");

				result = client.getJSResult(headStatement);

				loop.replaceNameWith(headStatement, result.toString());
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
