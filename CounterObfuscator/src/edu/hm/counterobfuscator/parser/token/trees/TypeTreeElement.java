/**
 * 
 */
package edu.hm.counterobfuscator.parser.token.trees;

import edu.hm.counterobfuscator.types.AbstractType;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 06.01.2015
 * 
 * 
 */
public class TypeTreeElement {

	private ITypeTree children;
	private AbstractType type;
	private TypeTreeElement parent;

	public TypeTreeElement(TypeTreeElement parent, AbstractType type) {
		this.parent = parent;
		this.type = type;
		this.children = new TypeTree();
	}

	public TypeTreeElement getParent() {
		return parent;
	}

	public void setParent(TypeTreeElement parent) {
		this.parent = parent;
	}

	public ITypeTree getChildren() {
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

	public TypeTreeElement getChild(int index) {
		return children.get(index);
	}

}
