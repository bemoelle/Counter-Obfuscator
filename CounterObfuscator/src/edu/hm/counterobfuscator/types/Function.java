package edu.hm.counterobfuscator.types;

import java.util.ArrayList;
import java.util.List;

import edu.hm.counterobfuscator.helper.Scope;

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
	private String				bodyAsString;

	public Function(Scope pos, String name, String headString, String bodyAsString,
			boolean isPacked) {
		super(TYPE.FUNCTION, pos, name);

		this.bodyAsString = bodyAsString;
		this.isPacked = isPacked;
		this.head = new ArrayList<Variable>();

		if (!headString.equals("()")) {
			headString = headString.substring(1, headString.indexOf(")"));
			String[] heads = headString.split(",");
			for (String varName : heads) {
				head.add(new Variable(pos, varName, "", "", false));
			}
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

		String headString = "";
		for(int i=0; i<head.size(); i++) {
			
			headString += head.get(i).getName();
			if(i<head.size()-1) {
				headString += ",";
			}
		}
		
		return headString;
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

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.types.AbstractType#hasNameInIt(java.lang.String)
	 */
	@Override
	public boolean hasNameInIt(String name) {
		// TODO Auto-generated method stub
		return false;
	}

}
