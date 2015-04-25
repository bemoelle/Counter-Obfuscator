package edu.hm.counterobfuscator.definitions;

import edu.hm.counterobfuscator.helper.Scope;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 *       abstract class for all javascript definitions
 */
public abstract class AbstractType {

	protected String name;
	protected Scope pos;
	protected DEFINITION type;

	public AbstractType(DEFINITION type, Scope pos, String name) {

		this.type = type;
		this.pos = pos;
		this.name = name.replaceAll(" ", "");
		if(type == DEFINITION.DEFAULT)
			this.name = name.replaceAll(";", "");
	}

	/**
	 * @return type
	 */
	public DEFINITION getDefinition() {
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
	 * @param name
	 *            set name
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
	 *         abstract methode to be define in subclass
	 */
	public abstract boolean hasSameName(Object other);

	/**
	 * @param name
	 * @return true if type has name in it
	 * 
	 *         abstract type to be define in subclass
	 */
	public abstract boolean hasNameInIt(String name);

	/**
	 * @param name
	 * @param value
	 */
	public void replaceNameWith(String nameToReplace, String value) {
		
		name = name.replaceAll("^" + nameToReplace + "$", value);

	}

	/**
	 * @param name
	 * @param value
	 */
	public abstract void replaceValueWith(String name, String value);

	/**
	 * @param name
	 */
	public void replaceName(String name) {
		
		
		
	}

	/**
	 * @return
	 */
	public abstract String getValue();

}
