package edu.hm.counterobfuscator.parser.tree;

import java.util.List;

import edu.hm.counterobfuscator.helper.Position;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 09.01.2015
 *
 */
public interface IProgrammTree extends Iterable<Element> {

	/**
	 * @return
	 */
	public boolean isEmpty();

	/**
	 * @param typeTreeElement
	 */
	public void add(Element typeTreeElement);

	/**
	 * 
	 */
	public void print(boolean flat);

	/**
	 * @return
	 */
	public int size();

	/**
	 * @param index
	 * @return
	 */
	public Element get(int index);

	/**
	 * 
	 */
	public void clear();

	/**
	 * @return
	 */
	public Element getLast();

	/**
	 * @param index
	 * @return 
	 */
	public Element remove(int index);
	
	/**
	 * @param type
	 * @return
	 */
	public boolean removeElement(Element type);
	
	/**
	 * @return
	 */
	public Position findGlobalScope();

	/**
	 * @return
	 */
	public IProgrammTree flatten();

	/**
	 * @param reverse
	 */
	public void addAll(ProgrammTree ProgrammTree);

	/**
	 * @return
	 */
	public IProgrammTree reverseOrder();

	public void prettyPrint();
}
