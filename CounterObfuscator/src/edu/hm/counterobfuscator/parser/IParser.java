package edu.hm.counterobfuscator.parser;

import edu.hm.counterobfuscator.parser.tree.IProgrammTree;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 11.02.2015
 * 
 * 
 */
public interface IParser {
	
	/**
	 * @return actual programm tree
	 */
	public IProgrammTree getProgrammTree();

}
