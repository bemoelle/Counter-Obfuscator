/**
 * 
 */
package edu.hm.counterobfuscator.parser.token.trees;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 06.01.2015
 * 
 * 
 */
public class TypeTree implements ITypeTree {

	private LinkedList<TypeTreeElement> typeTree;

	/**
	 * 
	 */
	public TypeTree() {
		typeTree = new LinkedList<TypeTreeElement>();
	}

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#isEmpty()
	 */
	public boolean isEmpty() {
		return typeTree.isEmpty();
	}

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#add(edu.hm.counterobfuscator.parser.token.trees.TypeTreeElement)
	 */
	public void add(TypeTreeElement element) {

		typeTree.add(element);
	}
	
	//TODO
	/**
	 * @return
	 */
	public TypeTreeElement walk() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#print()
	 */
	public void print() {

		printChildElement("", this);

	}

	/**
	 * @param tab
	 * @param tree
	 */
	private void printChildElement(String tab, ITypeTree tree) {

		for(int i=0; i<tree.size(); i++) {
		
			TypeTreeElement element = tree.get(i);
			System.out.println("|" + tab
					+ element.getType().getType().toString());

			if (element.hasChildren()) {
				printChildElement(tab + "__", element.getChildren());
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#size()
	 */
	public int size() {

		return typeTree.size();
	}

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#get(int)
	 */
	public TypeTreeElement get(int index) {

		return typeTree.get(index);
	}

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#clear()
	 */
	public void clear() {
		typeTree.clear();
		
	}
	
	//-----------------------------------------------------
	// Iterator
	public TypeTreeElement getLast() {
		return typeTree.getLast();
	}

	@Override
	public Iterator<TypeTreeElement> iterator() {
		// TODO Auto-generated method stub
		return null;
	}


}
