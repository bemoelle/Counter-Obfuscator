package edu.hm.counterobfuscator.parser;

import java.util.List;

import edu.hm.counterobfuscator.parser.token.TOKENTYPE;
import edu.hm.counterobfuscator.types.AbstractType;

public interface IJSParser {
	
	public void printAllTokens();

	/**
	 * @param <T>
	 * @param var
	 * @return
	 */
	public <T> List<T> getTypesOfToken(TOKENTYPE var);

	/**
	 * @return
	 */
	public List<AbstractType> getAlltypes();

	/**
	 * @param value
	 * @return
	 */
	public List<Integer> getAllPosOfTokensByValue(String value);

}
