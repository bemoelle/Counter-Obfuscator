/**
 * 
 */
package edu.hm.counterobfuscator.types;

import edu.hm.counterobfuscator.helper.Scope;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 15.01.2015
 * 
 * 
 */
public class This extends AbstractType {

	private String	value;
	private String	notation;

	/**
	 * @param type
	 * @param pos
	 * @param name
	 */
	public This(Scope pos, String name, String notation,  String value) {
		super(TYPE.THIS, pos, name);
		this.notation = notation;
		this.value = value;
		
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	public String getNotation() {

		return notation;
	}

	public void setNotation(String notation) {

		this.notation = notation;
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

}
