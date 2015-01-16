package edu.hm.counterobfuscator.parser;

import java.io.IOException;

import org.apache.commons.codec.EncoderException;

public class JSParserFactory {

	/**
	 * @param String
	 *           file
	 * @return jsParser
	 * @throws IOException
	 * @throws EncoderException 
	 * @throws IllegalArgumentException 
	 */
	public static IJSParser create(String file) throws IOException, IllegalArgumentException, EncoderException {

		// TODO settings ?!?
		JSParser jsParser = new JSParser(file, null);
		jsParser.process();
		
		return jsParser;
	}

}
