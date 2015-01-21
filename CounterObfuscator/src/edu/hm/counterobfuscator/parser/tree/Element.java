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
public class Element {

	private IProgrammTree children;
	private AbstractType type;
	private Element parent;

	/**
	 * @param parent
	 * @param type
	 */
	public Element(Element parent, AbstractType type) {
		this.parent = parent;
		this.type = type;
		this.children = new ProgrammTree();
	}

	/**
	 * @return
	 */
	public Element getParent() {
		return parent;
	}

	/**
	 * @param parent
	 */
	public void setParent(Element parent) {
		this.parent = parent;
	}

	/**
	 * @return
	 */
	public IProgrammTree getChildren() {
		return children;
	}

	/**
	 * @param child
	 */
	public void addChild(Element child) {
		children.add(child);
	}

	/**
	 * @return
	 */
	public Element getLatestChild() {
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
	public Element getChild(int index) {
		return children.get(index);
	}
	
	@Override
	public boolean equals(Object other){
		
//		if (!(other instanceof TypeTreeElement)) {
//			return false;
//		} else  {
			Element otherTypeTreeElement = (Element)other;
			
			//if(this.getType() != otherTypeTreeElement.getType()) return false;
			if(!this.getType().getPos().equals(otherTypeTreeElement.getType().getPos())) return false;
			//if(!this.getType().getName().equals(otherTypeTreeElement.getType().getName())) return false;
			
			return true;
//		}
	}

}
