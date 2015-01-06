package edu.hm.counterobfuscator.parser.token;

import java.io.IOException;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 25.12.2014
 * 
 *       Factory to create Tokenizer and Tokenanalyser and call process() method
 *       of both classes
 */
public class TokenAnalyserFactory {

	/**
	 * @param String
	 *           input
	 * @return TokenAnalyser
	 * @throws IOException 
	 */
	public static TokenAnalyser create(String input) throws IOException {

		Tokenizer tokenizer = new Tokenizer(input);
		tokenizer.process();
		

		TokenAnalyser analyser = new TokenAnalyser(tokenizer);
		analyser.process();

		return analyser;
	}

}
