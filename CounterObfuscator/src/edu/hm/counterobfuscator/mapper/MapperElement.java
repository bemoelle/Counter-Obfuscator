package edu.hm.counterobfuscator.mapper;

import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.parser.tree.Element;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 13.01.2015
 * 
 * 
 */
public class MapperElement {

	private Position	scope;
	private Element	element;
	private int			positionInList;

	/**
	 * @param positionInList
	 * @param scope
	 * @param element
	 */
	public MapperElement(int positionInList, Position scope, Element element) {
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
	public Element getElement() {
		return element;
	}

	/**
	 * @param type
	 */
	public void setType(Element type) {
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
