package edu.hm.webscraper.parser.token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.xalan.trace.EndSelectionEvent;

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
public class TokenAnalyser {

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
	public Tokenizer getTokenizer() {
		return tokenizer;
	}

	/**
	 * @return
	 */
	public Set<Function> getFunctions() {
		return functions;
	}

	/**
	 * @return vars
	 */
	public List<IType> getVars() {
		return vars;
	}

	/**
	 * @param type
	 * @return String name of type
	 */
	public String getNameOfType(IType type) {

		int startPos = type.getStartPos();
		int endPos = type.getEndPos();

		if ((endPos - startPos) == 0) {
			return tokenizer.getStringOfTokens(tokenizer.getAllTokensUntilEndPos(startPos, endPos));
		}
		else {
			int posOfAssign = tokenizer.getPositionOfNextToken(startPos, TOKENTYPE.ASSIGN) - 1;
			return tokenizer.getStringOfTokens(tokenizer
					.getAllTokensUntilEndPos(startPos, posOfAssign));
		}
	}

	/**
	 * @param type
	 * @return String value of type
	 */
	public String getValueOfType(IType type) {

		int startPos = type.getStartPos();
		int endPos = type.getEndPos();

		int posOfAssign = tokenizer.getPositionOfNextToken(startPos, TOKENTYPE.ASSIGN) + 1;

		if (posOfAssign == -1 || posOfAssign > endPos) {
			return "undefined";
		}
		else {
			return tokenizer.getStringOfTokens(tokenizer.getAllTokensUntilEndPos(posOfAssign, endPos));
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

		List<Token> tokens = tokenizer.getAllTokensOfSameType(TOKENTYPE.VAR);

		for (Token actualToken : tokens) {

			// ignore VAR statement at the beginning
			int startPos = actualToken.getPos() + 1;

			int nextComma = tokenizer.getPositionOfNextToken(startPos, TOKENTYPE.COMMA);
			int nextSemicolon = tokenizer.getPositionOfNextToken(startPos, TOKENTYPE.SEMICOLON);

			if (nextComma > nextSemicolon) { // var test=100;
				vars.add(new Variable(startPos, tokenizer.getPositionOfNextToken(startPos,
						TOKENTYPE.SEMICOLON) - 1));
				continue;
			}
			else { // var test=100,test2=102;

				int actualPos = startPos;

				// next comma exist and position is before next semicolon
				while (nextComma > 0 && nextComma < nextSemicolon) {

					vars.add(new Variable(actualPos, nextComma - 1));

					actualPos = nextComma + 1;

					nextComma = tokenizer.getPositionOfNextToken(actualPos, TOKENTYPE.COMMA);
					nextSemicolon = tokenizer.getPositionOfNextToken(actualPos, TOKENTYPE.SEMICOLON);
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

		List<Token> tokens = tokenizer.getAllTokensOfSameType(TOKENTYPE.ASSIGN);

		for (Token token : tokens) {

			int pos = token.getPos();
			if (pos > 1 && allTokensOfJSCode.get(pos - 1).getType() == TOKENTYPE.STRING
					&& allTokensOfJSCode.get(pos - 2).getType() != TOKENTYPE.VAR) {
				String name = allTokensOfJSCode.get(pos - 1).getValue();

				String value = tokenizer.getStringOfTokens(tokenizer.getAllTokensUntilType(pos + 1,
						TOKENTYPE.SEMICOLON));

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

		List<Token> tokens = tokenizer.getAllTokensOfSameType(TOKENTYPE.FUNCTION);

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
			head = tokenizer.getStringOfTokens(tokenizer.getAllTokensUntilType(posOfHead,
					TOKENTYPE.CLOSE_BRACKET));

			int xxx = tokenizer.getPositionOfNextToken(posOfHead, TOKENTYPE.OPEN_CURLY_BRACKET);
			List<Token> yyy = tokenizer.getAllTokensBetweenBrackets(xxx, TOKENTYPE.OPEN_CURLY_BRACKET,
					TOKENTYPE.CLOSE_CURLY_BRACKET);
			body = tokenizer.getStringOfTokens(yyy);

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
			if (tokenizer.isTokenWithinStartAndEndPos(pos,
					tokenizer.getPositionOfNextToken(pos, TOKENTYPE.CLOSE_CURLY_BRACKET),
					TOKENTYPE.RETURN)) {

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

}
