package edu.hm.counterobfuscator.parser.token;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.helper.Validate;
import edu.hm.counterobfuscator.parser.token.trees.TypeTreeElement;
import edu.hm.counterobfuscator.types.AbstractType;
import edu.hm.counterobfuscator.types.Function;
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

	private static Logger					log;
	private List<Token>						allTokensOfJSCode;
	private Token								actualToken;
	private List<AbstractType>				allTypes;
	private LinkedList<TypeTreeElement>	programmTree;

	public TokenAnalyser(Tokenizer tokenizer) {

		Validate.notNull(tokenizer);

		TokenAnalyser.log = Logger.getLogger(Function.class.getName());

		this.allTokensOfJSCode = tokenizer.getTokens();

		allTypes = new ArrayList<AbstractType>();

		actualToken = allTokensOfJSCode.get(0);

	}

	public List<AbstractType> getAllTypes() {
		return allTypes;
	}

	private Token getActualToken() {

		return actualToken;
	}

	private void getNextToken() {

		if (actualToken.getPos() == allTokensOfJSCode.size() - 1) {
			return;
		}

		actualToken = allTokensOfJSCode.get(actualToken.getPos() + 1);
	}

	private boolean hasNextToken() {

		if (actualToken.getPos() == allTokensOfJSCode.size() - 1) {
			return false;
		}

		return true;
	}

	private void call(TOKENTYPE type) throws IllegalArgumentException {

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
		default:
		}
	}

	/**
	 * @throws Exception
	 * 
	 */
	protected void process() throws IllegalArgumentException {

		while (hasNextToken()) {

			call(actualToken.getType());

			getNextToken();
		}

		createTypeTree();

		for (TypeTreeElement e : programmTree) {

			System.out.println(e.getType().getType() + " : " + e.getType().getPos().getStartPos()
					+ "-" + e.getType().getPos().getEndPos());
			for (TypeTreeElement t : e.getChildren()) {

				System.out.println("|_____" + t.getType().getType() + " : "
						+ t.getType().getPos().getStartPos() + "-" + t.getType().getPos().getEndPos());
			}
		}

	}

	/**
	 * 
	 */
	private void createTypeTree() {
		
		int number = 1;
		int highestEndPos = -1;
		TypeTreeElement parent = null;
		programmTree = new LinkedList<TypeTreeElement>();
		for (AbstractType actualType : allTypes) {

			int startPos = actualType.getPos().getStartPos();
			int endPos = actualType.getPos().getEndPos();

			TypeTreeElement actualElement = new TypeTreeElement(number++, actualType);

			if (startPos > highestEndPos) {

				if (parent != null)
					programmTree.add(parent);

				parent = actualElement;
				highestEndPos = endPos;

			}
			else {
				parent.addChild(actualElement);
			}

		}

		programmTree.add(parent);

	}

	/**
	 * @return
	 */
	private void processVar() {

		int startPos = getActualToken().getPos();

		int assign = getPositionOfNextToken(startPos, TOKENTYPE.ASSIGN);
		int endPos = getPositionOfNextToken(startPos, TOKENTYPE.SEMICOLON);

		String name = getNameOfType(startPos + 1, assign - 1);
		String value = getNameOfType(assign + 1, endPos - 1);

		allTypes.add(new Variable(new Position(startPos, endPos), name, value));

		// while (hasNextToken() && actualToken.getType() != TOKENTYPE.SEMICOLON)
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
		// value = getStringOfTokens(getAllTokensUntilEndPos(actualToken.getPos()
		// + 1, comma));
		// actualToken = allTokensOfJSCode.get(comma - 1);
		// }
		// else {
		// value = getStringOfTokens(getAllTokensUntilEndPos(actualToken.getPos()
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

		int nextOpenBracket = getPositionOfNextToken(startPos, TOKENTYPE.OPEN_BRACKET);
		int nextClosedBracket = getPositionOfNextToken(nextOpenBracket, TOKENTYPE.CLOSE_BRACKET);
		int nextCurlyOpenBracket = getPositionOfNextToken(nextClosedBracket,
				TOKENTYPE.OPEN_CURLY_BRACKET);
		int endPos = getPositionOfNextToken(nextCurlyOpenBracket, TOKENTYPE.CLOSE_CURLY_BRACKET);

		String name = getNameOfType(startPos + 1, nextOpenBracket - 1);
		String head = getNameOfType(nextOpenBracket, nextClosedBracket);
		List<Token> body = getAllTokensUntilEndPos(nextCurlyOpenBracket, endPos);

		boolean isPacked = false;
		// TODO same as function.call()
		if (startPos > 0 && allTokensOfJSCode.get(startPos - 1).getType() == TOKENTYPE.OPEN_BRACKET) {
			startPos--;
			endPos = getPositionOfNextToken(endPos + 2, TOKENTYPE.CLOSE_BRACKET);
			isPacked = true;
		}

		allTypes.add(new Function(new Position(startPos, endPos), name, head, isPacked, body));

	}

	/**
	 * 
	 */
	private void processLocalVariables() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		// Validate.notNull(vars);

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

			// // ignore VAR statement at the beginning
			// int startPos = actualToken.getPos() + 1;
			//
			// int nextSemicolon = getPositionOfNextToken(startPos,
			// TOKENTYPE.SEMICOLON);
			// int nextComma = getPositionOfNextToken(startPos, TOKENTYPE.COMMA);
			//
			// if (nextComma < 0 || nextComma > nextSemicolon) { // var test=100;
			//
			// int nextAssign = getPositionOfNextToken(startPos, TOKENTYPE.ASSIGN);
			//
			// if (nextAssign > 0 && nextAssign < nextSemicolon) {
			// String name = getNameOfType(startPos, nextAssign - 1);
			// String body = getNameOfType(nextAssign + 1, nextSemicolon - 1);
			// vars.add(new Variable(startPos, nextSemicolon - 1, name, body));
			//
			// }
			// else {
			// vars.add(new Variable(startPos, nextSemicolon - 1,
			// getNameOfType(startPos,
			// nextSemicolon - 1), getNameOfType(nextAssign, nextSemicolon - 1)));
			// }
			// continue;
			// }
			// else { // var test=100,test2=102;
			//
			// int actualPos = startPos;
			//
			// // next comma exist and position is before next semicolon
			// while (nextComma > 0 && nextComma < nextSemicolon) {
			//
			// int nextAssign = getPositionOfNextToken(actualPos,
			// TOKENTYPE.ASSIGN);
			//
			// if (nextAssign > 0 && nextAssign < nextSemicolon) {
			// String name = getNameOfType(actualPos, nextAssign - 1);
			// String body = getNameOfType(nextAssign + 1, nextComma - 1);
			// vars.add(new Variable(actualPos, nextComma - 1, name, body));
			// }
			// else {
			// String name = getNameOfType(actualPos, nextComma - 1);
			// String body = "undefined";
			// vars.add(new Variable(actualPos, nextComma - 1, name, body));
			// }

			// actualPos = nextComma + 1;
			//
			// nextComma = getPositionOfNextToken(actualPos, TOKENTYPE.COMMA);
			// nextSemicolon = getPositionOfNextToken(actualPos,
			// TOKENTYPE.SEMICOLON);
			// }
			//
			// int nextAssign = getPositionOfNextToken(actualPos,
			// TOKENTYPE.ASSIGN);
			//
			// if (nextAssign > 0 && nextAssign < nextSemicolon) {
			// String name = getNameOfType(actualPos, nextAssign - 1);
			// String body = getNameOfType(nextAssign + 1, nextSemicolon - 1);
			// vars.add(new Variable(actualPos, nextSemicolon - 1, name, body));
			// }
			// else {
			// String name = getNameOfType(actualPos, nextSemicolon - 1);
			// String body = "undefined";
			// vars.add(new Variable(actualPos, nextSemicolon - 1, name, body));
			// }
			// continue;
			// }
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
	private void processWhile() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		//Validate.notNull(loops);

		log.info("start processLoops analyse process...");

	
	}
	
	/**
	 * 
	 */
	private void processFor() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		//Validate.notNull(loops);

		log.info("start processLoops analyse process...");

	
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
