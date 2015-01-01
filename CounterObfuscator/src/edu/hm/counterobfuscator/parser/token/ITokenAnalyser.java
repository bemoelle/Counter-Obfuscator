package edu.hm.counterobfuscator.parser.token;

import java.util.List;

import edu.hm.counterobfuscator.types.IType;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 31.12.2014
 * 
 * 
 */
public interface ITokenAnalyser {

	/**
	 * @return
	 */
	public List<Token> getTokens();

	/**
	 * @return
	 */
	public List<IType> getTypesOfTokenTypes(TOKENTYPE type);

	/**
	 * @param value
	 * @return
	 */
	public List<Integer> getAllPosOfTokensByValue(String value);

}