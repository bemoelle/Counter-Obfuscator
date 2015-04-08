package edu.hm.counterobfuscator.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.codec.EncoderException;

import edu.hm.counterobfuscator.definitions.Function;
import edu.hm.counterobfuscator.helper.Validate;
import edu.hm.counterobfuscator.parser.token.ITokenAnalyser;
import edu.hm.counterobfuscator.parser.token.TokenAnalyserFactory;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.ProgrammTree;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 30.12.2014
 * 
 *       Parser to tokenise and analyse an input file (JSCode)
 * 
 */
public class Parser implements IParser {

	private BufferedReader br;
	private String unparsedJSCode;
	private static Logger log;
	private ITokenAnalyser tokenanalyser;
	private IProgrammTree programmTree;

	/**
	 * @param client
	 * @param input
	 * @param isFile
	 * @param settings
	 * @throws IOException
	 */
	public Parser(String input, boolean isFile, Map<String, String> settings)
			throws IOException {

		Validate.notNull(input);

		Parser.log = Logger.getLogger(Parser.class.getName());

		this.unparsedJSCode = "";

		if (isFile) {

			br = new BufferedReader(new FileReader(input));
			String line = "";

			while ((line = br.readLine()) != null) {
				unparsedJSCode += line;
			}

			br.close();
		} else {
			unparsedJSCode = input;
		}
		log.info("read JavaScript Code:" + unparsedJSCode);
	}

	/**
	 * @throws IOException
	 * @throws EncoderException
	 * @throws IllegalArgumentException
	 * 
	 */
	public void process() throws IOException, IllegalArgumentException,
			EncoderException {

		log.info("start parsing jscode...");

		String parsed = unparsedJSCode.replaceAll("\"", "\\\"");

		tokenanalyser = TokenAnalyserFactory.create(parsed);

		programmTree = new ProgrammTree(tokenanalyser.getAllTypes());

		log.info("parsing jscode finished");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.IParser#getProgrammTree()
	 */
	public IProgrammTree getProgrammTree() {

		return programmTree;
	}

}
