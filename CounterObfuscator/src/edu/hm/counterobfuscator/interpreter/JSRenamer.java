package edu.hm.counterobfuscator.interpreter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Logger;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import edu.hm.counterobfuscator.HTMLUnitClient;
import edu.hm.counterobfuscator.IClient;
import edu.hm.counterobfuscator.parser.IJSParser;
import edu.hm.counterobfuscator.parser.token.TOKENTYPE;
import edu.hm.counterobfuscator.parser.token.Token;
import edu.hm.counterobfuscator.types.AbstractType;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.types.IType;
import edu.hm.counterobfuscator.types.Variable;

/**
 * @author
 * @date 30.12.2014
 * 
 *       class to rename obfuscated names, replace them with the assigned value
 */
public class JSRenamer implements IInterpreter {

	private IClient			client;
	private IJSParser			jsParser;
	private static Logger	log;

	public JSRenamer(IJSParser jsParser, IClient client) {

		JSRenamer.log = Logger.getLogger(Function.class.getName());

		this.jsParser = jsParser;
		this.client = client;

	}

	public void process() {

		log.info("start renaming process...");

		List<Function> vars = jsParser.getTypesOfToken(TOKENTYPE.FUNCTION);

		List<Token> tokens = jsParser.getTokens();

		String scriptBuffer = "";

		for (Function v : vars) {

			v.print();

			String name = v.getName();
			String head = v.getHead();
			String body = v.getBoby();

			//Object result = client.getJSResult(scriptBuffer + value);

			List<Integer> listOfTokens = jsParser.getAllPosOfTokensByValue(name);

//			for (int pos : listOfTokens) {
//
//				Token actualToken = tokens.get(pos);
//				if (actualToken.getPos() != v.getStartPos()) {
//					actualToken.setValue("");
//				}
//			}
			System.out.println(name + "--" + head + "--" + body);
		}

		log.info("finished renaming process");

	}

}
