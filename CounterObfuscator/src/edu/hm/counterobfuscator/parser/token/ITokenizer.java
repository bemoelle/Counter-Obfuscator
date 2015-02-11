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
	 * 
	 */
	void process();

	/**
	 * @return
	 */
	List<Token> getTokens();

}
