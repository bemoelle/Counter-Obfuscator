package edu.hm.webscraper.parser.token;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.hm.webscraper.helper.Function;
import edu.hm.webscraper.helper.Validate;
import edu.hm.webscraper.helper.Variable;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 25.12.2014
 * 
 *       after tokenising it is necessary analyse each token, to find JavaScript
 *       keywords like VAR, FUNCTION, etc.
 * 
 */
public class TokenAnalyser {

	private List<Token>		allTokensOfJSCode;
	private Set<Variable>	vars;
	private Set<Function>	functions;
	private Set<Function>	loops;
	private Tokenizer			tokenizer;

	public TokenAnalyser(Tokenizer tokenizer) {

		Validate.notNull(tokenizer);

		this.allTokensOfJSCode = tokenizer.getTokens();
		this.tokenizer = tokenizer;

		vars = new HashSet<Variable>();
		functions = new HashSet<Function>();
		loops = new HashSet<Function>();

	}

	/**
	 * 
	 */
	public void process() {

		processLocalVariables();
		processGlobalVariables();
		processFunctions();
		processLoops();

		for (Variable v : vars) {
			System.out.println(v.getName() + " : " + v.getValue());
		}

		System.out.println("----------------------------------");

		for (Function v : functions) {
			System.out.println(v.getName() + " : " + v.getHead() + " : " + v.getBody());
		}

	}

	/**
	 * @return
	 */
	public Set<Function> getFunctions() {
		return functions;
	}

	/**
	 * @param vars
	 */
	public void setVars(Set<Variable> vars) {
		this.vars = vars;
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
		// this.test3; --> wird in der jetztigen implementierng noch ignoriert
		// xxx,xxx2; --> kein gültiger JavaScript Code

		List<Token> tokens = tokenizer.getAllTokensOfSameType(TOKENTYPE.VAR);

		for (Token actualToken : tokens) {

			String varLineUntilSemikolon = tokenizer.getStringOfTokens(tokenizer
					.getAllTokensUntilType(actualToken.getPos() + 1, TOKENTYPE.SEMIKOLON));
			String[] varLineSplitted = varLineUntilSemikolon
					.split(",(?![^(\\[|\\(|\\{)(]*(\\)|\\]|\\}))");

			for (String actualString : varLineSplitted) {

				String value = "";

				String[] nameValueString = actualString.split("=");
				if (nameValueString.length > 1) {
					value = nameValueString[1];
				} else {
					value = "undefined";
				}
				Variable v = new Variable(0, nameValueString[0], value);
				v.setGlobal(false);
				v.setObject(false);
				vars.add(v);

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
					&& allTokensOfJSCode.get(pos - 3).getType() != TOKENTYPE.VAR) {
				String name = allTokensOfJSCode.get(pos - 1).getValue();

				String value = tokenizer.getStringOfTokens(tokenizer.getAllTokensUntilType(pos + 1,
						TOKENTYPE.SEMIKOLON));

				// TODO check if global is local var :)
				vars.add(new Variable(pos - 1, name, value));
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

			} else if (nextToken.getType() == TOKENTYPE.OPEN_BRACKET) {
				// Anonymous function, function has NO functionname

				name = "undefined";
				posOfHead = pos + 2;

			} else {
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

}
