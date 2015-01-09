/**
 * 
 */
package edu.hm.counterobfuscator.parser.token.trees;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 06.01.2015
 * 
 * 
 */
public class TypeTree implements ITypeTree {

	private LinkedList<TypeTreeElement> typeTree;

	public TypeTree() {
		typeTree = new LinkedList<TypeTreeElement>();
	}

	public boolean isEmpty() {
		return typeTree.isEmpty();
	}

	public void add(TypeTreeElement element) {

		typeTree.add(element);
	}
	
	//TODO
	public TypeTreeElement walk() {
		
		return null;
	}

	public void print() {

		printChildElement("", this);

	}

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

	public int size() {

		return typeTree.size();
	}

	public TypeTreeElement get(int index) {

		return typeTree.get(index);
	}

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
