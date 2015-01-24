/**
 * 
 */
package edu.hm.counterobfuscator.types;

import edu.hm.counterobfuscator.helper.Position;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 06.01.2015
 * 
 * 
 */
public class ForWhile extends AbstractType {

	private Variable head;
	private String headString;
	private String bodyAsString;

	public ForWhile(Position pos, String name, String headString,
			String bodyAsString) {

		super(name.equals("for") ? TYPE.FOR : TYPE.WHILE, pos, name);
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

}
