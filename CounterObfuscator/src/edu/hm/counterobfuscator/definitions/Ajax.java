package edu.hm.counterobfuscator.definitions;

import edu.hm.counterobfuscator.helper.Scope;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 *       represent JavaScript FUNCTION assign to DEFINITION.FUNCTION
 * 
 */
public class Ajax extends AbstractType {

	private String				value;

	public Ajax(Scope pos, String name, String value) {
		super(DEFINITION.AJAX, pos, name);

		this.value = value;
	}

	public String getValue() {

		return value;
	}

	public void setValue(String value) {
		
		this.value = value;
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

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.types.AbstractType#hasNameInIt(java.lang.String)
	 */
	@Override
	public boolean hasNameInIt(String nameToTest) {
		
		if (name.indexOf(nameToTest) != -1)
			return true;

		return false;
	}

	@Override
	public void replaceValueWith(String nameToReplace, String valueToReplace) {
		
		value = value.replaceAll(nameToReplace, valueToReplace);
		
	}

}
