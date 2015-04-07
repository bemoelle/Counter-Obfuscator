package edu.hm.counterobfuscator.parser;

import java.io.IOException;

import org.apache.commons.codec.EncoderException;

public class ParserFactory {

	/**
	 * @param String
	 *            file
	 * @return jsParser
	 * @throws IOException
	 * @throws EncoderException
	 * @throws IllegalArgumentException
	 * 
	 *             class to create the parser
	 */
	public static IParser create(String file, boolean isFile)
			throws IOException, IllegalArgumentException, EncoderException {

		Parser jsParser = new Parser(file, isFile, null);
		jsParser.process();

		return jsParser;
	}

}
