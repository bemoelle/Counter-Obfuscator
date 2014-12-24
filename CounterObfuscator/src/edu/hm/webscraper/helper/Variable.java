package edu.hm.webscraper.helper;

/*
 * represent an JavaScript VAR
 * assign to @TOKENTYPE.VAR
 */
public class Variable {

	private String name;
	private String value;
	private boolean isGlobal;
	private boolean isObject;
	private int pos;

	public Variable(int pos, String name, String value) {
		this.pos = pos;
		this.name = name;
		this.value = value;
//		this.isGlobal = false;
//		this.isObject = false;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public boolean isGlobal() {
		return isGlobal;
	}

	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}

	public boolean isObject() {
		return isObject;
	}

	public void setObject(boolean isObject) {
		this.isObject = isObject;
	}
}
