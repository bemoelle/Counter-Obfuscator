/**
 * 
 */
package edu.hm.counterobfuscator.types;

import edu.hm.counterobfuscator.helper.Scope;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 07.01.2015
 * 
 * 
 */
public class Default extends AbstractType {

	/**
	 * @param type
	 * @param pos
	 * @param name
	 */
	public Default(Scope pos, String name) {
		super(TYPE.DEFAULT, pos, name);
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

}
