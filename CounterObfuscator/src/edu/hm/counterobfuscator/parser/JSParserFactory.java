package edu.hm.counterobfuscator.parser;

import java.io.IOException;

public class JSParserFactory {

	/**
	 * @param String
	 *           file
	 * @return jsParser
	 * @throws IOException
	 */
	public static IJSParser create(String file) throws IOException {

		// TODO settings ?!?
		JSParser jsParser = new JSParser(file, null);
		jsParser.process();
		
		return jsParser;
	}

}
