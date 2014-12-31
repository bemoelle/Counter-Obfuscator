package edu.hm.webscraper.parser.token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.hm.webscraper.helper.Validate;
import edu.hm.webscraper.types.Function;
import edu.hm.webscraper.types.IType;
import edu.hm.webscraper.types.Variable;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 25.12.2014
 * 
 *       after tokenising it is necessary analyze each token, to find JavaScript
 *       keywords like VAR, FUNCTION, etc.
 * 
 */
class TokenAnalyser implements ITokenAnalyser {

	private List<Token>		allTokensOfJSCode;
	private List<IType>		vars;
	private Set<Function>	functions;
	private Set<Function>	loops;
	private Set<Function>	tryCatch;
	private Tokenizer			tokenizer;

	public TokenAnalyser(Tokenizer tokenizer) {

		Validate.notNull(tokenizer);

		this.allTokensOfJSCode = tokenizer.getTokens();
		this.tokenizer = tokenizer;

		vars = new ArrayList<IType>();
		functions = new HashSet<Function>();
		loops = new HashSet<Function>();
		tryCatch = new HashSet<Function>();

	}

	/**
	 * @return tokenizer
	 */
	public List<Token> getTokens() {
		return allTokensOfJSCode;
	}

	/**
	 * @return vars
	 */
	public List<IType> getTypesOfTokenTypes(TOKENTYPE type) {
		Validate.notNull(type);

		if (type == TOKENTYPE.VAR) {
			Validate.notNull(vars);
			return vars;
		}
		else {
			return null;
		}
	}

	/**
	 * @param type
	 * @return String name of type
	 */
	public String getNameOfType(IType type) {

		int startPos = type.getStartPos();
		int endPos = type.getEndPos();

		if ((endPos - startPos) == 0) {
			return getStringOfTokens(getAllTokensUntilEndPos(startPos, endPos));
		}
		else {
			int posOfAssign = getPositionOfNextToken(startPos, TOKENTYPE.ASSIGN) - 1;
			return getStringOfTokens(getAllTokensUntilEndPos(startPos, posOfAssign));
		}
	}

	/**
	 * @param type
	 * @return String value of type
	 */
	public String getValueOfType(IType type) {

		int startPos = type.getStartPos();
		int endPos = type.getEndPos();

		int posOfAssign = getPositionOfNextToken(startPos, TOKENTYPE.ASSIGN) + 1;

		if (posOfAssign == -1 || posOfAssign > endPos) {
			return "undefined";
		}
		else {
			return getStringOfTokens(getAllTokensUntilEndPos(posOfAssign, endPos));
		}
	}

	/**
	 * 
	 */
	void process() {

		processLocalVariables();
		// processGlobalVariables();
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

			int nextComma = getPositionOfNextToken(startPos, TOKENTYPE.COMMA);
			int nextSemicolon = getPositionOfNextToken(startPos, TOKENTYPE.SEMICOLON);

			if (nextComma > nextSemicolon) { // var test=100;
				vars.add(new Variable(startPos,
						getPositionOfNextToken(startPos, TOKENTYPE.SEMICOLON) - 1));
				continue;
			}
			else { // var test=100,test2=102;

				int actualPos = startPos;

				// next comma exist and position is before next semicolon
				while (nextComma > 0 && nextComma < nextSemicolon) {

					vars.add(new Variable(actualPos, nextComma - 1));

					actualPos = nextComma + 1;

					nextComma = getPositionOfNextToken(actualPos, TOKENTYPE.COMMA);
					nextSemicolon = getPositionOfNextToken(actualPos, TOKENTYPE.SEMICOLON);
				}

				vars.add(new Variable(actualPos, nextSemicolon - 1));
				continue;
			}
		}
	}

	/**
	 * 
	 */
	private void processGlobalVariables() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(vars);

		List<Token> tokens = getAllTokensOfSameType(TOKENTYPE.ASSIGN);

		for (Token token : tokens) {

			int pos = token.getPos();
			if (pos > 1 && allTokensOfJSCode.get(pos - 1).getType() == TOKENTYPE.STRING
					&& allTokensOfJSCode.get(pos - 2).getType() != TOKENTYPE.VAR) {
				String name = allTokensOfJSCode.get(pos - 1).getValue();

				String value = getStringOfTokens(getAllTokensUntilType(pos + 1, TOKENTYPE.SEMICOLON));

				// TODO check if global is local var :)
				// vars.add(new Variable(pos - 1, name, value));
			}
		}
	}

	/**
	 * 
	 */
	private void processFunctions() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(functions);

		List<Token> tokens = getAllTokensOfSameType(TOKENTYPE.FUNCTION);

		for (Token actualToken : tokens) {

			String name = "";
			String head = "";
			String body = "";

			int pos = actualToken.getPos();
			int posOfHead = actualToken.getPos();
			Token nextToken = allTokensOfJSCode.get(pos + 1);

			if (nextToken.getType() == TOKENTYPE.STRING) {
				// function has A functioname

				name = nextToken.getValue();
				posOfHead = pos + 3;

			}
			else if (nextToken.getType() == TOKENTYPE.OPEN_BRACKET) {
				// Anonymous function, function has NO functionname

				name = "undefined";
				posOfHead = pos + 2;

			}
			else {
				// TODO through exception -> JavaScript Failure
				System.out.println("FEHLER");
			}

			// get token of begin of head
			// function (p,a,c,k,e,d)
			nextToken = allTokensOfJSCode.get(posOfHead);
			head = getStringOfTokens(getAllTokensUntilType(posOfHead, TOKENTYPE.CLOSE_BRACKET));

			int xxx = getPositionOfNextToken(posOfHead, TOKENTYPE.OPEN_CURLY_BRACKET);
			List<Token> yyy = getAllTokensBetweenBrackets(xxx, TOKENTYPE.OPEN_CURLY_BRACKET,
					TOKENTYPE.CLOSE_CURLY_BRACKET);
			body = getStringOfTokens(yyy);

			Function function = new Function(pos, name, head, body);

			// set if function is Packed
			// (function(p,a,c,k,e,d) {...})(p,a,c,k,e,d))
			if (pos > 0 && allTokensOfJSCode.get(pos - 1).getType() == TOKENTYPE.OPEN_BRACKET) {

				// TODO setPacked wars
				function.setPacked(true);
				function.setPackedVars(null);
			}

			// set if function has a return statement
			// function(p,a,c,k,e,d) {... return ...};
			if (isTokenWithinStartAndEndPos(pos,
					getPositionOfNextToken(pos, TOKENTYPE.CLOSE_CURLY_BRACKET), TOKENTYPE.RETURN)) {

				function.setHasReturn(true);
			}

			functions.add(function);
		}

	}

	/**
	 * 
	 */
	private void processLoops() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(loops);

		// TODO ...
	}

	private void processTryCatchStatements() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(tryCatch);

		// TODO ...

	}

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

	public int getPositionOfNextToken(int startPos, TOKENTYPE type) {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.isTrue(startPos > -1);
		Validate.notNull(type);

		for (int i = startPos; i < allTokensOfJSCode.size(); i++) {

			// check that token has correct Type and is not within brackets like:
			// no: var AAAA='krkeIplIaMcMIe'.replace(/[BBBB]/g,'');
			// yes: var AAAA,BBBB;
			if (allTokensOfJSCode.get(i).getType() == type
					&& !getTokenIsInBrackets(allTokensOfJSCode.get(i))) {
				return allTokensOfJSCode.get(i).getPos();
			}
		}

		return -1;
	}

	public String getStringOfTokens(List<Token> tokensToString) {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(tokensToString);

		String valueOfTokens = "";

		for (Token actualToken : tokensToString) {
			valueOfTokens += actualToken.getValue();
		}

		return valueOfTokens;
	}

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

	public List<Integer> getAllPosOfTokensByValue(String value) {

		// TODO VALIDATE

		List<Integer> posOfTokens = new ArrayList<Integer>();

		for (Token actualToken : allTokensOfJSCode) {

			if (actualToken.getValue().equals(value)) {
				posOfTokens.add(actualToken.getPos());
			}
		}

		return posOfTokens;
	}

	public boolean getTokenIsInBrackets(Token tokenToTest) {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(tokenToTest);

		int openCurlyBrackets = 0;
		int openBrackets = 0;
		int openSquareBrackets = 0;

		for (int i = 0; i < tokenToTest.getPos(); i++) {

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
