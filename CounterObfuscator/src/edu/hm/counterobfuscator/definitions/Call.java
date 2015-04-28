/**
 * 
 */
package edu.hm.counterobfuscator.definitions;

import edu.hm.counterobfuscator.helper.Scope;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 15.01.2015
 * 
 *       e.g. obj.SayHello(_0xd237[3]);
 * 
 *       name: obj function: SayHello value: _0xd237[3]
 * 
 */
public class Call extends AbstractType {
	private String value;
	private String parameter;

	public Call(Scope pos, String name, String parameter, String value) {
		super(DEFINITION.CALL, pos, name);
		this.parameter = parameter;

		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFunction() {
		return parameter;
	}

	public void setFunction(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * @return
	 */
	public String getParameter() {
		return parameter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.counterobfuscator.types.AbstractType#hasSameName(java.lang.Object)
	 */
	@Override
	public boolean hasSameName(Object other) {
		
		if(name.equals(((AbstractType)other).getName())) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.counterobfuscator.types.AbstractType#hasNameInIt(java.lang.String)
	 */
	@Override
	public boolean hasNameInIt(String nameToTest) {

		if (name.matches("^" + nameToTest + "$")) {
			return true;
		}

		if (parameter.indexOf(nameToTest) > -1) {
			return true;
		}

		return false;
	}

	@Override
	public void replaceValueWith(String nameToReplace, String valueToReplace) {
		
		value = value.replaceAll(nameToReplace, valueToReplace);
		
	}

}
