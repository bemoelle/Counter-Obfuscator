/**
 * 
 */
package edu.hm.counterobfuscator.definitions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hm.counterobfuscator.helper.Scope;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 15.01.2015
 * 
 *       represent JavaScript RETURN statement assign to DEFINITION.RETURN
 */
public class Return extends AbstractType {

	/**
	 * @param type
	 * @param pos
	 * @param name
	 */
	public Return(Scope pos, String name) {
		super(DEFINITION.RETURN, pos, name);
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

		// if return value contains more than one variable
		String[] names = name.split("\\+");

		for (int i = 0; i < names.length; i++) {
			if(names[i].matches(nameToTest)) {
				return true;
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.counterobfuscator.types.AbstractType#replaceName(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void replaceNameWith(String nameToReplace, String valueToReplace) {

		// if return value contains more than one variable
		String[] names = name.split("\\+");

		for (int i = 0; i < names.length; i++) {

			names[i] = names[i].replaceAll("^" + nameToReplace + "$",
					valueToReplace);
		}

		String newName = "";

		if (names.length > 1) {
			for (int i = 0; i < names.length; i++) {

				newName += names[i];

				if (i < (names.length - 1)) {
					newName += "+";
				}

			}
		} else {
			newName = names[0];
		}

		name = newName;
	}

	@Override
	public void replaceValueWith(String nameToReplace, String valueToReplace) {

		replaceNameWith(nameToReplace, valueToReplace);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.counterobfuscator.types.AbstractType#replaceName(java.lang.String)
	 */
	@Override
	public void replaceName(String name) {

		// TODO Auto-generated method stub

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

}
