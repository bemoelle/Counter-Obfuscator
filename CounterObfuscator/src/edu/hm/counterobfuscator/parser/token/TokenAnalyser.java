package edu.hm.counterobfuscator.parser.token;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.codec.EncoderException;
import org.jboss.netty.handler.codec.http.websocketx.PongWebSocketFrame;

import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.helper.Validate;
import edu.hm.counterobfuscator.types.AbstractType;
import edu.hm.counterobfuscator.types.Default;
import edu.hm.counterobfuscator.types.ForWhile;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.types.FunctionCall;
import edu.hm.counterobfuscator.types.Return;
import edu.hm.counterobfuscator.types.This;
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
// TODO clean!
class TokenAnalyser implements ITokenAnalyser {

	private static Logger log;
	private List<Token> allTokensOfJSCode;
	private Token actualToken;
	private List<AbstractType> allTypes;

	public TokenAnalyser(Tokenizer tokenizer) {

		Validate.notNull(tokenizer);

		TokenAnalyser.log = Logger.getLogger(Function.class.getName());

		this.allTokensOfJSCode = tokenizer.getTokens();

		allTypes = new ArrayList<AbstractType>();

		actualToken = allTokensOfJSCode.get(0);

	}

	/**
	 * @throws EncoderException
	 * @throws Exception
	 * 
	 */
	protected void process() throws IllegalArgumentException, EncoderException {

		while (hasNextToken()) {

			call(actualToken.getType());

			setToNextToken();
		}

	}

	public List<AbstractType> getAllTypes() {
		return allTypes;
	}

	private Token getActualToken() {

		return actualToken;
	}

	private void setNextTokenTo(int pos) {

		actualToken = allTokensOfJSCode.get(pos);
	}

	private void setToNextToken() {

		if (actualToken.getPos() == allTokensOfJSCode.size() - 1) {
			return;
		}

		actualToken = allTokensOfJSCode.get(actualToken.getPos() + 1);
	}

	private Token getNextTokenOf(Token token) {

		return allTokensOfJSCode.get(token.getPos() + 1);
	}

	private boolean hasNextToken() {

		if (actualToken.getPos() == allTokensOfJSCode.size() - 1) {
			return false;
		}

		return true;
	}

	private void call(TOKENTYPE type) throws IllegalArgumentException,
			EncoderException {

		switch (type) {
		case VAR:
			processVar();
			break;
		case FUNCTION:
			processFunction();
			break;
		case WHILE:
			processWhile();
			break;
		case FOR:
			processFor();
			break;
		case THIS:
			processThis();
			break;
		case RETURN:
			processReturn();
			break;
		default:
			processDefault();
			break;

		}
	}

	/**
	 * 
	 */
	private void processDefault() {

		int startPos = getActualToken().getPos();
		int endPos = getPositionOfNextToken(startPos, TOKENTYPE.SEMICOLON);

		Token nextToken = getNextTokenOf(getActualToken());

		switch (nextToken.getType()) {
		case DOT:
			// Name.Func(Parameter); isObject = true;
			System.out.println("FunctionCall");
			int dot = nextToken.getPos();

			int openBracket = getPositionOfNextToken(startPos,
					TOKENTYPE.OPEN_BRACKET);

			String nameFC = getNameOfType(startPos, dot - 1);
			String functionFC = getNameOfType(dot + 1, openBracket - 1);
			// -2 because ignore last )
			String valueFC = getNameOfType(openBracket + 1, endPos - 2);

			allTypes.add(new FunctionCall(new Position(startPos, endPos),
					nameFC, functionFC, valueFC));

			break;
		case ASSIGN:

			// TODO
			// ++ --
			// test -= test;
			// test += test;

			// Variable(Position pos, String name, String value, boolean
			// isObject)
			// var test = new Name(Parameter); wird in processVar() behandelt
			// TODO test2 = new Name(Parameter);

			Variable var = null;

			int assign = getActualToken().getPos();

			String name = getNameOfType(startPos, assign - 1);
			String value = getNameOfType(assign + 1, endPos - 1);

			var = new Variable(new Position(startPos, endPos), name, value,
					false);

			if (!allTypes.contains(var)) {
				var.setGlobal(true);
			}

			allTypes.add(var);

			break;
		default:
		}
		setNextTokenTo(endPos);

	}

	private void processReturn() {

		int startPos = getActualToken().getPos();
		int endPos = getPositionOfNextToken(startPos, TOKENTYPE.SEMICOLON);

		String name = getNameOfType(startPos + 1, endPos - 1);

		allTypes.add(new Return(new Position(startPos, endPos), name));

		setNextTokenTo(endPos);

	}

	// private void processGlobalVar() {
	//
	// int assign = getActualToken().getPos();
	// int startPos = getPositionOfPreviousToken(assign - 1, TOKENTYPE.STRING);
	// int endPos = getPositionOfNextToken(startPos, TOKENTYPE.SEMICOLON);
	//
	// String name = getNameOfType(startPos, assign - 1);
	// String value = getNameOfType(assign + 1, endPos - 1);
	//
	// Variable var = new Variable(new Position(startPos, endPos), name, value);
	//
	// if (!allTypes.contains(var)) {
	// var.setGlobal(true);
	// }
	//
	// allTypes.add(var);
	//
	// }

	/**
	 * @return
	 * @throws EncoderException
	 */
	private void processVar() throws EncoderException {

		//functioncall wird noch nicht behandelt 
		//var test = obj.Func(Parameter) -> processDefault();
		
		int startPos = getActualToken().getPos();

		int assign = getPositionOfNextToken(startPos, TOKENTYPE.ASSIGN);
		int endPos = getPositionOfNextToken(startPos, TOKENTYPE.SEMICOLON);

		String name = getNameOfType(startPos + 1, assign - 1);
		String value = null;
		boolean isObject = false;

		if (isTokenWithinStartAndEndPos(assign, endPos, TOKENTYPE.NEW)) {
			isObject = true;

		}

		value = getNameOfType(assign + 1, endPos - 1);
		allTypes.add(new Variable(new Position(startPos, endPos), name, value,
				isObject));

		setNextTokenTo(endPos);

		// while (hasNextToken() && actualToken.getType() !=
		// TOKENTYPE.SEMICOLON)
		// {
		//
		// getNextToken();
		// if (actualToken.getType() == TOKENTYPE.STRING) {
		// name = actualToken.getValue();
		// }
		// else if (actualToken.getType() == TOKENTYPE.COMMA) {
		// declaration.add(new VariableDecTree(actualPos, name, value));
		// name = "";
		// value = "";
		// }
		// else if (actualToken.getType() == TOKENTYPE.ASSIGN) {
		// int comma = getPositionOfNextToken(actualToken.getPos(),
		// TOKENTYPE.COMMA);
		// int semicolon = getPositionOfNextToken(actualToken.getPos(),
		// TOKENTYPE.SEMICOLON);
		//
		// if (comma > 0 && comma < semicolon) {
		// value =
		// getStringOfTokens(getAllTokensUntilEndPos(actualToken.getPos()
		// + 1, comma));
		// actualToken = allTokensOfJSCode.get(comma - 1);
		// }
		// else {
		// value =
		// getStringOfTokens(getAllTokensUntilEndPos(actualToken.getPos()
		// + 1,
		// semicolon));
		// actualToken = allTokensOfJSCode.get(semicolon - 1);
		// }
		//
		// }
		// else if (actualToken.getType() == TOKENTYPE.SEMICOLON) {
		// declaration.add(new VariableDecTree(actualPos, name, value));
		// name = "";
		// value = "";
		// }
		//
		// }

	}

	private void processFunction() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(allTypes);

		log.info("start processFunction analyse process...");

		int startPos = getActualToken().getPos();

		int nextOpenBracket = getPositionOfNextToken(startPos,
				TOKENTYPE.OPEN_BRACKET);
		int nextClosedBracket = getPositionOfNextToken(nextOpenBracket,
				TOKENTYPE.CLOSE_BRACKET);
		int nextCurlyOpenBracket = getPositionOfNextToken(nextClosedBracket,
				TOKENTYPE.OPEN_CURLY_BRACKET);
		int endPos = getPositionOfNextToken(nextCurlyOpenBracket,
				TOKENTYPE.CLOSE_CURLY_BRACKET);

		String name = getNameOfType(startPos + 1, nextOpenBracket - 1);
		String head = getNameOfType(nextOpenBracket, nextClosedBracket);
		String body = getNameOfType(nextCurlyOpenBracket, endPos);

		boolean isPacked = false;
		// TODO same as function.call()
		if (startPos > 0
				&& allTokensOfJSCode.get(startPos - 1).getType() == TOKENTYPE.OPEN_BRACKET) {
			startPos--;
			endPos = getPositionOfNextToken(endPos + 2, TOKENTYPE.CLOSE_BRACKET);
			isPacked = true;
		}

		allTypes.add(new Function(new Position(startPos, endPos), name, head, body, isPacked));

		setNextTokenTo(nextCurlyOpenBracket);

	}

	private void processThis() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(allTypes);

		log.info("start processThis analyse process...");

		int startPos = getActualToken().getPos();

		int assign = getPositionOfNextToken(startPos, TOKENTYPE.ASSIGN);
		int endPos = getPositionOfNextToken(startPos, TOKENTYPE.SEMICOLON);

		String name = getNameOfType(startPos + 1, assign - 1);
		String value = getNameOfType(assign + 1, endPos - 1);

		allTypes.add(new This(new Position(startPos, endPos), name, value));

		setNextTokenTo(endPos);
	}

	/**
	 * 
	 */
	private void processWhile() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		// Validate.notNull(loops);

		log.info("start processLoops analyse process...");

	}

	/**
	 * 
	 */
	private void processFor() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(allTypes);

		log.info("start processFor analyse process...");

		int startPos = getActualToken().getPos();

		int nextOpenBracket = getPositionOfNextToken(startPos,
				TOKENTYPE.OPEN_BRACKET);
		int nextClosedBracket = getPositionOfNextToken(nextOpenBracket,
				TOKENTYPE.CLOSE_BRACKET);
		int nextCurlyOpenBracket = getPositionOfNextToken(nextClosedBracket,
				TOKENTYPE.OPEN_CURLY_BRACKET);
		int endPos = getPositionOfNextToken(nextCurlyOpenBracket,
				TOKENTYPE.CLOSE_CURLY_BRACKET);

		String head = getNameOfType(nextOpenBracket, nextClosedBracket);
		String body = getNameOfType(nextCurlyOpenBracket, endPos);

		allTypes.add(new ForWhile(new Position(startPos, endPos), "for", head, body));

		setNextTokenTo(nextCurlyOpenBracket);
	}

	/**
	 * @return all tokens
	 */
	public List<Token> getTokens() {
		return allTokensOfJSCode;
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

		if (startPos > endPos) {
			return "";
		}

		return getStringOfTokens(getAllTokensUntilEndPos(startPos, endPos));
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
	public int getPositionOfNextToken(int startPos, TOKENTYPE... type) {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.isTrue(startPos > -1);
		Validate.notNull(type);

		for (int i = startPos; i < allTokensOfJSCode.size(); i++) {

			// check that token has correct Type and is not within brackets
			// like:
			// no: var AAAA='krkeIplIaMcMIe'.replace(/[BBBB]/g,'');
			// yes: var AAAA,BBBB;
			if (isIn(allTokensOfJSCode.get(i).getType(), type)
					&& (!isTokenWithinBrackets(startPos + 1,
							allTokensOfJSCode.get(i)))) {
				return allTokensOfJSCode.get(i).getPos();
			}
		}

		return 0;
	}

	public boolean isIn(TOKENTYPE type, TOKENTYPE... tokentypes) {

		for (TOKENTYPE xxx : tokentypes) {

			if (xxx == type)
				return true;
		}

		return false;

	}

	public int getPositionOfPreviousToken(int startPos, TOKENTYPE type) {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.isTrue(startPos > -1);
		Validate.notNull(type);

		for (int i = startPos; i > -1; i--) {

			if (allTokensOfJSCode.get(i).getType() == type) {
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

			if (actualToken.hasWhitespace()) {
				valueOfTokens += " ";
			}
		}

		return valueOfTokens;
	}

	/**
	 * @param startPos
	 * @param endPos
	 * @param type
	 * @return true if a tokentype is in a given startPos and endPos
	 */
	public boolean isTokenWithinStartAndEndPos(int startPos, int endPos,
			TOKENTYPE type) {

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
	public List<Token> getAllTokensBetweenBrackets(int startPos,
			TOKENTYPE startToken, TOKENTYPE endToken) {

		Validate.isTrue(allTokensOfJSCode.get(startPos).getType() == startToken);

		List<Token> allTokensBetweenTwoBrackets = new ArrayList<Token>();

		int openBracketsCounter = 0;

		for (int i = startPos; i < allTokensOfJSCode.size(); i++) {

			Token actualToken = allTokensOfJSCode.get(i);

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

	/**
	 * @param tokenToTest
	 * @return true if Token is within two Brackets, otherwise false
	 * 
	 *         startPos because some token are in brackets and we like to ignore
	 *         that, e.g. var test = (function (a,b) {return a+b;}); ignore
	 *         first and last round bracket
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.counterobfuscator.parser.token.ITokenAnalyser#getTypesOfTokenTypes
	 * (edu.hm.counterobfuscator.parser.token.TOKENTYPE)
	 */
	@Override
	public List<AbstractType> getTypesOfTokenTypes(TOKENTYPE type) {
		// TODO Auto-generated method stub
		return null;
	}

}
