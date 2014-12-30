package edu.hm.webscraper.interpreter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Logger;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import edu.hm.webscraper.HTMLUnitClient;
import edu.hm.webscraper.parser.IJSParser;
import edu.hm.webscraper.types.Function;
import edu.hm.webscraper.types.IType;

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

		List<IType> vars = jsParser.getTokenAnalyser().getVars();

		String tmpBuffer = "";

		for (IType v : vars) {

			v.print();
			System.out.println(jsParser.getTokenAnalyser().getNameOfType(v));
			System.out.println(jsParser.getTokenAnalyser().getValueOfType(v));

		}

		log.info("finished renaming process");

	}

}
