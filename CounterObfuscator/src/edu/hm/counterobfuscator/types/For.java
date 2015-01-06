/**
 * 
 */
package edu.hm.counterobfuscator.types;

import edu.hm.counterobfuscator.helper.Position;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 06.01.2015
 * 
 * 
 */
public class For extends AbstractType {

	/**
	 * @param type
	 * @param pos
	 * @param name
	 */
	public For(Position pos, String name) {
		super(TYPE.FOR, pos, name);
		
	}

}
