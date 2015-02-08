package edu.hm.counterobfuscator.parser.tree;

import java.util.List;

import edu.hm.counterobfuscator.types.AbstractType;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 09.01.2015
 *
 */
public final class TreeCreator {

	private static IProgrammTree programmTree;

	/**
	 * 
	 */
	public TreeCreator() {
		//noting to do here
	}

	/**
	 * @param allTypes
	 * @return
	 */
	public static IProgrammTree createTypeTree(List<AbstractType> allTypes) {

		
		programmTree = new ProgrammTree();
		
		
		return programmTree;
	}

	

}
