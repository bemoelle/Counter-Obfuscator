package edu.hm.counterobfuscator.parser.token;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.codec.EncoderException;

import edu.hm.counterobfuscator.helper.Validate;

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
	public static ITokenAnalyser create(String input) throws IllegalArgumentException, EncoderException {

		Logger log = Logger.getLogger(TokenAnalyserFactory.class.getName());
		
		Validate.notNull(input);
		
		ITokenizer tokenizer = new Tokenizer(input);
		tokenizer.process();

		ITokenAnalyser analyser = new TokenAnalyser(tokenizer);
		analyser.process();
		
		log.info("TokenAnalyser creation finished");

		return analyser;
	}

}
