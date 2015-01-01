package edu.hm.counterobfuscator.interpreter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Logger;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import edu.hm.counterobfuscator.HTMLUnitClient;
import edu.hm.counterobfuscator.parser.IJSParser;
import edu.hm.counterobfuscator.parser.token.TOKENTYPE;
import edu.hm.counterobfuscator.parser.token.Token;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.types.IType;

/**
 * @author
 * @date 30.12.2014
 * 
 *       class to rename obfuscated names, replace them with the assigned value
 */
public class JSRenamer {

	private HTMLUnitClient	client;
	private IJSParser			jsParser;
	private static Logger	log;

	public JSRenamer(IJSParser jsParser) throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		JSRenamer.log = Logger.getLogger(Function.class.getName());

		this.jsParser = jsParser;

		client = new HTMLUnitClient("http://www.google.com/", BrowserVersion.FIREFOX_24);
	}

	public void process() throws FailingHttpStatusCodeException, MalformedURLException, IOException {

		log.info("start renaming process...");

		List<IType> vars = jsParser.getTypesOfToken(TOKENTYPE.VAR);

		List<Token> tokens = jsParser.getTokens();

		String scriptBuffer = "";

		for (IType v : vars) {

			v.print();

			String name = v.getName();
			String value = v.getValue();

			Object result = client.getJSResult(scriptBuffer + value);

			List<Integer> listOfTokens = jsParser.getAllPosOfTokensByValue(name);

			for (int pos : listOfTokens) {

				Token actualToken = tokens.get(pos);
				if (actualToken.getPos() != v.getStartPos()) {
					actualToken.setValue(result + "");
				}
			}
			scriptBuffer += name + "=" + value + ";";
		}

		log.info("finished renaming process");

	}

}
