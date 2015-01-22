/**
 * 
 */
package edu.hm.counterobfuscator.parser.tree;

import edu.hm.counterobfuscator.types.AbstractType;
import edu.hm.counterobfuscator.types.Call;
import edu.hm.counterobfuscator.types.Variable;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 21.01.2015
 * 
 * 
 */
public final class ValueExtractor {

	private ValueExtractor() {
	}

	static public String getName(Element element) {

		return element.getType().getName();

	}

	static public String getValue(Element element) {

		AbstractType type = element.getType();

		switch (type.getType()) {
		case VARIABLE:
			return ((Variable) type).getValue();
		case CALL:
			return ((Call) type).getValue();
		default:
			return "";
		}
	}

	static public void setValue(Element element, String value) {

		AbstractType type = element.getType();

		switch (type.getType()) {
		case VARIABLE:
			((Variable) type).setValue(value);
			break;
		case CALL:
			((Call) type).setValue(value);
			break;
		default:

		}
	}

	/**
	 * @param element
	 * @param newName
	 */
	public static void setName(Element element, String newName) {
		
		element.getType().setName(newName);
		
	}

}
