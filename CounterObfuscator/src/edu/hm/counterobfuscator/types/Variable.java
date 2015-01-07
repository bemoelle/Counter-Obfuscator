package edu.hm.counterobfuscator.types;

import edu.hm.counterobfuscator.helper.Position;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 *       represent JavaScript VAR assign to TOKENTYPE.VAR
 * 
 */
public class Variable extends AbstractType {

	private String	value;
	private boolean isGlobal;

	public Variable(Position pos, String name, String value, boolean isGlobal) {
		super(TYPE.VARIABLE, pos, name);

		if (value.equals("")) {
			this.value = "undefined";
		}
		else {
			this.value = value;
		}
		
		this.isGlobal = isGlobal;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isGlobal() {
		return isGlobal;
	}

	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}

}
