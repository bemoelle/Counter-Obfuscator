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

	public TYPE getType() {
		return type;
	}

	public Position getPos() {
		return pos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void print() {
		System.out.println("startPos: " + pos.getStartPos() + " -- " + "endPos: " + pos.getEndPos());

	}

	public abstract boolean hasSameName(Object other);

	// public abstract boolean isSame(Object other);
}
