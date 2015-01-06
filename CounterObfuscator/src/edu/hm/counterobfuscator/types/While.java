/**
 * 
 */
package edu.hm.counterobfuscator.types;

import edu.hm.counterobfuscator.helper.Position;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 * 
 */
public class While  extends AbstractType {

	/**
	 * @param type
	 * @param pos
	 * @param name
	 */
	public While(Position pos, String name) {
		super(TYPE.WHILE, pos, name);

	}


}
