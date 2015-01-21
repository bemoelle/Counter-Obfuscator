package edu.hm.counterobfuscator.types;

import java.util.ArrayList;
import java.util.List;

import edu.hm.counterobfuscator.helper.Position;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 *       represent JavaScript FUNCTION assign to TOKENTYPE.FUNCTION
 * 
 */
public class Function extends AbstractType {

	private List<Variable>	head;
	private boolean			isPacked;
	private String				headString;
	private String				bodyAsString;

	public Function(Position pos, String name, String headString, String bodyAsString,
			boolean isPacked) {
		super(TYPE.FUNCTION, pos, name);

		this.headString = headString;
		this.bodyAsString = bodyAsString;
		this.isPacked = isPacked;
		this.head = new ArrayList<Variable>();

		headString = headString.substring(1,headString.indexOf(")"));
		String[] heads = headString.split(",");
		for (String varName : heads) {
			head.add(new Variable(pos, varName, "", false));
		}

	}

	public List<Variable> getHead() {
		return head;
	}

	public String getBodyAsString() {
		return bodyAsString;
	}

	public boolean isPacked() {
		return isPacked;
	}

	public void setBodyAsString(String bodyAsString) {
		this.bodyAsString = bodyAsString;
	}

	public String getHeadString() {
		return headString;
	}

	public void setHeadString(String headString) {
		this.headString = headString;
	}

}
