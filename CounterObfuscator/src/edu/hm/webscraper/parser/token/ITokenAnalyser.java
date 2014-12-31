package edu.hm.webscraper.parser.token;

import java.util.List;

import edu.hm.webscraper.types.IType;

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
	 * @param v
	 * @return
	 */
	public String getNameOfType(IType v);

	/**
	 * @param v
	 * @return
	 */
	public String getValueOfType(IType v);

	/**
	 * @return
	 */
	public List<IType> getTypesOfTokenTypes(TOKENTYPE type);

}
