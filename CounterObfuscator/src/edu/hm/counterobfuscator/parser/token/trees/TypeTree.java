/**
 * 
 */
package edu.hm.counterobfuscator.parser.token.trees;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 06.01.2015
 * 
 * 
 */
public class TypeTree implements ITypeTree {

	private List<TypeTreeElement> typeTree;

	public TypeTree() {
		typeTree = new LinkedList<>();
	}

	public boolean isEmpty() {
		return typeTree.isEmpty();
	}

	public void add(TypeTreeElement element) {

		typeTree.add(element);
	}
	
	public TypeTreeElement walk() {
		
		return null;
	}

	public void print() {

		printChildElement("", typeTree);

	}

	private void printChildElement(String tab, List<TypeTreeElement> tree) {

		for (TypeTreeElement element : tree) {
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
}
