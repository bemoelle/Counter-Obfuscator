package edu.hm.counterobfuscator.parser.token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import edu.hm.counterobfuscator.helper.Validate;
import edu.hm.counterobfuscator.types.AbstractType;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.types.IType;
import edu.hm.counterobfuscator.types.Loop;
import edu.hm.counterobfuscator.types.Variable;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 25.12.2014
 * 
 *       after tokenising it is necessary analyze each token, to find JavaScript
 *       keywords like VAR, FUNCTION, etc.
 * 
 *       class is package private
 * 
 */
class TokenAnalyser implements ITokenAnalyser {

	private static Logger		log;
	private List<Token>			allTokensOfJSCode;
	private List<AbstractType>	vars;
	private List<AbstractType>	functions;
	private List<AbstractType>	loops;
	private List<AbstractType>	tryCatch;

	public TokenAnalyser(Tokenizer tokenizer) {

		Validate.notNull(tokenizer);

		TokenAnalyser.log = Logger.getLogger(Function.class.getName());

		this.allTokensOfJSCode = tokenizer.getTokens();

		vars = new ArrayList<AbstractType>();
		functions = new ArrayList<AbstractType>();
		loops = new ArrayList<AbstractType>();
		tryCatch = new ArrayList<AbstractType>();

	}

	/**
	 * 
	 */
	protected void process() {

		processLocalVariables();
		processGlobalVariables();
		processFunctions();
		processLoops();
		processTryCatchStatements();
	}

	/**
	 * 
	 */
	private void processLocalVariables() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(vars);

		log.info("start processLocalVariables analyse process...");

		// find all local vars with the KEYWORD VAR,
		// var test=100;
		// var test,test1,test2;
		// var test=100,test2=102;
		// test=100;
		// test=100, test2=102;
		// this.test3; --> not implemented yet!
		// xxx,xxx2; --> wrong JavaScript Code

		List<Token> tokens = getAllTokensOfSameType(TOKENTYPE.VAR);

		for (Token actualToken : tokens) {

			// ignore VAR statement at the beginning
			int startPos = actualToken.getPos() + 1;

			int nextSemicolon = getPositionOfNextToken(startPos, TOKENTYPE.SEMICOLON);
			int nextComma = getPositionOfNextToken(startPos, TOKENTYPE.COMMA);

			if (nextComma < 0 || nextComma > nextSemicolon) { // var test=100;

				int nextAssign = getPositionOfNextToken(startPos, TOKENTYPE.ASSIGN);

				if (nextAssign > 0 && nextAssign < nextSemicolon) {
					String name = getNameOfType(startPos, nextAssign - 1);
					String body = getNameOfType(nextAssign + 1, nextSemicolon - 1);
					vars.add(new Variable(startPos, nextSemicolon - 1, name, body));

				}
				else {
					vars.add(new Variable(startPos, nextSemicolon - 1, getNameOfType(startPos,
							nextSemicolon - 1), getNameOfType(nextAssign, nextSemicolon - 1)));
				}
				continue;
			}
			else { // var test=100,test2=102;

				int actualPos = startPos;

				// next comma exist and position is before next semicolon
				while (nextComma > 0 && nextComma < nextSemicolon) {

					int nextAssign = getPositionOfNextToken(actualPos, TOKENTYPE.ASSIGN);

					if (nextAssign > 0 && nextAssign < nextSemicolon) {
						String name = getNameOfType(actualPos, nextAssign - 1);
						String body = getNameOfType(nextAssign + 1, nextComma - 1);
						vars.add(new Variable(actualPos, nextComma - 1, name, body));
					}
					else {
						String name = getNameOfType(actualPos, nextComma - 1);
						String body = "undefined";
						vars.add(new Variable(actualPos, nextComma - 1, name, body));
					}

					actualPos = nextComma + 1;

					nextComma = getPositionOfNextToken(actualPos, TOKENTYPE.COMMA);
					nextSemicolon = getPositionOfNextToken(actualPos, TOKENTYPE.SEMICOLON);
				}

				int nextAssign = getPositionOfNextToken(actualPos, TOKENTYPE.ASSIGN);

				if (nextAssign > 0 && nextAssign < nextSemicolon) {
					String name = getNameOfType(actualPos, nextAssign - 1);
					String body = getNameOfType(nextAssign + 1, nextSemicolon - 1);
					vars.add(new Variable(actualPos, nextSemicolon - 1, name, body));
				}
				else {
					String name = getNameOfType(actualPos, nextSemicolon - 1);
					String body = "undefined";
					vars.add(new Variable(actualPos, nextSemicolon - 1, name, body));
				}
				continue;
			}
		}
	}

	/**
	 * 
	 */
	private void processGlobalVariables() {
		//
		// Validate.isTrue(allTokensOfJSCode.size() > 0);
		// Validate.notNull(vars);

		log.info("start processGlobalVariables analyse process...");
		//
		// List<Token> tokens = getAllTokensOfSameType(TOKENTYPE.ASSIGN);
		//
		// for (Token token : tokens) {
		//
		// int pos = token.getPos();
		// if (pos > 1 && allTokensOfJSCode.get(pos - 1).getType() ==
		// TOKENTYPE.STRING
		// && allTokensOfJSCode.get(pos - 2).getType() != TOKENTYPE.VAR) {
		// String name = allTokensOfJSCode.get(pos - 1).getValue();
		//
		// String value = getStringOfTokens(getAllTokensUntilType(pos + 1,
		// TOKENTYPE.SEMICOLON));
		//
		// // TODO check if global is local var :)
		// // vars.add(new Variable(pos - 1, name, value));
		// }
		// }
	}

	/**
	 * 
	 */
	private void processFunctions() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(functions);

		log.info("start processFunctions analyse process...");

		List<Token> tokens = getAllTokensOfSameType(TOKENTYPE.FUNCTION);

		for (Token actualToken : tokens) {

			// ignore FUNCTION statement at the beginning
			int startPos = actualToken.getPos() + 1;

			int nextOpenBracket = getPositionOfNextToken(startPos, TOKENTYPE.OPEN_BRACKET);
			int nextClosedBracket = getPositionOfNextToken(nextOpenBracket, TOKENTYPE.CLOSE_BRACKET);
			int nextCurlyOpenBracket = getPositionOfNextToken(nextClosedBracket,
					TOKENTYPE.OPEN_CURLY_BRACKET);
			int nextCurlyClosedBracket = getPositionOfNextToken(nextCurlyOpenBracket,
					TOKENTYPE.CLOSE_CURLY_BRACKET);

			String name = getNameOfType(startPos, nextOpenBracket - 1);
			String head = getNameOfType(nextOpenBracket, nextClosedBracket);
			String body = getNameOfType(nextCurlyOpenBracket, nextCurlyClosedBracket);

			functions.add(new Function(startPos, nextCurlyClosedBracket, name, head, body));

		}

	}

	/**
	 * 
	 */
	private void processLoops() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(loops);

		log.info("start processLoops analyse process...");

		// add all tokens for FOR and while loops
		List<Token> tokens = getAllTokensOfSameType(TOKENTYPE.FOR);
		tokens.addAll(getAllTokensOfSameType(TOKENTYPE.WHILE));

		for (Token actualToken : tokens) {

			// ignore FUNCTION statement at the beginning
			int startPos = actualToken.getPos() + 1;

			int nextOpenBracket = getPositionOfNextToken(startPos, TOKENTYPE.OPEN_BRACKET);
			int nextClosedBracket = getPositionOfNextToken(nextOpenBracket, TOKENTYPE.CLOSE_BRACKET);
			int nextCurlyOpenBracket = getPositionOfNextToken(nextClosedBracket,
					TOKENTYPE.OPEN_CURLY_BRACKET);
			int nextCurlyClosedBracket = getPositionOfNextToken(nextCurlyOpenBracket,
					TOKENTYPE.CLOSE_CURLY_BRACKET);

			String name = getNameOfType(startPos, nextOpenBracket - 1);
			String head = getNameOfType(nextOpenBracket, nextClosedBracket);
			String body = getNameOfType(nextCurlyOpenBracket, nextCurlyClosedBracket);

			loops.add(new Loop(startPos, nextCurlyClosedBracket, name, head, body));

		}
	}

	/**
	 * 
	 */
	private void processTryCatchStatements() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(tryCatch);

		log.info("start processTryCatchStatements analyse process...");

		// TODO ...

	}

	/**
	 * @return all tokens
	 */
	public List<Token> getTokens() {
		return allTokensOfJSCode;
	}

	/**
	 * @return vars
	 */
	public List<AbstractType> getTypesOfTokenTypes(TOKENTYPE type) {

		// TODO enhance for other types
		Validate.notNull(type);

		if (type == TOKENTYPE.VAR) {
			Validate.notNull(vars);
			return vars;
		}
		else if (type == TOKENTYPE.FUNCTION) {
			Validate.notNull(functions);
			return functions;
		}
		else {
			return null;
		}
	}

	/**
	 * @param value
	 * @return a List of position in tokenlist, which have the same value
	 */
	public List<Integer> getAllPosOfTokensByValue(String value) {

		Validate.notNull(value);

		List<Integer> posOfTokens = new ArrayList<Integer>();

		for (Token actualToken : allTokensOfJSCode) {

			if (actualToken.getValue().equals(value)) {
				posOfTokens.add(actualToken.getPos());
			}
		}

		return posOfTokens;
	}

	/**
	 * @param type
	 * @return String name of type
	 */
	public String getNameOfType(int startPos, int endPos) {

		// if ((endPos - startPos) == 0) {

		if (startPos > endPos) {
			return "";
		}

		return getStringOfTokens(getAllTokensUntilEndPos(startPos, endPos));
		// }
		// else {
		// int posOfAssign = getPositionOfNextToken(startPos, TOKENTYPE.ASSIGN) -
		// 1;
		// return getStringOfTokens(getAllTokensUntilEndPos(startPos,
		// posOfAssign));
		// }
	}

	/**
	 * @param type
	 * @return
	 */
	public List<Token> getAllTokensOfSameType(TOKENTYPE type) {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(type);

		List<Token> allTokensOfType = new ArrayList<Token>();

		for (Token token : allTokensOfJSCode) {

			if (token.getType() == type) {
				allTokensOfType.add(token);
			}
		}

		return allTokensOfType;
	}

	/**
	 * @param startPos
	 * @param type
	 * @return
	 */
	public List<Token> getAllTokensUntilType(int startPos, TOKENTYPE type) {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.isTrue(startPos > 0 && startPos < allTokensOfJSCode.size());
		Validate.notNull(type);

		List<Token> allTokensUntilType = new ArrayList<Token>();

		for (int i = startPos; i < allTokensOfJSCode.size(); i++) {

			Token actualToken = allTokensOfJSCode.get(i);

			allTokensUntilType.add(actualToken);

			if (actualToken.getType() == type) {
				break;
			}
		}

		return allTokensUntilType;
	}

	/**
	 * @param startPos
	 * @param endPos
	 * @return al list of tokens until a endPos from a startPos
	 */
	public List<Token> getAllTokensUntilEndPos(int startPos, int endPos) {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.isTrue(startPos <= endPos);
		Validate.isTrue(endPos < allTokensOfJSCode.size());

		List<Token> allTokensUntilEndPos = new ArrayList<Token>();

		for (int i = startPos; i <= endPos; i++) {

			allTokensUntilEndPos.add(allTokensOfJSCode.get(i));

		}

		return allTokensUntilEndPos;
	}

	/**
	 * @param startPos
	 * @param type
	 * @param ignoreIsInBrackets
	 * @return position of next tokentype at a given startPos, return -1 if
	 *         tokentype doesn't exist
	 */
	public int getPositionOfNextToken(int startPos, TOKENTYPE type) {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.isTrue(startPos > -1);
		Validate.notNull(type);

		for (int i = startPos; i < allTokensOfJSCode.size(); i++) {

			// check that token has correct Type and is not within brackets like:
			// no: var AAAA='krkeIplIaMcMIe'.replace(/[BBBB]/g,'');
			// yes: var AAAA,BBBB;
			if (allTokensOfJSCode.get(i).getType() == type
					&& (!isTokenWithinBrackets(startPos + 1, allTokensOfJSCode.get(i)))) {
				return allTokensOfJSCode.get(i).getPos();
			}
		}

		return -1;
	}

	/**
	 * @param tokensToString
	 * @return create a string from a given list of tokens
	 */
	public String getStringOfTokens(List<Token> tokensToString) {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(tokensToString);

		String valueOfTokens = "";

		for (Token actualToken : tokensToString) {
			valueOfTokens += actualToken.getValue();
		}

		return valueOfTokens;
	}

	/**
	 * @param startPos
	 * @param endPos
	 * @param type
	 * @return true if a tokentype is in a given startPos and endPos
	 */
	public boolean isTokenWithinStartAndEndPos(int startPos, int endPos, TOKENTYPE type) {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.isTrue(startPos <= endPos);
		Validate.notNull(type);

		for (int i = startPos; i < endPos; i++) {

			if (allTokensOfJSCode.get(i).getType() == type) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param startPos
	 * @param startToken
	 * @param endToken
	 * @return a list of tokens at a given startpos and starttoken type to a
	 *         endtoken type
	 */
	public List<Token> getAllTokensBetweenBrackets(int startPos, TOKENTYPE startToken,
			TOKENTYPE endToken) {

		Validate.isTrue(allTokensOfJSCode.get(startPos).getType() == startToken);

		List<Token> allTokensBetweenTwoBrackets = new ArrayList<Token>();

		int openBracketsCounter = 0;

		for (int i = startPos; i < allTokensOfJSCode.size(); i++) {

			Token actualToken = allTokensOfJSCode.get(i);

			allTokensBetweenTwoBrackets.add(actualToken);

			if (actualToken.getType() == startToken) {
				openBracketsCounter++;
			}
			else if (actualToken.getType() == endToken) {
				openBracketsCounter--;
			}

			if (openBracketsCounter == 0) {
				break;
			}
		}

		return allTokensBetweenTwoBrackets;
	}

	/**
	 * @param tokenToTest
	 * @return true if Token is within two Brackets, otherwise false
	 * 
	 *         startPos because some token are in brackets and we like to ignore
	 *         that, e.g. var test = (function (a,b) {return a+b;}); ignore first
	 *         and last round bracket
	 */
	public boolean isTokenWithinBrackets(int startPos, Token tokenToTest) {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(tokenToTest);

		int openCurlyBrackets = 0;
		int openBrackets = 0;
		int openSquareBrackets = 0;

		for (int i = startPos; i < tokenToTest.getPos(); i++) {

			TOKENTYPE tokenType = allTokensOfJSCode.get(i).getType();

			if (tokenType == TOKENTYPE.OPEN_BRACKET) {
				openBrackets++;
			}
			else if (tokenType == TOKENTYPE.CLOSE_BRACKET) {
				openBrackets--;
			}
			else if (tokenType == TOKENTYPE.OPEN_CURLY_BRACKET) {
				openCurlyBrackets++;
			}
			else if (tokenType == TOKENTYPE.CLOSE_CURLY_BRACKET) {
				openCurlyBrackets--;
			}
			else if (tokenType == TOKENTYPE.OPEN_SQUARE_BRACKET) {
				openSquareBrackets++;
			}
			else if (tokenType == TOKENTYPE.CLOSE_SQUARE_BRACKET) {
				openSquareBrackets--;
			}
		}

		if (openBrackets > 0 || openCurlyBrackets > 0 || openSquareBrackets > 0) {
			return true;
		}
		else {
			return false;
		}
	}
}
