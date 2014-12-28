package edu.hm.webscraper.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.helper.Validate;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import edu.hm.webscraper.IClient;
import edu.hm.webscraper.helper.Function;
import edu.hm.webscraper.parser.token.TokenAnalyser;
import edu.hm.webscraper.parser.token.TokenAnalyserFactory;

public class JSParser {

	private BufferedReader br;
	private String unparsedJSCode;
	private static Logger log;
	private Pattern hexStringPattern;
	
	public JSParser(IClient client, String file, Map<String, String> settings)
			throws IOException {

		JSParser.log = Logger.getLogger(Function.class.getName());

		this.unparsedJSCode = "";

		br = new BufferedReader(new FileReader(file));
		String line = "";

		while ((line = br.readLine()) != null) {
			unparsedJSCode += line;
		}

		br.close();
		log.info("read JavaScript Code:" + unparsedJSCode);

		hexStringPattern = Pattern.compile("'[\\\\x{1}[\\d|\\w]*]*'");
	}

	public String process() throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {

		log.info("start parsing jscode...");

//		 entfernen aller Hochkomma wichtig für die regexp
		 String parsed = unparsedJSCode.replaceAll("\"", "\'");
		 // replace function
		// parsed = parsed.replaceAll("\\\\", "hex");
		 log.info("read JavaScript Code:" + parsed);
		
		 Matcher hexStringMatcher = hexStringPattern.matcher(parsed);
		
		 while (hexStringMatcher.find()) {
		 parsed = parsed.replaceAll(hexStringMatcher.group(),
		 "'" + hexToASCII(hexStringMatcher.group()) + "'");
		 }

		 TokenAnalyser tokenanalyser = TokenAnalyserFactory.create(unparsedJSCode);
		
		return unparsedJSCode;
	}

	private static String hexToASCII(String hex) {

		Validate.notNull(hex);
		
		if (hex.indexOf("hex") > -1) {

			hex = hex.replaceAll("[\\\\|hex|']", "");

			if (hex.length() % 2 != 0) {
				System.err.println("requires EVEN number of chars: " +  hex);
				return null;
			}
			StringBuilder sb = new StringBuilder();
			// Convert Hex 0232343536AB into two characters stream.
			for (int i = 0; i < hex.length() - 1; i += 2) {
				String output = hex.substring(i, (i + 2));
				int decimal = Integer.parseInt(output, 16);
				sb.append((char) decimal);
			}
			return sb.toString();
		} else {
			return "";
		}
	}

	public static String fromCharCode(int... codePoints) {
		return new String(codePoints, 0, codePoints.length);
	}
}
