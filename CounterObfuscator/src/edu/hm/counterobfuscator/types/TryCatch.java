/**
 * 
 */
package edu.hm.counterobfuscator.types;

import java.util.List;

import edu.hm.counterobfuscator.helper.Position;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 06.02.2015
 * 
 * 
 */
public class TryCatch extends AbstractType {

	private List<Variable>	head;
	private boolean			isPacked;
	private String				bodyAsString;

	public TryCatch(Position pos, String name, String headString) {
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
	}}
