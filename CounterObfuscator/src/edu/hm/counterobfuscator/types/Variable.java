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

	private String		value;
	private boolean	isGlobal;
	private boolean	noexe;
	private boolean	isArray;
	private boolean	hasNew;

	public Variable(Position pos, String name, String value) {
		super(TYPE.VARIABLE, pos, name);

		if (value.equals("")) {
			this.value = "undefined";
		}
		else {
			this.value = value;
		}

		this.isGlobal = false;
		this.isArray = false;
		this.hasNew = false;
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
		return hasNew;
	}

	public void setHasNew(boolean hasNew) {
		this.hasNew = hasNew;
	}

}
