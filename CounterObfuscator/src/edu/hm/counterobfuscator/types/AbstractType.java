package edu.hm.counterobfuscator.types;

import edu.hm.counterobfuscator.helper.Scope;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 * 
 */
public abstract class AbstractType {

	protected String name;
	protected Scope pos;
	protected TYPE type;

	public AbstractType(TYPE type, Scope pos, String name) {

		this.type = type;
		this.pos = pos;
		this.name = name;
	}

	/**
	 * @return type
	 */
	public TYPE getType() {
		return type;
	}

	/**
	 * @return position
	 */
	public Scope getPos() {
		return pos;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name set name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * print type to System.out.
	 */
	public void print() {
		System.out.println("startPos: " + pos.getStartPos() + " -- "
				+ "endPos: " + pos.getEndPos());

	}

	/**
	 * @param other
	 * @return true if other has same name as this
	 * 
	 * abstract methode to be define in subclass
	 */
	public abstract boolean hasSameName(Object other);

	/**
	 * @param name
	 * @return true if type has name in it
	 * 
	 * abstract type to be define in subclass
	 */
	public abstract boolean hasNameInIt(String name);
}
