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
public class Return extends AbstractType {

	/**
	 * @param type
	 * @param pos
	 * @param name
	 */
	public Return(Position pos, String name) {
		super(TYPE.RETURN, pos, name);
	}

}
