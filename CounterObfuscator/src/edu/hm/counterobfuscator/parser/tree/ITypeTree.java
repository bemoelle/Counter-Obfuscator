package edu.hm.counterobfuscator.parser.tree;

import java.util.List;

import edu.hm.counterobfuscator.helper.Position;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 09.01.2015
 *
 */
public interface ITypeTree extends Iterable<TypeTreeElement> {

	/**
	 * @return
	 */
	public boolean isEmpty();

	/**
	 * @param typeTreeElement
	 */
	public void add(TypeTreeElement typeTreeElement);

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
	public TypeTreeElement get(int index);

	/**
	 * 
	 */
	public void clear();

	/**
	 * @return
	 */
	public TypeTreeElement getLast();

	/**
	 * @param index
	 * @return 
	 */
	public TypeTreeElement remove(int index);
	
	/**
	 * @param type
	 * @return
	 */
	public boolean removeElement(TypeTreeElement type);
	
	/**
	 * @return
	 */
	public Position findGlobalScope();

	/**
	 * @return
	 */
	public ITypeTree flatten();

	/**
	 * @param reverse
	 */
	public void addAll(List<TypeTreeElement> treeList);

	/**
	 * @return
	 */
	public ITypeTree reverseOrder();
}
