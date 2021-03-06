package edu.hm.counterobfuscator.parser.token;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.codec.EncoderException;

import edu.hm.counterobfuscator.definitions.AbstractType;
import edu.hm.counterobfuscator.definitions.Ajax;
import edu.hm.counterobfuscator.definitions.Call;
import edu.hm.counterobfuscator.definitions.Default;
import edu.hm.counterobfuscator.definitions.ForWhile;
import edu.hm.counterobfuscator.definitions.Function;
import edu.hm.counterobfuscator.definitions.If;
import edu.hm.counterobfuscator.definitions.Return;
import edu.hm.counterobfuscator.definitions.This;
import edu.hm.counterobfuscator.definitions.TryCatch;
import edu.hm.counterobfuscator.definitions.Variable;
import edu.hm.counterobfuscator.helper.Scope;
import edu.hm.counterobfuscator.helper.Validate;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 25.12.2014
 * 
 *       after tokenising it is necessary analyze each token, to find JavaScript
 *       definitions like VAR, FUNCTION, etc.
 * 
 *       class is public
 * 
 */
/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 28.04.2015
 * 
 * 
 */
public class TokenAnalyser implements ITokenAnalyser {

	private static Logger		log;
	private List<Token>			allTokensOfJSCode;
	private Token					actualToken;
	private List<AbstractType>	allTypes;

	public TokenAnalyser(ITokenizer tokenizer) {

		Validate.notNull(tokenizer);

		log = Logger.getLogger(TokenAnalyser.class.getName());

		this.allTokensOfJSCode = tokenizer.getTokens();

		allTypes = new ArrayList<AbstractType>();

		Validate.notNull(allTokensOfJSCode);

	}

	/**
	 * @throws IllegalArgumentException
	 * @throws EncoderException
	 */
	public void process() throws IllegalArgumentException, EncoderException {

		log.info("start token analyse process...");

		if (allTokensOfJSCode.size() > 0) {
			actualToken = allTokensOfJSCode.get(0);
		} else {
			// nothing to do here
			return;
		}

		for (int i = 0; i < allTokensOfJSCode.size(); i++) {
			if (allTokensOfJSCode.get(i).getDefinition() == TOKENTYPE.WHITESPACE) {
				allTokensOfJSCode.remove(i);
			} else {
				break;
			}
		}

		for (int i = allTokensOfJSCode.size() - 1; i >= 0; i--) {

			if (allTokensOfJSCode.get(i).getDefinition() == TOKENTYPE.WHITESPACE) {
				allTokensOfJSCode.remove(i);
			} else {
				break;
			}
		}

		while (hasNextToken()) {

			call(actualToken.getDefinition());
			setToNextToken();
		}

		log.info("token analyse finished.");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.token.ITokenAnalyser#getAllTypes()
	 */
	public List<AbstractType> getAllTypes() {

		return allTypes;
	}

	/**
	 * @return the actual token
	 */
	private Token getActualToken() {

		return actualToken;
	}

	/**
	 * @param pos
	 * 
	 *           set next token to pos
	 */
	private void setNextTokenTo(int pos) {

		actualToken = allTokensOfJSCode.get(pos);
	}

	/**
	 * set token to next token
	 */
	private void setToNextToken() {

		// end is near
		if (actualToken.getPos() == allTokensOfJSCode.size() - 1) {
			return;
		}

		actualToken = allTokensOfJSCode.get(actualToken.getPos() + 1);
	}

	/**
	 * @param token
	 * @return next token of token
	 */
	private Token getNextTokenOf(Token token) {

		return allTokensOfJSCode.get(token.getPos() + 1);
	}

	/**
	 * @return true if token has a next token
	 */
	private boolean hasNextToken() {

		if (actualToken.getPos() == allTokensOfJSCode.size() - 1) {
			return false;
		}

		return true;
	}

	/**
	 * @param type
	 * @throws IllegalArgumentException
	 * @throws EncoderException
	 * 
	 *            call correct methode to process the javascript statement
	 */
	private void call(TOKENTYPE type) throws IllegalArgumentException, EncoderException {

		switch (type) {
		case VAR:
			processVar();
			break;
		case EVAL:
		case FUNCTION:
			processFunction();
			break;
		case WHILE:
			processWhile();
			break;
		case FOR:
			processFor();
			break;
		case IF:
			processIf();
			break;
		case THIS:
			processThis();
			break;
		case RETURN:
			processReturn();
			break;
		case STRING:
			processString();
			break;
		case TRY:
			processTry();
			break;
		case CATCH:
			processCatch();
			break;
		case JQUERY:
			processJQuery();
		default:
			break;

		}
	}

	/**
	 * process if statement
	 */
	private void processIf() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(allTypes);

		log.info("process if statement...");

		int startPos = getActualToken().getPos();

		int nextOpenBracket = getPositionOfNextToken(startPos, TOKENTYPE.OPEN_BRACKET);
		int nextClosedBracket = getPositionOfNextToken(nextOpenBracket, TOKENTYPE.CLOSE_BRACKET);
		int nextCurlyOpenBracket = getPositionOfNextToken(nextClosedBracket,
				TOKENTYPE.OPEN_CURLY_BRACKET);
		int endPos = getPositionOfNextToken(nextCurlyOpenBracket, TOKENTYPE.CLOSE_CURLY_BRACKET);

		String head = getNameOfType(nextOpenBracket, nextClosedBracket);
		String body = getNameOfType(nextCurlyOpenBracket, endPos);

		allTypes.add(new If(new Scope(startPos, endPos), "if", head, body));

		setNextTokenTo(nextCurlyOpenBracket);

	}

	/**
	 * function to jquery.getScript calls Get and run a JavaScript using an AJAX
	 * request:
	 * 
	 * e.g. $.getScript("demo_ajax_script.js");
	 */
	private void processJQuery() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(allTypes);

		log.info("process jquery statement...");

		int startPos = getActualToken().getPos();
		int openBracket = getPositionOfNextToken(startPos, TOKENTYPE.OPEN_BRACKET);
		int closedBracket = getPositionOfNextToken(openBracket, TOKENTYPE.CLOSE_BRACKET);
		String name = getNameOfType(startPos + 2, openBracket - 1);
		String value = getNameOfType(openBracket + 1, closedBracket - 1);

		int endPos = getPositionOfNextToken(startPos, TOKENTYPE.SEMICOLON);

		allTypes.add(new Ajax(new Scope(startPos, endPos), name, value));

		setNextTokenTo(endPos);
	}

	/**
	 * process string
	 */
	private void processString() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(allTypes);

		log.info("process string statement...");

		int startPos = getActualToken().getPos();
		int assign = getPositionOfNextToken(startPos, TOKENTYPE.ASSIGN);
		int endPosSemikolon = getPositionOfNextToken(startPos, TOKENTYPE.SEMICOLON);
		int endPosCurlyBracket = getPositionOfNextToken(startPos, TOKENTYPE.CLOSE_CURLY_BRACKET);

		int endPos = endPosSemikolon;

		// if statement ends with an } instead of an ;
		if (endPosCurlyBracket > -1 && endPosCurlyBracket < endPosSemikolon) {
			endPos = endPosCurlyBracket - 1;
		}

		if (endPos == 0) {
			endPos = 1;
		}

		// if a assign is found in statement => it is a var statement
		Token nextToken = null;

		if (getNextTokenOf(getActualToken()).getDefinition() == TOKENTYPE.WHITESPACE) {
			setToNextToken();
		}

		if (assign > 0 && assign < endPos) {

			nextToken = getNextTokenOf(getActualToken());

			if (!(nextToken.getDefinition() == TOKENTYPE.PLUS)
					&& !(nextToken.getDefinition() == TOKENTYPE.MINUS)) {
				setNextTokenTo(assign);
				nextToken = getActualToken();
			}

		} else {
			nextToken = getNextTokenOf(getActualToken());
		}

		// ignore whitespaces
		if (nextToken.getDefinition() == TOKENTYPE.WHITESPACE) {
			nextToken = getNextTokenOf(nextToken);
		}

		switch (nextToken.getDefinition()) {
		case DOT:
			// Name.Func(Parameter); isObject = true;
			System.out.println("FunctionCall");
			int dot = nextToken.getPos();

			int openBracket = getPositionOfNextToken(startPos, TOKENTYPE.OPEN_BRACKET);

			String nameFC = getNameOfType(startPos, dot - 1);
			String functionFC = getNameOfType(dot + 1, openBracket - 1);
			// -2 because ignore last )
			String valueFC = getNameOfType(openBracket + 1, endPos - 2);

			allTypes.add(new Call(new Scope(startPos, endPos), nameFC, functionFC, valueFC));

			break;
		case PLUS:
		case MINUS:
			int assign1 = -100;
			int plusMinus = nextToken.getPos() - 1;

			if (getNextTokenOf(nextToken).getDefinition() == TOKENTYPE.PLUS) {
				Variable var = new Variable(new Scope(startPos, endPos), getNameOfType(startPos,
						plusMinus), "", "++", false);
				var.setGlobal(true);
				allTypes.add(var);
				break;
			} else if (getNextTokenOf(nextToken).getDefinition() == TOKENTYPE.MINUS) {
				Variable var = new Variable(new Scope(startPos, endPos), getNameOfType(startPos,
						plusMinus), "", "--", false);
				var.setGlobal(true);
				allTypes.add(var);
				break;
			} else {
				assign1 = getNextTokenOf(nextToken).getPos();
			}
			// ++ --
			// test -= test;
			// test += test;
			String name1 = getNameOfType(startPos, assign1 - 2);
			String value1 = getNameOfType(assign1 + 1, endPos - 1);

			Variable var1 = new Variable(new Scope(startPos, endPos), name1, "+=", value1, false);

			if (!allTypes.contains(var1)) {
				var1.setGlobal(true);
			}

			allTypes.add(var1);

			break;
		case ASSIGN:
			// Variable(Position pos, String name, String value, boolean
			// isObject)
			// var test = new Name(Parameter); wird in processVar() behandelt
			// int assign = nextToken.getPos();

			String name = getNameOfType(startPos, assign - 1);
			String value = getNameOfType(assign + 1, endPos - 1);

			Variable var = new Variable(new Scope(startPos, endPos), name, "=", value, false);

			if (!allTypes.contains(var)) {
				var.setGlobal(true);
			}

			allTypes.add(var);

			break;
		default:
			allTypes.add(new Default(new Scope(startPos, endPos), getNameOfType(startPos, endPos)));
		}
		setNextTokenTo(endPos);

	}

	/**
	 * process return statement
	 */
	private void processReturn() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(allTypes);

		log.info("process return statement...");

		int startPos = getActualToken().getPos();
		int endPosSemikolon = getPositionOfNextToken(startPos, TOKENTYPE.SEMICOLON);
		int endPosCurlyBracket = getPositionOfNextToken(startPos, TOKENTYPE.CLOSE_CURLY_BRACKET);

		int endPos = endPosSemikolon;

		if (endPosSemikolon < 0 || (endPosCurlyBracket > -1 && endPosCurlyBracket < endPosSemikolon)) {
			endPos = endPosCurlyBracket;
		}

		String name = getNameOfType(startPos + 1, endPos - 1);

		allTypes.add(new Return(new Scope(startPos, endPos - 1), name));

		setNextTokenTo(endPos);

	}

	/**
	 * this ia a super duper methode to split a var line which is separted with
	 * commas
	 * 
	 * e.g. var a=1, b=2, c; => var a=1; var b=2; var c;
	 */
	private List<String> mightyCommaAndSplitMethode(int startPos, int endPos) {

		List<String> newList = new ArrayList<String>();

		int indexComma = getPositionOfNextToken(startPos, TOKENTYPE.COMMA);

		if (indexComma < 0 || indexComma > endPos) { // found NO comma
			String name = getNameOfType(startPos, endPos);

			if (name.indexOf("var") > -1) {
				name = name.replaceAll("var", "");
			}
			newList.add(name);
		} else { // found a comma

			boolean endReached = false;

			while (indexComma > -1) {

				String name = getNameOfType(startPos, indexComma - 1);
				startPos = indexComma + 1;
				indexComma = getPositionOfNextToken(startPos, TOKENTYPE.COMMA);
				int indexSemiColon = getPositionOfNextToken(startPos, TOKENTYPE.SEMICOLON);

				if (name.indexOf("var") > -1) {
					name = name.replaceAll("var", "");
				}
				newList.add(name);

				// break while loop
				if (endReached) {
					break;
				}

				if (indexSemiColon < indexComma) {
					indexComma = indexSemiColon;
					endReached = true;
				}

			}

		}

		return newList;

	}

	/**
	 * process var statement
	 * 
	 * @throws EncoderException
	 */
	private void processVar() throws EncoderException {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(allTypes);

		log.info("process var statement...");

		// var test = obj.Func(Parameter) -> processDefault();
		int startPos = getActualToken().getPos();
		int endPos = getPositionOfNextToken(startPos, TOKENTYPE.SEMICOLON);

		boolean isObject = false;

		// get all vars which are declared with one statement
		List<String> vars = mightyCommaAndSplitMethode(startPos, endPos - 1);

		for (String actualVar : vars) {

			int assign = actualVar.indexOf("=");
			// has an assignment
			if (assign > -1) {
				String name = actualVar.substring(0, assign);
				name = name.replaceAll(" ", "");

				String value = actualVar.substring(assign + 1);

				// value has new in it
				int newIndex = value.indexOf("new");
				if (newIndex > -1) {
					isObject = true;
					log.info("found an object with name: " + name);
				}

				allTypes.add(new Variable(new Scope(startPos, endPos), name, "=", value, isObject));

			} else {
				String name = actualVar.replaceAll(" ", "");
				allTypes.add(new Variable(new Scope(startPos, endPos), name, "=", "", isObject));
			}

		}

		setNextTokenTo(endPos);

	}

	/**
	 * process function statement
	 */
	private void processFunction() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(allTypes);

		log.info("process function statement...");

		int startPos = getActualToken().getPos();

		int nextOpenBracket = -1;
		int nextClosedBracket = -1;
		int nextCurlyOpenBracket = -1;
		int endPos = -1;

		if (getActualToken().getDefinition() == TOKENTYPE.FUNCTION) {
			nextOpenBracket = getPositionOfNextToken(startPos, TOKENTYPE.OPEN_BRACKET);
			nextClosedBracket = getPositionOfNextToken(nextOpenBracket, TOKENTYPE.CLOSE_BRACKET);
			nextCurlyOpenBracket = getPositionOfNextToken(nextClosedBracket,
					TOKENTYPE.OPEN_CURLY_BRACKET);
			endPos = getPositionOfNextToken(nextCurlyOpenBracket, TOKENTYPE.CLOSE_CURLY_BRACKET);

			String name = getNameOfType(startPos + 1, nextOpenBracket - 1);
			String head = getNameOfType(nextOpenBracket, nextClosedBracket);
			String body = getNameOfType(nextCurlyOpenBracket, endPos);

			boolean isPacked = false;

			if (startPos > 0
					&& allTokensOfJSCode.get(startPos - 1).getDefinition() == TOKENTYPE.OPEN_BRACKET) {
				startPos--;
				endPos = allTokensOfJSCode.size() -1; //getPositionOfNextToken(endPos + 1, TOKENTYPE.CLOSE_BRACKET) + 1;
				name = "";
				head = "()";
				body = getNameOfType(startPos, endPos);
				isPacked = true;
			}

			allTypes.add(new Function(new Scope(startPos, endPos), name, head, body, isPacked));

			if (isPacked) {
				setNextTokenTo(endPos);
			} else {
				setNextTokenTo(nextCurlyOpenBracket);
			}
			
		}

		if (getActualToken().getDefinition() == TOKENTYPE.EVAL) {
			nextOpenBracket = getPositionOfNextToken(startPos, TOKENTYPE.OPEN_BRACKET);
			endPos = getPositionOfNextToken(nextOpenBracket, TOKENTYPE.CLOSE_BRACKET);
			
			String body = getNameOfType(nextOpenBracket, endPos);
			
			allTypes.add(new Function(new Scope(startPos, endPos), "eval", "()", body, true));
			setNextTokenTo(endPos);
		}	

	}

	/**
	 * process this statement
	 */
	private void processThis() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(allTypes);

		log.info("process this statement...");

		int startPos = getActualToken().getPos();

		int assign = getPositionOfNextToken(startPos, TOKENTYPE.ASSIGN);
		int endPosSemikolon = getPositionOfNextToken(startPos, TOKENTYPE.SEMICOLON);
		int endPosCurlyBracket = getPositionOfNextToken(startPos, TOKENTYPE.CLOSE_CURLY_BRACKET);

		int endPos = endPosSemikolon;

		if (endPosCurlyBracket > -1 && endPosCurlyBracket < endPosSemikolon) {
			endPos = endPosCurlyBracket - 1;

			int endPosNextThis = getPositionOfNextToken(startPos + 1, TOKENTYPE.THIS);

			if (endPosNextThis > -1 && endPosNextThis < endPosCurlyBracket) {
				endPos = endPosNextThis - 1;
			}
		}

		String name = getNameOfType(startPos + 1, assign - 1);
		String value = getNameOfType(assign + 1, endPos - 1);

		allTypes.add(new This(new Scope(startPos, endPos), name, "", value));

		setNextTokenTo(assign);
	}

	/**
	 * process while statement
	 */
	private void processWhile() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(allTypes);

		log.info("process while statement...");

		int startPos = getActualToken().getPos();

		int nextOpenBracket = getPositionOfNextToken(startPos, TOKENTYPE.OPEN_BRACKET);
		int nextClosedBracket = getPositionOfNextToken(nextOpenBracket, TOKENTYPE.CLOSE_BRACKET);
		int nextCurlyOpenBracket = getPositionOfNextToken(nextClosedBracket,
				TOKENTYPE.OPEN_CURLY_BRACKET);
		int endPos = getPositionOfNextToken(nextCurlyOpenBracket, TOKENTYPE.CLOSE_CURLY_BRACKET);

		String head = getNameOfType(nextOpenBracket, nextClosedBracket);
		String body = getNameOfType(nextCurlyOpenBracket, endPos);

		allTypes.add(new ForWhile(new Scope(startPos, endPos), "while", head, body));

		setNextTokenTo(nextCurlyOpenBracket);

	}

	/**
	 * process for statement
	 */
	private void processFor() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(allTypes);

		log.info("process for statement...");

		int startPos = getActualToken().getPos();

		int nextOpenBracket = getPositionOfNextToken(startPos, TOKENTYPE.OPEN_BRACKET);
		int nextClosedBracket = getPositionOfNextToken(nextOpenBracket, TOKENTYPE.CLOSE_BRACKET);
		int nextCurlyOpenBracket = getPositionOfNextToken(nextClosedBracket,
				TOKENTYPE.OPEN_CURLY_BRACKET);
		int endPos = getPositionOfNextToken(nextCurlyOpenBracket, TOKENTYPE.CLOSE_CURLY_BRACKET);

		String head = getNameOfType(nextOpenBracket, nextClosedBracket);
		String body = getNameOfType(nextCurlyOpenBracket, endPos);

		allTypes.add(new ForWhile(new Scope(startPos, endPos), "for", head, body));

		setNextTokenTo(nextCurlyOpenBracket);
	}

	/**
	 * process try statement
	 */
	private void processTry() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(allTypes);

		log.info("process try statement...");

		int startPos = getActualToken().getPos();

		int nextCurlyOpenBracket = getPositionOfNextToken(startPos, TOKENTYPE.OPEN_CURLY_BRACKET);

		int endPos = getPositionOfNextToken(nextCurlyOpenBracket, TOKENTYPE.CLOSE_CURLY_BRACKET);

		allTypes.add(new TryCatch(new Scope(startPos, endPos), "try", null));

		setNextTokenTo(nextCurlyOpenBracket);
	}

	/**
	 * process catch statement
	 */
	private void processCatch() {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(allTypes);

		log.info("process catch statement...");

		int startPos = getActualToken().getPos();

		int nextCurlyOpenBracket = getPositionOfNextToken(startPos, TOKENTYPE.OPEN_CURLY_BRACKET);

		int endPos = getPositionOfNextToken(nextCurlyOpenBracket, TOKENTYPE.CLOSE_CURLY_BRACKET);

		allTypes.add(new TryCatch(new Scope(startPos, endPos), "catch", null));

		setNextTokenTo(nextCurlyOpenBracket);
	}

	/**
	 * @param type
	 * @return String name of type
	 */
	private String getNameOfType(int startPos, int endPos) {

		if (startPos > endPos) {
			return "";
		}

		return getStringOfTokens(getAllTokensUntilEndPos(startPos, endPos));
	}

	/**
	 * @param startPos
	 * @param endPos
	 * @return al list of tokens until a endPos from a startPos
	 */
	private List<Token> getAllTokensUntilEndPos(int startPos, int endPos) {

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
	private int getPositionOfNextToken(int startPos, TOKENTYPE... type) {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.isTrue(startPos > -1);
		Validate.notNull(type);

		for (int i = startPos; i < allTokensOfJSCode.size(); i++) {

			// check that token has correct Type and is not within brackets
			// like:
			// no: var AAAA='krkeIplIaMcMIe'.replace(/[BBBB]/g,'');
			// yes: var AAAA,BBBB;
			if (isIn(allTokensOfJSCode.get(i).getDefinition(), type)
					&& (!isTokenWithinBrackets(startPos + 1, allTokensOfJSCode.get(i)))) {
				return allTokensOfJSCode.get(i).getPos();
			}
		}

		return -1;
	}

	/**
	 * @param type
	 * @param tokentypes
	 * @return
	 */
	private boolean isIn(TOKENTYPE type, TOKENTYPE... tokentypes) {

		for (TOKENTYPE token : tokentypes) {

			if (token == type)
				return true;
		}

		return false;

	}

	/**
	 * @param tokensToString
	 * @return create a string from a given list of tokens
	 */
	private String getStringOfTokens(List<Token> tokensToString) {

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
	 * @param tokenToTest
	 * @return true if Token is within two Brackets, otherwise false
	 * 
	 *         startPos because some token are in brackets and we like to ignore
	 *         that, e.g. var test = (function (a,b) {return a+b;}); ignore first
	 *         and last round bracket
	 */
	private boolean isTokenWithinBrackets(int startPos, Token tokenToTest) {

		Validate.isTrue(allTokensOfJSCode.size() > 0);
		Validate.notNull(tokenToTest);

		int openCurlyBrackets = 0;
		int openBrackets = 0;
		int openSquareBrackets = 0;
		int quote = 0;

		for (int i = startPos; i < tokenToTest.getPos(); i++) {

			TOKENTYPE tokenType = allTokensOfJSCode.get(i).getDefinition();

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
			} else if (tokenType == TOKENTYPE.QUOTE && quote == 0) {
				quote++;
			}else if (tokenType == TOKENTYPE.QUOTE) {
				quote--;
			}
			
		}

		if (openBrackets > 0 || openCurlyBrackets > 0 || openSquareBrackets > 0 || quote > 0) {
			return true;
		} else {
			return false;
		}
	}
}
