package edu.hm.counterobfuscator.types;

import org.apache.commons.lang3.StringEscapeUtils;

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

	public Variable(Position pos, String name, String value) {
		super(TYPE.VARIABLE, pos, name);

		if (value.equals("")) {
			this.value = "undefined";
		}
		else {
			this.value = value;
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
