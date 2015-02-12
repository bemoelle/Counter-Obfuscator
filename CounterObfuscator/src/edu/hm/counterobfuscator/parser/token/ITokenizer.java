/**
 * 
 */
package edu.hm.counterobfuscator.parser.token;

import java.util.List;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 11.02.2015
 * 
 * 
 */
public interface ITokenizer {

	/**
	 * transform input string in a representation of tokens
	 */
	void process();

	/**
	 * @return tokens
	 */
	List<Token> getTokens();

}
