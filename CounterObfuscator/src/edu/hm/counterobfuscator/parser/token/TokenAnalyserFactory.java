package edu.hm.counterobfuscator.parser.token;

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
	 */
	public static TokenAnalyser create(String input) {

		Tokenizer tokenizer = new Tokenizer(input);
		tokenizer.process();

		TokenAnalyser analyser = new TokenAnalyser(tokenizer);
		analyser.process();

		return analyser;
	}

}
