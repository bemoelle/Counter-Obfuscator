package edu.hm.webscraper.parser;

import java.util.List;

import edu.hm.webscraper.parser.token.TOKENTYPE;
import edu.hm.webscraper.parser.token.Token;
import edu.hm.webscraper.types.IType;

public interface IJSParser {
	
	public void printAllTokens();

	/**
	 * @param var
	 * @return
	 */
	public List<IType> getTypesOfToken(TOKENTYPE var);

	/**
	 * @return
	 */
	public List<Token> getTokens();

	/**
	 * @param value
	 * @return
	 */
	public List<Integer> getAllPosOfTokensByValue(String value);

}
