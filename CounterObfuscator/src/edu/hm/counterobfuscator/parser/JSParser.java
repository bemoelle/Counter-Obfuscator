package edu.hm.counterobfuscator.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import edu.hm.counterobfuscator.IClient;
import edu.hm.counterobfuscator.parser.token.TOKENTYPE;
import edu.hm.counterobfuscator.parser.token.TokenAnalyserFactory;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.types.IType;
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

	public void process() throws FailingHttpStatusCodeException, MalformedURLException, IOException {

		log.info("start parsing jscode...");

		String parsed = unparsedJSCode.replaceAll("\"", "\'");

		tokenanalyser = TokenAnalyserFactory.create(parsed);

		log.info("parsing jscode finished");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.webscraper.parser.IJSParser#printAllTokens()
	 */
	public void printAllTokens() {

		for (Token token : tokenanalyser.getTokens()) {

			System.out.println(token.getPos() + " : " + token.getValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.webscraper.parser.IJSParser#getTypesOfTokenTypes(edu.hm.webscraper
	 * .parser.token.TOKENTYPE)
	 */
	public List<IType> getTypesOfToken(TOKENTYPE var) {
		return tokenanalyser.getTypesOfTokenTypes(var);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.webscraper.parser.IJSParser#getTokens()
	 */
	public List<Token> getTokens() {
		return tokenanalyser.getTokens();
	}
	
	public List<Integer> getAllPosOfTokensByValue(String value) {
		return tokenanalyser.getAllPosOfTokensByValue(value);
	}
}
