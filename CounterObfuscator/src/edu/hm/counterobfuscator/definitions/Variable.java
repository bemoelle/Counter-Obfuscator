package edu.hm.counterobfuscator.definitions;

import edu.hm.counterobfuscator.helper.Scope;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 *       represent JavaScript VAR assign to DEFINITION.VAR
 * 
 */
public class Variable extends AbstractType {

	private String value;
	private boolean isGlobal;
	private boolean executable;
	private boolean isObject;
	private String parameter;
	private String	assign;

	public Variable(Scope pos, String name, String assign, String value, boolean isObject) {
		super(DEFINITION.VARIABLE, pos, name);
		this.assign = assign;

		value = value.replaceAll("^\\s", "");
		
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

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.types.AbstractType#hasNameInIt(java.lang.String)
	 */
	@Override
	public boolean hasNameInIt(String nameToTest) {
		
		String toTest = name + value;
		
		if(toTest.indexOf(nameToTest) != -1)
			return true;
		
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.types.AbstractType#replaceName(java.lang.String, java.lang.String)
	 */
	@Override
	public void replaceNameWith(String nameToReplace, String valueToReplace) {

		//name = name.replace(nameToReplace, valueToReplace);
		value = value.replace(nameToReplace, valueToReplace);
		
	}

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.types.AbstractType#replaceName(java.lang.String)
	 */
	@Override
	public void replaceName(String name) {

		this.name = name;
		
	}

}
