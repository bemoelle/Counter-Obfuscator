package edu.hm.counterobfuscator.types;

import edu.hm.counterobfuscator.helper.Scope;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 *       represent JavaScript FUNCTION assign to TOKENTYPE.FUNCTION
 * 
 */
public class Ajax extends AbstractType {

	private String				value;

	public Ajax(Scope pos, String name, String value) {
		super(TYPE.AJAX, pos, name);

		this.value = value;
	}

	public String getValue() {

		return value;
	}

	public void setValue(String value) {
		
		this.value = value;
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

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.types.AbstractType#hasNameInIt(java.lang.String)
	 */
	@Override
	public boolean hasNameInIt(String name) {
		// TODO Auto-generated method stub
		return false;
	}

}
