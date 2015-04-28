/**
 * 
 */
package edu.hm.counterobfuscator.definitions;

import edu.hm.counterobfuscator.helper.Scope;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 06.01.2015
 * 
 *       represent JavaScript LOOP assign to DEFINITION.WHILE or DEFINITION.FOR
 */
public class If extends AbstractType {

	private String headString;
	private String bodyAsString;

	public If(Scope pos, String name, String headString,
			String bodyAsString) {

		super(name.equals("if") ? DEFINITION.IF : DEFINITION.ELSE, pos, name);
		this.headString = headString;
		this.bodyAsString = bodyAsString;

	}

	public String getHeadString() {

		return headString;
	}

	public void setHeadString(String headString) {

		this.headString = headString;
	}

	public String getBodyAsString() {

		return bodyAsString;
	}

	public void setBodyAsString(String body) {

		this.bodyAsString = body;
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
	public boolean hasNameInIt(String name) {

		return headString.contains(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.counterobfuscator.types.AbstractType#replaceName(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void replaceNameWith(String nameToReplace, String valueToReplace) {

		headString = headString.replaceAll(nameToReplace, valueToReplace);

	}
	
	@Override
	public void replaceValueWith(String nameToReplace, String valueToReplace) {

		headString = headString.replaceAll(nameToReplace, valueToReplace);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.counterobfuscator.types.AbstractType#replaceName(java.lang.String)
	 */
	@Override
	public void replaceName(String name) {

		this.name = name;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.types.AbstractType#getValue()
	 */
	@Override
	public String getValue() {

		return "";
	}

}
