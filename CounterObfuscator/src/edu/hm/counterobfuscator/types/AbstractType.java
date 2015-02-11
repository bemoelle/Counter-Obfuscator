package edu.hm.counterobfuscator.types;

import edu.hm.counterobfuscator.helper.Position;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 * 
 */
public abstract class AbstractType {

	protected String		name;
	protected Position	pos;
	protected TYPE			type;

	public AbstractType(TYPE type, Position pos, String name) {

		this.type = type;
		this.pos = pos;
		this.name = name;
	}

	/**
	 * @return
	 */
	public TYPE getType() {
		return type;
	}

	/**
	 * @return
	 */
	public Position getPos() {
		return pos;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 */
	public void print() {
		System.out.println("startPos: " + pos.getStartPos() + " -- " + "endPos: " + pos.getEndPos());

	}

	/**
	 * @param other
	 * @return
	 */
	public abstract boolean hasSameName(Object other);

	/**
	 * @param name
	 * @return
	 */
	public abstract boolean hasNameInIt(String name);
}
