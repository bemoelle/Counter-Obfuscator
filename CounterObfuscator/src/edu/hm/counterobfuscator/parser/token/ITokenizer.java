/**
 * 
 */
package edu.hm.counterobfuscator.parser.token;

import java.util.List;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 11.02.2015
 * 
 *       this class transform a string in a representation of tokens, these
 *       tokens are delivered to the TokenAnalyser class {@link TokenAnalyser}
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
