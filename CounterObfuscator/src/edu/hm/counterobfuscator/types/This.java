/**
 * 
 */
package edu.hm.counterobfuscator.types;

import edu.hm.counterobfuscator.helper.Position;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 15.01.2015
 * 
 * 
 */
public class This extends AbstractType {

	private String	value;

	/**
	 * @param type
	 * @param pos
	 * @param name
	 */
	public This(Position pos, String name, String value) {
		super(TYPE.THIS, pos, name);
		this.value = value;
		
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
