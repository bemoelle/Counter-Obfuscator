package edu.hm.counterobfuscator.types;

import java.util.ArrayList;
import java.util.List;

import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.parser.token.Token;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 *       represent JavaScript FUNCTION assign to TOKENTYPE.FUNCTION
 * 
 */
public class Function extends AbstractType {

	private List<Variable>	head;
	private String				body;
	private boolean			isPacked;
	private String				headString;

	public Function(Position pos, String name, String headString, boolean isPacked) {
		super(TYPE.FUNCTION, pos, name);
		this.headString = headString;
		this.isPacked = isPacked;
		this.body = "";
		this.head = new ArrayList<Variable>();

		String[] heads = headString.split(",");
		for (String varName : heads) {
			head.add(new Variable(pos, varName, "", false));
		}

	}

	public List<Variable> getHead() {
		return head;
	}

	public String getBody() {
		return body;
	}

	public boolean isPacked() {
		return isPacked;
	}

	public void setBody(String boby) {
		this.body = boby;
	}

	public String getHeadString() {
		return headString;
	}

	public void setHeadString(String headString) {
		this.headString = headString;
	}

}
