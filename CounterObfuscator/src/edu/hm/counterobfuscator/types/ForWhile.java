/**
 * 
 */
package edu.hm.counterobfuscator.types;

import java.util.List;

import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.parser.token.Token;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 06.01.2015
 * 
 * 
 */
public class ForWhile extends AbstractType {

	private Variable			head;
	private List<Token>	boby;
	private String	headString;

	public ForWhile(Position pos, String name, String headString, List<Token> boby) {

		super(name.equals("for") ? TYPE.FOR : TYPE.WHILE, pos, name);
		this.headString = headString;

		boolean isGlobal = false;
		
		String varName = headString.substring(1, headString.indexOf("="));
		if(varName.matches("var.*")) {
			varName = varName.replace("var", "");
			isGlobal = true;
		}
		
		this.head = new Variable(pos, varName, "", isGlobal);
		this.boby = boby;
	}

	public Variable getHead() {
		return head;
	}

	public List<Token> getBoby() {
		return boby;
	}

	public void setBoby(List<Token> boby) {
		this.boby = boby;
	}

	public String getHeadString() {
		return headString;
	}

	public void setHeadString(String headString) {
		this.headString = headString;
	}

}
