package edu.hm.counterobfuscator.parser;

import java.io.IOException;

import org.apache.commons.codec.EncoderException;

public class ParserFactory {

	/**
	 * @param String
	 *           file
	 * @return jsParser
	 * @throws IOException
	 * @throws EncoderException 
	 * @throws IllegalArgumentException 
	 */
	public static IParser create(String file) throws IOException, IllegalArgumentException, EncoderException {

		// TODO settings ?!?
		Parser jsParser = new Parser(file, null);
		jsParser.process();
		
		return jsParser;
	}

}
