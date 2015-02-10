package edu.hm.counterobfuscator.parser.tree.mapper;

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

	/**
	 * @param scope
	 * @param element
	 */
	public MapperElement(Position scope, Element element) {
		this.scope = scope;
		this.element = element;
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
