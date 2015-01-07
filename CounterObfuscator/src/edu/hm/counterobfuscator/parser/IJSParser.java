package edu.hm.counterobfuscator.parser;

import java.util.ArrayList;
import java.util.List;

import edu.hm.counterobfuscator.parser.token.trees.TypeTreeElement;
import edu.hm.counterobfuscator.types.AbstractType;

public interface IJSParser {
	
	public void printAllTokens();

	/**
	 * @return
	 */
	public List<AbstractType> getAlltypes();

	/**
	 * @param value
	 * @return
	 */
	public List<Integer> getAllPosOfTokensByValue(String value);

	/**
	 * @return
	 */
	public ArrayList<TypeTreeElement> getProgrammTree();

}
