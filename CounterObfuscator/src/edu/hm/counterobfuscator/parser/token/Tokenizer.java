package edu.hm.counterobfuscator.parser.token;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import edu.hm.counterobfuscator.helper.Validate;
import edu.hm.counterobfuscator.parser.matcher.IMatch;
import edu.hm.counterobfuscator.parser.matcher.TokenMatcher;
import edu.hm.counterobfuscator.types.Function;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 25.12.2014
 * 
 *       Class to transform a String input stream into a representation of
 *       tokens
 * 
 */
class Tokenizer {

	private static Logger	log;
	private String				input;
	private char[]				inputArray;
	private List<Token>		tokens;

	public Tokenizer(String input) {

		this.input = input;
		tokens = new ArrayList<Token>();

		Tokenizer.log = Logger.getLogger(Function.class.getName());
	}

	void process() {

		// TODO reduce cyclo complex actual is 5

		log.info("start tokinizing process...");
		
		Validate.notNull(tokens);

		String matches = "(|)|[|]|;|=|{|}|,|.";

		IMatch matcher = new TokenMatcher(matches);

		int beginPos = 0;

		String charBuffer = "";

		inputArray = input.toCharArray();

		for (int pos = 0, element = 0; pos < inputArray.length; pos++) {

			char actualChar = inputArray[pos];

			// TODO refactor!
			if (matcher.matchAll(actualChar + "") || actualChar == ' ') {

				if (charBuffer.length() > 0) {
					getTokens().add(
							new Token(element++, mapPosInInputArrayToTokentype(beginPos, pos - 1),
									charBuffer));
				}
				if (actualChar != ' ') { // ignore whitespaces
					getTokens()
							.add(new Token(element++, mapPosInInputArrayToTokentype(pos, pos), actualChar
									+ ""));
				}
				beginPos = pos + 1;
				charBuffer = ""; // reset buffer

			}
			else {
				charBuffer += actualChar;
			}

		}
		
		log.info("tokinizing process finished");
		
		int i=0;
		for(Token t: tokens) {
			
			System.out.println(i++ + "--" + t.getValue());
		}
		
	}

	List<Token> getTokens() {

		return tokens;
	}

	private TOKENTYPE mapPosInInputArrayToTokentype(int posStart, int posEnd) {

		String extractedString = "";
		int pos = posStart;

		while (pos <= posEnd) {
			extractedString += inputArray[pos++];
		}

		return mapStringToTokentype(extractedString);
	}

	private TOKENTYPE mapStringToTokentype(String object) {

		switch (object) {
		case "this":
			return TOKENTYPE.THIS;
		case "constructor":
			return TOKENTYPE.CONSTRUCTOR;
		case "new":
			return TOKENTYPE.NEW;
		case "function":
			return TOKENTYPE.FUNCTION;
		case "return":
			return TOKENTYPE.RETURN;
		case "var":
			return TOKENTYPE.VAR;
		case "for":
			return TOKENTYPE.FOR;
		case "while":
			return TOKENTYPE.WHILE;
		case "try":
			return TOKENTYPE.TRY;
		case "catch":
			return TOKENTYPE.CATCH;
		case "replace":
			return TOKENTYPE.REPLACE;
		case "slice":
			return TOKENTYPE.SCLICE;
		case "parseInt":
			return TOKENTYPE.PARSEINT;
		case "(":
			return TOKENTYPE.OPEN_BRACKET;
		case ")":
			return TOKENTYPE.CLOSE_BRACKET;
		case "[":
			return TOKENTYPE.OPEN_SQUARE_BRACKET;
		case "]":
			return TOKENTYPE.CLOSE_SQUARE_BRACKET;
		case "{":
			return TOKENTYPE.OPEN_CURLY_BRACKET;
		case "}":
			return TOKENTYPE.CLOSE_CURLY_BRACKET;
		case "/":
			return TOKENTYPE.SLASH;
		case "\\":
			return TOKENTYPE.BACKSLASH;
		case ";":
			return TOKENTYPE.SEMICOLON;
		case ",":
			return TOKENTYPE.COMMA;
		case ".":
			return TOKENTYPE.DOT;
		case "'":
			return TOKENTYPE.QUOTE;
		case "=":
			return TOKENTYPE.ASSIGN;
		case "+":
			return TOKENTYPE.PLUS;
		case "-":
			return TOKENTYPE.MINUS;
		case "<":
		case ">":
			return TOKENTYPE.EQUAL;
		case " ":
			return TOKENTYPE.WHITESPACE;
		default:
			return TOKENTYPE.STRING;
		}
	}
}
