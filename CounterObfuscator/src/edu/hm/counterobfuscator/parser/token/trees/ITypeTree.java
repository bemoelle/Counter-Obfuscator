package edu.hm.counterobfuscator.parser.token.trees;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 09.01.2015
 *
 */
public interface ITypeTree extends Iterable<TypeTreeElement> {

	public boolean isEmpty();

	public void add(TypeTreeElement tte);

	public void print();

	public int size();

	public TypeTreeElement get(int index);

	public void clear();

	public TypeTreeElement getLast();
}
