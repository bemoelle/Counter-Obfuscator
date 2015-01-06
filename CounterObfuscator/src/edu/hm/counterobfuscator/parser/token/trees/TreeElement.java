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
public class TreeElement {

	private int								number;
	private LinkedList<TreeElement>	children;
	private AbstractType					type;

	public TreeElement(int number, AbstractType type) {
		this.number = number;
		this.type = type;
		this.children = new LinkedList<TreeElement>();
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public LinkedList<TreeElement> getChildren() {
		return children;
	}

	public void setChildren(LinkedList<TreeElement> children) {
		this.children = children;
	}

	public void addChild(TreeElement child) {
		children.add(child);
	}

	public AbstractType getType() {
		return type;
	}

	public void setType(AbstractType type) {
		this.type = type;
	}

}
