package edu.hm.counterobfuscator.mapper;

import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.parser.tree.TypeTreeElement;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 13.01.2015
 * 
 * 
 */
public class MapperElement {

	private Position			scope;
	private TypeTreeElement	element;
	private int					positionInList;

	/**
	 * @param positionInList
	 * @param scope
	 * @param element
	 */
	public MapperElement(int positionInList, Position scope, TypeTreeElement element) {
		this.positionInList = positionInList;
		this.scope = scope;
		this.element = element;
	}

	/**
	 * @return
	 */
	public int getPositionInList() {
		return positionInList;
	}

	/**
	 * @return
	 */
	public TypeTreeElement getElement() {
		return element;
	}

	/**
	 * @param type
	 */
	public void setType(TypeTreeElement type) {
		this.element = type;
	}

	/**
	 * @return
	 */
	public Position getScope() {
		return scope;
	}

	/**
	 * @param scope
	 */
	public void setScope(Position scope) {
		this.scope = scope;
	}

}
