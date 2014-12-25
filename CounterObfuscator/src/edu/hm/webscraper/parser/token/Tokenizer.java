package edu.hm.webscraper.parser.token;

import java.util.ArrayList;
import java.util.List;

import edu.hm.webscraper.helper.Validate;
import edu.hm.webscraper.parser.matcher.IMatch;
import edu.hm.webscraper.parser.matcher.TokenMatcher;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 25.12.2014
 * 
 *       Class to transform a String input stream into a representation of
 *       tokens
 * 
 */
final public class Tokenizer {

	private String input;
	private char[] inputArray;
	private List<Token> tokens;

	public Tokenizer(String input) {

		this.input = input;
		tokens = new ArrayList<Token>();

	}

	public void process() {

		// TODO reduce cyclo complex actual is 5

		Validate.notNull(tokens);

		String matches = "(|)|[|]|;|.|'|=|{|}|,|+|-|/|<|>";

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
							new Token(element++, mapPosInInputArrayToTokentype(
									beginPos, pos - 1), charBuffer));
				}
				if (actualChar != ' ') { // ignore whitespaces
					getTokens().add(
							new Token(element++, mapPosInInputArrayToTokentype(
									pos, pos), actualChar + ""));
				}
				beginPos = pos + 1;
				charBuffer = ""; // reset buffer

			} else {
				charBuffer += actualChar;
			}

		}

	}

	public List<Token> getTokens() {

		return tokens;
	}

	public List<Token> getAllTokensOfSameType(TOKENTYPE type) {

		Validate.isTrue(tokens.size() > 0);
		Validate.notNull(type);

		List<Token> allTokensOfType = new ArrayList<Token>();

		for (Token token : tokens) {

			if (token.getType() == type) {
				allTokensOfType.add(token);
			}
		}

		return allTokensOfType;
	}

	public List<Token> getAllTokensUntilType(int startPos, TOKENTYPE type) {

		Validate.isTrue(tokens.size() > 0);
		Validate.isTrue(startPos > 0 && startPos < tokens.size());
		Validate.notNull(type);

		List<Token> allTokensUntilType = new ArrayList<Token>();

		for (int i = startPos; i < tokens.size(); i++) {

			Token actualToken = tokens.get(i);

			if (actualToken.getType() == type) {
				break;
			} else {
				allTokensUntilType.add(actualToken);
			}
		}

		return allTokensUntilType;
	}

	public List<Token> getAllTokensUntilEndPos(int startPos, int endPos) {

		Validate.isTrue(tokens.size() > 0);
		Validate.isTrue(startPos <= endPos);

		List<Token> allTokensUntilEndPos = new ArrayList<Token>();

		for (int i = startPos; i < endPos; i++) {

			allTokensUntilEndPos.add(tokens.get(i));

		}

		return allTokensUntilEndPos;
	}

	public int getPositionOfNextToken(int startPos, TOKENTYPE type) {

		Validate.isTrue(tokens.size() > 0);
		Validate.isTrue(startPos > -1);
		Validate.notNull(type);

		for (int i = startPos; i < tokens.size(); i++) {

			if (tokens.get(i).getType() == type) {
				return tokens.get(i).getPos();
			}
		}

		return -1;
	}

	public String getStringOfTokens(List<Token> tokensToString) {

		Validate.isTrue(tokens.size() > 0);
		Validate.notNull(tokensToString);

		String valueOfTokens = "";

		for (Token actualToken : tokensToString) {
			valueOfTokens += actualToken.getValue();
		}

		return valueOfTokens;
	}

	public boolean isTokenWithinStartAndEndPos(int startPos, int endPos,
			TOKENTYPE type) {

		Validate.isTrue(tokens.size() > 0);
		Validate.isTrue(startPos <= endPos);
		Validate.notNull(type);

		for (int i = startPos; i < endPos; i++) {

			if (tokens.get(i).getType() == type) {
				return true;
			}
		}

		return false;
	}

	public List<Token> getAllTokensBetweenBrackets(int startPos,
			TOKENTYPE startToken, TOKENTYPE endToken) {

		Validate.isTrue(tokens.get(startPos).getType() == startToken);

		List<Token> allTokensBetweenTwoBrackets = new ArrayList<Token>();

		int openBracketsCounter = 0;

		for (int i = startPos; i < tokens.size(); i++) {

			Token actualToken = tokens.get(i);

			allTokensBetweenTwoBrackets.add(actualToken);

			if (actualToken.getType() == startToken) {
				openBracketsCounter++;
			} else if (actualToken.getType() == endToken) {
				openBracketsCounter--;
			}

			if (openBracketsCounter == 0) {
				break;
			}
		}

		return allTokensBetweenTwoBrackets;
	}

	public List<Integer> getAllPosOfTokensByValue(String value) {

		List<Integer> posOfTokens = new ArrayList<Integer>();

		for (Token actualToken : tokens) {

			if (actualToken.getValue().equals(value)) {
				posOfTokens.add(actualToken.getPos());
			}
		}

		return posOfTokens;
	}

	public boolean getTokenIsInBrackets(Token tokenToTest) {

		Validate.isTrue(tokens.size() > 0);
		Validate.notNull(tokenToTest);

		int openCurlyBrackets = 0;
		int openBrackets = 0;
		int openSquareBrackets = 0;

		for (int i = 0; i < tokenToTest.getPos(); i++) {

			TOKENTYPE tokenType = tokens.get(i).getType();

			if (tokenType == TOKENTYPE.OPEN_BRACKET) {
				openBrackets++;
			} else if (tokenType == TOKENTYPE.CLOSE_BRACKET) {
				openBrackets--;
			} else if (tokenType == TOKENTYPE.OPEN_CURLY_BRACKET) {
				openCurlyBrackets++;
			} else if (tokenType == TOKENTYPE.CLOSE_CURLY_BRACKET) {
				openCurlyBrackets--;
			} else if (tokenType == TOKENTYPE.OPEN_SQUARE_BRACKET) {
				openSquareBrackets++;
			} else if (tokenType == TOKENTYPE.CLOSE_SQUARE_BRACKET) {
				openSquareBrackets--;
			}
		}

		if (openBrackets > 0 || openCurlyBrackets > 0 || openSquareBrackets > 0) {
			return true;
		} else {
			return false;
		}
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
		case ";":
			return TOKENTYPE.SEMIKOLON;
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
