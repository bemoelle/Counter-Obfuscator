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

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.types.AbstractType#hasSameName(java.lang.Object)
	 */
	@Override
	public boolean hasSameName(Object other) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.types.AbstractType#hasNameInIt(java.lang.String)
	 */
	@Override
	public boolean hasNameInIt(String name) {
		// TODO Auto-generated method stub
		return false;
	}

}
