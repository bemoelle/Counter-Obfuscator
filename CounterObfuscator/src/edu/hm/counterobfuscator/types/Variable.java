package edu.hm.counterobfuscator.types;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 *       represent JavaScript VAR assign to TOKENTYPE.VAR
 * 
 */
public class Variable extends AbstractType {

	private String	value;

	public Variable(int startPos, int endPos, String name, String value) {
		super(startPos, endPos, name);
		if(value.equals("")) {
			this.value = "undefined";
		} else {
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
