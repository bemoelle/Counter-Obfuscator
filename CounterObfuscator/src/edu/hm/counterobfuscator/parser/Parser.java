package edu.hm.counterobfuscator.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.codec.EncoderException;

import edu.hm.counterobfuscator.parser.token.ITokenAnalyser;
import edu.hm.counterobfuscator.parser.token.TokenAnalyserFactory;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.ProgrammTree;
import edu.hm.counterobfuscator.types.Function;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 30.12.2014
 * 
 *       JSParser to tokenise and analyse an input file (JSCode)
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
	 * @param file
	 * @param settings
	 * @throws IOException
	 */
	public Parser(String file, Map<String, String> settings) throws IOException {

		Parser.log = Logger.getLogger(Parser.class.getName());

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

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.parser.IParser#getProgrammTree()
	 */
	public IProgrammTree getProgrammTree() {

		return programmTree;
	}

}
