/**
 * 
 */
package edu.hm.counterobfuscator.definitions;

import edu.hm.counterobfuscator.helper.Scope;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 15.01.2015
 * 
 *       represent JavaScript THIS statement assign to DEFINITION.THIS
 */
public class This extends AbstractType {

	private String value;
	private String notation;

	/**
	 * @param type
	 * @param pos
	 * @param name
	 */
	public This(Scope pos, String name, String notation, String value) {
		super(DEFINITION.THIS, pos, name);
		this.notation = notation;
		this.value = value;

	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.counterobfuscator.types.AbstractType#hasSameName(java.lang.Object)
	 */
	@Override
	public boolean hasSameName(Object other) {
		
		if (name.equals(((AbstractType) other).getName())) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.counterobfuscator.types.AbstractType#hasNameInIt(java.lang.String)
	 */
	@Override
	public boolean hasNameInIt(String nameToTest) {
		
		if (name.indexOf(nameToTest) != -1)
			return true;

		return false;
	}

	public String getNotation() {

		return notation;
	}

	public void setNotation(String notation) {

		this.notation = notation;
	}

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.definitions.AbstractType#replaceValueWith(java.lang.String, java.lang.String)
	 */
	@Override
	public void replaceValueWith(String nameToReplace, String valueToReplace) {
		
		value = value.replaceAll(nameToReplace, valueToReplace);
		
	}

}
