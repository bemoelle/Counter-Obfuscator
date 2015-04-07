package edu.hm.counterobfuscator.parser.tree.mapper;

import edu.hm.counterobfuscator.helper.Scope;
import edu.hm.counterobfuscator.parser.tree.Element;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 13.01.2015
 * 
 *       this class represent an mapped elmement, which is found by the mapper
 */
public class MapperElement {

	private Scope scope;
	private Element element;

	/**
	 * @param scope
	 * @param element
	 */
	public MapperElement(Scope scope, Element element) {
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
	public Scope getScope() {
		return scope;
	}

	/**
	 * @param scope
	 */
	public void setScope(Scope scope) {
		this.scope = scope;
	}

}
