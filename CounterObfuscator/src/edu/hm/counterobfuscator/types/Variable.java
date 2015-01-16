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

	private String value;
	private boolean isGlobal;
	private boolean noexe;
	private boolean isArray;
	private boolean isObject;
	private String parameter;

	public Variable(Position pos, String name, String value, boolean isObject) {
		super(TYPE.VARIABLE, pos, name);

		if (isObject) {
			this.value = value.substring(4, value.indexOf("("));
			this.parameter = value.substring(value.indexOf("(") + 1,
					value.indexOf(")"));

		} else {
			this.value = value;
			this.parameter = "";
		}

		this.isGlobal = false;
		this.isArray = false;
		this.isObject = isObject;
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

	public void setNoExe(boolean notExe) {
		this.noexe = notExe;
	}

	public boolean getNoExe() {
		return noexe;
	}

	public boolean isArray() {
		return isArray;
	}

	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}

	public boolean isHasNew() {
		return isObject;
	}

	public void setHasNew(boolean hasNew) {
		this.isObject = hasNew;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

}
