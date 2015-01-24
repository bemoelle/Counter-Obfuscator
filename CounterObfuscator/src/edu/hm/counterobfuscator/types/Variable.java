package edu.hm.counterobfuscator.types;

import net.sourceforge.htmlunit.corejs.javascript.ast.Name;
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
	private boolean executable;
	private boolean isObject;
	private String parameter;
	private String	assign;

	public Variable(Position pos, String name, String assign, String value, boolean isObject) {
		super(TYPE.VARIABLE, pos, name);
		this.assign = assign;

		if (isObject) {
			this.value = value.substring(4, value.indexOf("("));
			this.parameter = value.substring(value.indexOf("(") + 1,
					value.indexOf(")"));

		} else {
			this.value = value;
			this.parameter = "";
		}

		this.isGlobal = false;
		this.executable = true;
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

	public boolean isObject() {
		return isObject;
	}
	
	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public boolean isExecutable() {
		return executable;
	}

	public void setExecutable(boolean executable) {
		this.executable = executable;
	}

	public String getAssign() {
		return assign;
	}

	public void setAssign(String assign) {
		this.assign = assign;
	}

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.types.AbstractType#hasSameName(java.lang.Object)
	 */
	@Override
	public boolean hasSameName(Object other) {
		
		if(!(other instanceof Variable)) {
			return false;
		}
		
		Variable var = (Variable) other;
		
		if(super.name.equals(var.name)) {
			if(assign.equals(var.assign)) {
				return true;
			}
		}
		
		return false;
	}

}
