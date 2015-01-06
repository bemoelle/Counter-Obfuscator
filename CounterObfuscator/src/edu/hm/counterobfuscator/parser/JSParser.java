package edu.hm.counterobfuscator.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.hm.counterobfuscator.IClient;
import edu.hm.counterobfuscator.parser.token.TOKENTYPE;
import edu.hm.counterobfuscator.parser.token.TokenAnalyserFactory;
import edu.hm.counterobfuscator.types.AbstractType;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.parser.token.ITokenAnalyser;
import edu.hm.counterobfuscator.parser.token.Token;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 30.12.2014
 * 
 *       JSParser to tokenise and analyse an input file (JSCode)
 * 
 */
public class JSParser implements IJSParser {

	private BufferedReader	br;
	private String				unparsedJSCode;
	private static Logger	log;
	private ITokenAnalyser	tokenanalyser;

	/**
	 * @param client
	 * @param file
	 * @param settings
	 * @throws IOException
	 */
	public JSParser(IClient client, String file, Map<String, String> settings) throws IOException {

		JSParser.log = Logger.getLogger(Function.class.getName());

		this.unparsedJSCode = "";

		br = new BufferedReader(new FileReader(file));
		String line = "";

		while ((line = br.readLine()) != null) {
			unparsedJSCode += line;
		}

		br.close();
		log.info("read JavaScript Code:" + unparsedJSCode);
	}

	/**
	 * @throws IOException
	 * 
	 */
	public void process() throws IOException {

		log.info("start parsing jscode...");

		String parsed = unparsedJSCode.replaceAll("\"", "\\\"");

		tokenanalyser = TokenAnalyserFactory.create(parsed);

		log.info("parsing jscode finished");

		printAllTokens();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.webscraper.parser.IJSParser#printAllTokens()
	 */
	public void printAllTokens() {

		for (Token token : tokenanalyser.getTokens()) {

			System.out.println(token.getPos() + " : " + token.getValue());
			// System.out.println(token.getValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.IJSParser#getTypesOfToken(edu.hm.
	 * counterobfuscator.parser.token.TOKENTYPE)
	 */
	public List<AbstractType> getTypesOfToken(TOKENTYPE type) {

		return tokenanalyser.getTypesOfTokenTypes(type);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.webscraper.parser.IJSParser#getTokens()
	 */
	public List<AbstractType> getAlltypes() {
		return tokenanalyser.getAllTypes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.counterobfuscator.parser.IJSParser#getAllPosOfTokensByValue(java
	 * .lang.String)
	 */
	public List<Integer> getAllPosOfTokensByValue(String value) {
		return tokenanalyser.getAllPosOfTokensByValue(value);
	}
}
