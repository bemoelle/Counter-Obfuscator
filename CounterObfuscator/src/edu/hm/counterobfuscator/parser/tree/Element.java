/**
 * 
 */
package edu.hm.counterobfuscator.parser.tree;

import org.apache.commons.lang3.Validate;

import edu.hm.counterobfuscator.definitions.AbstractType;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 06.01.2015
 * 
 *       element represent an node in a programmtree a element can also be a
 *       parent of programmtree of childrens
 */
public class Element {

	private IProgrammTree children;
	private AbstractType type;
	private Element parent;
	private Element next;
	private Element before;
	private int depth;

	/**
	 * @param parent
	 * @param type
	 * @param depth
	 */
	public Element(Element parent, AbstractType type, int depth) {

		this.parent = parent;
		this.type = type;
		this.children = new ProgrammTree();
		this.depth = depth;
	}

	/**
	 * @return parent of actual element
	 */
	public Element getParent() {

		return parent;
	}

	/**
	 * @param parent set parent of actual element 
	 */
	public void setParent(Element parent) {

		this.parent = parent;
	}

	/**
	 * @return all childrens of element
	 */
	public IProgrammTree getChildren() {

		return children;
	}

	/**
	 * @param child set child of actual element
	 */
	public void addChild(Element child) {

		children.add(child);
	}

	/**
	 * @return last child of children
	 */
	public Element getLatestChild() {

		return children.getLast();
	}

	/**
	 * @return true if element has children
	 */
	public boolean hasChildren() {

		return !children.isEmpty();
	}

	/**
	 * @return return type of actual element
	 */
	public AbstractType getDefinition() {

		return type;
	}

	/**
	 * @param type set type of actual element
	 */
	public void setType(AbstractType type) {

		this.type = type;
	}

	/**
	 * removed all children of actual element
	 */
	public void removeAllChildren() {

		children.clear();

	}

	/**
	 * @param index
	 * @return element
	 * 
	 * return 
	 */
	public Element getChild(int index) {

		Validate.isTrue(index > -1);
		Validate.isTrue(index < children.size());
		return children.get(index);
	}

	@Override
	public boolean equals(Object other) {

		// if (!(other instanceof TypeTreeElement)) {
		// return false;
		// } else {
		Element otherTypeTreeElement = (Element) other;

		if (this.getDefinition() != otherTypeTreeElement.getDefinition())
			return false;
		if (!this.getDefinition().getPos()
				.equals(otherTypeTreeElement.getDefinition().getPos()))
			return false;
		if (!this.getDefinition().getName()
				.equals(otherTypeTreeElement.getDefinition().getName()))
			return false;

		return true;
		// }
	}

	/**
	 * @return depth in whole programmtree
	 */
	public int getDepth() {

		return depth;
	}

	/**
	 * @param depth set depth
	 */
	public void setDepth(int depth) {

		this.depth = depth;
	}

	/**
	 * @return get element after actual element
	 */
	public Element getNext() {

		return next;
	}

	/**
	 * @param next set next element
	 */
	public void setNext(Element next) {

		this.next = next;
	}

	/**
	 * @return get element before actual element
	 */
	public Element getBefore() {

		return before;
	}

	/**
	 * @param before set before element
	 */
	public void setBefore(Element before) {

		this.before = before;
	}
}
