package edu.hm.counterobfuscator.parser;

import edu.hm.counterobfuscator.parser.tree.IProgrammTree;

public interface IJSParser {
	
	public void printAllTokens();

	/**
	 * @return
	 */
	public IProgrammTree getProgrammTree();

}
