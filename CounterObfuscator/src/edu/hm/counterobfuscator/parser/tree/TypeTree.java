/**
 * 
 */
package edu.hm.counterobfuscator.parser.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.types.AbstractType;
import edu.hm.counterobfuscator.types.Variable;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 06.01.2015
 * 
 * 
 */
public class TypeTree implements ITypeTree {

	private List<TypeTreeElement>	typeTree;

	/**
	 * 
	 */
	public TypeTree() {
		typeTree = new ArrayList<TypeTreeElement>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#isEmpty()
	 */
	public boolean isEmpty() {
		return typeTree.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#add(edu.hm.
	 * counterobfuscator.parser.token.trees.TypeTreeElement)
	 */
	public void add(TypeTreeElement element) {

		typeTree.add(element);
	}
	
	public void addAll(List<TypeTreeElement> treeList) {

		typeTree.addAll(treeList);
	}

	/*
	 * (non-Javadoc)
	 * 
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

		for (int i = 0; i < tree.size(); i++) {

			TypeTreeElement element = tree.get(i);

			String test = "";
			if (element.getType() instanceof Variable) {
				test += ((Variable) element.getType()).getName() + " = ";
				test += ((Variable) element.getType()).getValue();
			}

			System.out.println("|" + tab + element.getType().getType().toString() + " -- " + test);

			if (element.hasChildren()) {

				printChildElement(tab + "__", element.getChildren());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#size()
	 */
	public int size() {

		return typeTree.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#get(int)
	 */
	public TypeTreeElement get(int index) {

		return typeTree.get(index);
	}

	public TypeTreeElement remove(int index) {
		return typeTree.remove(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#clear()
	 */
	public void clear() {
		typeTree.clear();

	}

	// -----------------------------------------------------
	// Iterator
	public TypeTreeElement getLast() {
		return typeTree.get(typeTree.size() - 1);
	}

	@Override
	public Iterator<TypeTreeElement> iterator() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.counterobfuscator.parser.tree.ITypeTree#removeElementOfType(edu
	 * .hm.counterobfuscator.types.AbstractType)
	 */
	@Override
	public boolean removeElement(TypeTreeElement type) {

		return typeTree.remove(type);
	}

	public Position findGlobalScope() {

		Position globalScope = new Position(0, 0);

		for (int i = 0; i < typeTree.size(); i++) {
			AbstractType element = typeTree.get(i).getType();

			if (element.getPos().getEndPos() > globalScope.getEndPos()) {
				globalScope.setEndPos(element.getPos().getEndPos());
			}
		}

		return globalScope;
	}

	public ITypeTree flatten() {

		ITypeTree list = new TypeTree();

		return walkThroughElement(this, list);
	}

	/**
	 * @return
	 */
	private ITypeTree walkThroughElement(ITypeTree tree, ITypeTree flatList) {

		for (int i = 0; i < tree.size(); i++) {

			flatList.add(tree.get(i));

			if (tree.get(i).hasChildren()) {

				return walkThroughElement(tree.get(i).getChildren(), flatList);
			}
		}
		return flatList;

	}
	
	public ITypeTree reverseOrder() {
		
		List<TypeTreeElement> reverse = typeTree;
		
		Collections.reverse(reverse);
		
		ITypeTree reverseTree = new TypeTree();
		
		reverseTree.addAll(reverse);
		
		return reverseTree;
	}

}
