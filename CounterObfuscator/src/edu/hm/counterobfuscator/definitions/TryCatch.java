/**
 * 
 */
package edu.hm.counterobfuscator.definitions;

import edu.hm.counterobfuscator.helper.Scope;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 06.02.2015
 * 
 *       represent JavaScript TRY-CATCH assign to DEFINITION.TRY or
 *       DEFINITION.CATCH
 */
public class TryCatch extends AbstractType {

	public TryCatch(Scope pos, String name, String headString) {

		super(DEFINITION.TRYCATCH, pos, name);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.counterobfuscator.types.AbstractType#hasSameName(java.lang.Object)
	 */
	@Override
	public boolean hasSameName(Object other) {

		if (name.equals(((AbstractType) other).getName())) {
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

		return "";
	}

	@Override
	public void replaceValueWith(String name, String value) {

		// nothing to do here

	}
}
