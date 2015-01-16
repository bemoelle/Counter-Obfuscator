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

	private Variable	head;
	private String		body;
	private String		headString;

	public ForWhile(Position pos, String name, String headString) {

		super(name.equals("for") ? TYPE.FOR : TYPE.WHILE, pos, name);
		this.headString = headString;

		boolean isGlobal = false;

		String varName = headString.substring(1, headString.indexOf("="));
		if (varName.matches("var.*")) {
			varName = varName.replace("var", "");
			isGlobal = true;
		}

		this.head = new Variable(pos, varName, "");
		this.body = "";
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

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
