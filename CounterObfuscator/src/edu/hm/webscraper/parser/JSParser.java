package edu.hm.webscraper.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import edu.hm.webscraper.IClient;
import edu.hm.webscraper.parser.token.Token;
import edu.hm.webscraper.parser.token.TokenAnalyser;
import edu.hm.webscraper.parser.token.TokenAnalyserFactory;
import edu.hm.webscraper.types.Function;

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
	private TokenAnalyser	tokenanalyser;

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

	public List<Token> getTokens() {
		return tokenanalyser.getTokenizer().getTokens();
	}

	public TokenAnalyser getTokenAnalyser() {
		return tokenanalyser;
	}

	public void printAllTokens() {

		for (Token token : getTokens()) {

			System.out.println(token.getPos() + " : " + token.getValue());
		}
	}

}
