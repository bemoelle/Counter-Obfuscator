/**
 * 
 */
package edu.hm.counterobfuscator.parser.token.trees;

import java.util.LinkedList;

import edu.hm.counterobfuscator.types.AbstractType;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 06.01.2015
 * 
 * 
 */
public class TypeTreeElement {

	//TODO Change children with ITypeTree
	private LinkedList<TypeTreeElement>	children;
	private AbstractType						type;

	public TypeTreeElement(AbstractType type) {
		this.type = type;
		this.children = new LinkedList<TypeTreeElement>();
	}

	public LinkedList<TypeTreeElement> getChildren() {
		return children;
	}

	public void addChild(TypeTreeElement child) {
		children.add(child);
	}
	
	public TypeTreeElement getLatestChild() {
		return children.getLast();
	}

	public boolean hasChildren() {
		return !children.isEmpty();
	}

	public AbstractType getType() {
		return type;
	}

	public void setType(AbstractType type) {
		this.type = type;
	}

	/**
	 * 
	 */
	public void removeAllChildren() {
		children.clear();
		
	}

}
