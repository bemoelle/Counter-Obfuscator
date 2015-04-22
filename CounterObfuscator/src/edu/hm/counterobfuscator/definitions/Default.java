/**
 * 
 */
package edu.hm.counterobfuscator.definitions;

import edu.hm.counterobfuscator.helper.Scope;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 07.01.2015
 * 
 *       default javascript statement like a string
 */
public class Default extends AbstractType {

	/**
	 * @param type
	 * @param pos
	 * @param name
	 */
	public Default(Scope pos, String name) {

		super(DEFINITION.DEFAULT, pos, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.counterobfuscator.types.AbstractType#hasSameName(java.lang.Object)
	 */
	@Override
	public boolean hasSameName(Object other) {

		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.counterobfuscator.types.AbstractType#hasNameInIt(java.lang.String)
	 */
	@Override
	public boolean hasNameInIt(String nameToTest) {

		if (name.indexOf(nameToTest) != -1)
			return true;

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.types.AbstractType#getValue()
	 */
	@Override
	public String getValue() {

		return name;
	}
	
	@Override
	public void replaceNameWith(String nameToReplace, String value) {
		
		name = name.replaceAll(nameToReplace, value);
	}

	@Override
	public void replaceValueWith(String name, String value) {
		System.out.println("sds");
	}

}
