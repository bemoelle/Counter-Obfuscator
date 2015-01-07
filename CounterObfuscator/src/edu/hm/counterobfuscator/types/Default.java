/**
 * 
 */
package edu.hm.counterobfuscator.types;

import edu.hm.counterobfuscator.helper.Position;

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
	public Default(Position pos, String name) {
		super(TYPE.DEFAULT, pos, name);
	}

}
