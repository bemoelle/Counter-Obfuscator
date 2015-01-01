package edu.hm.counterobfuscator.parser;

import java.util.List;

import edu.hm.counterobfuscator.parser.token.TOKENTYPE;
import edu.hm.counterobfuscator.types.IType;
import edu.hm.counterobfuscator.parser.token.Token;

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
