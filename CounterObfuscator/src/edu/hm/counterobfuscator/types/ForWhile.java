/**
 * 
 */
package edu.hm.counterobfuscator.types;

import edu.hm.counterobfuscator.helper.Scope;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 06.01.2015
 * 
 *       represent JavaScript LOOP assign to DEFINITION.WHILE or DEFINITION.FOR
 */
public class ForWhile extends AbstractType {

	private Variable head;
	private String headString;
	private String bodyAsString;

	public ForWhile(Scope pos, String name, String headString,
			String bodyAsString) {

		super(name.equals("for") ? DEFINITION.FOR : DEFINITION.WHILE, pos, name);
		this.headString = headString;
		this.bodyAsString = bodyAsString;

		boolean isGlobal = false;

		String varName = headString.substring(1, headString.indexOf("="));
		if (varName.matches("var.*")) {
			varName = varName.replace("var", "");
			isGlobal = true;
		}

		this.head = new Variable(pos, varName, "", "", isGlobal);
	}

	public Variable getHead() {

		return head;
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

		// TODO Auto-generated method stub
		return false;
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

		headString = headString.replace(nameToReplace, valueToReplace);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.counterobfuscator.types.AbstractType#replaceName(java.lang.String)
	 */
	@Override
	public void replaceName(String name) {

		// do nothing

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
