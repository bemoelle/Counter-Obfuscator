/**
 * 
 */
package edu.hm.counterobfuscator.types;

import edu.hm.counterobfuscator.helper.Scope;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 06.02.2015
 * 
 * 
 */
public class TryCatch extends AbstractType {

	public TryCatch(Scope pos, String name, String headString) {
		super(TYPE.TRYCATCH, pos, name);

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


	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.types.AbstractType#replaceName(java.lang.String, java.lang.String)
	 */
	@Override
	public void replaceNameWith(String name, String value) {

		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.types.AbstractType#replaceName(java.lang.String)
	 */
	@Override
	public void replaceName(String name) {

		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.types.AbstractType#getValue()
	 */
	@Override
	public String getValue() {

		// TODO Auto-generated method stub
		return "";
	}}
