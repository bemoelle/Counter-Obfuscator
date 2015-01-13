/**
 * 
 */
package edu.hm.counterobfuscator.parser.tree;

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

	/**
	 * @param parent
	 * @param type
	 */
	public TypeTreeElement(TypeTreeElement parent, AbstractType type) {
		this.parent = parent;
		this.type = type;
		this.children = new TypeTree();
	}

	/**
	 * @return
	 */
	public TypeTreeElement getParent() {
		return parent;
	}

	/**
	 * @param parent
	 */
	public void setParent(TypeTreeElement parent) {
		this.parent = parent;
	}

	/**
	 * @return
	 */
	public ITypeTree getChildren() {
		return children;
	}

	/**
	 * @param child
	 */
	public void addChild(TypeTreeElement child) {
		children.add(child);
	}

	/**
	 * @return
	 */
	public TypeTreeElement getLatestChild() {
		return children.getLast();
	}

	/**
	 * @return
	 */
	public boolean hasChildren() {
		return !children.isEmpty();
	}

	/**
	 * @return
	 */
	public AbstractType getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(AbstractType type) {
		this.type = type;
	}

	/**
	 * 
	 */
	public void removeAllChildren() {
		children.clear();

	}

	/**
	 * @param index
	 * @return
	 */
	public TypeTreeElement getChild(int index) {
		return children.get(index);
	}
	
	@Override
	public boolean equals(Object other){
		
		if (!(other instanceof TypeTreeElement)) {
			return false;
		} else  {
			TypeTreeElement otherTypeTreeElement = (TypeTreeElement)other;
			
			if(this.getType() != otherTypeTreeElement.getType()) return false;
			if(!this.getType().getPos().equals(otherTypeTreeElement.getType().getPos())) return false;
			if(!this.getType().getName().equals(otherTypeTreeElement.getType().getName())) return false;
			
			return true;
		}
	}

}
