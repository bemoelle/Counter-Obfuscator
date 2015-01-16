package edu.hm.counterobfuscator.parser.token;

import java.io.IOException;

import org.apache.commons.codec.EncoderException;

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
	 * @throws EncoderException 
	 * @throws IllegalArgumentException 
	 */
	public static TokenAnalyser create(String input) throws IOException, IllegalArgumentException, EncoderException {

		Tokenizer tokenizer = new Tokenizer(input);
		tokenizer.process();
		

		TokenAnalyser analyser = new TokenAnalyser(tokenizer);
		analyser.process();

		return analyser;
	}

}
