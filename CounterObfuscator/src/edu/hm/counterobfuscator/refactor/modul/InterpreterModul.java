package edu.hm.counterobfuscator.refactor.modul;

import java.util.logging.Logger;

import edu.hm.counterobfuscator.IClient;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.types.Ajax;
import edu.hm.counterobfuscator.types.Default;
import edu.hm.counterobfuscator.types.ForWhile;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.types.Call;
import edu.hm.counterobfuscator.types.This;
import edu.hm.counterobfuscator.types.Variable;

/**
 * @author
 * @date 30.12.2014
 * 
 *       class to rename obfuscated names, replace them with the assigned value
 */
public class InterpreterModul {

	private IClient			client;
	private static Logger	log;
	private String				jsScriptBuffer	= "";
	private boolean			handleError;

	public InterpreterModul(IClient client, boolean handleError) {

		this.handleError = handleError;
		InterpreterModul.log = Logger.getLogger(InterpreterModul.class.getName());

		this.client = client;

		// create an flat structure of a programmtree
		// this.flatProgrammTree = this.programmTree.flatten();
	}

	public Element process(Element element) throws Exception {

		log.info("start javascript interpreter process...");

		// for (int i = 0; i < flatProgrammTree.size(); i++) {

		callExecuteElement(element);
		// }

		log.info("finished javascript interpreter process");

		return element;

	}

	/**
	 * @param element
	 * @throws Exception
	 */
	private void callExecuteElement(Element element) throws Exception {

		switch (element.getType().getType()) {
		case FUNCTION:
			executeFunction(element);
			break;
		case CALL:
			executeFunctionCall(element);
			break;
		case VARIABLE:
			executeVariable(element);
			break;
		case FOR:
			executeFor(element);
			break;
		case THIS:
			executeThis(element);
			break;
		case AJAX:
			executeAjax(element);
			break;
		case DEFAULT:
			executeDefault(element);
		default:

		}
	}

	/**
	 * @param element
	 */
	private void executeAjax(Element element) {

		Ajax ajax = ((Ajax) element.getType());
		
		String result = executeJS(ajax.getName()+";").toString();
		if (result.indexOf("NO_EXECUTION") < 0) {
			ajax.setName(result);
		}
		
		result = executeJS(ajax.getValue()).toString();
		if (result.indexOf("NO_EXECUTION") < 0) {

			ajax.setValue(result);

		}
		
		String script = "$["+ajax.getName()+"]"+"("+ajax.getValue()+")";
		
		result = executeJS(script).toString();
		
		log.info("result is " + result);

	}

	/**
	 * @param element
	 * @return result if a function is executable packed or called
	 * 
	 *         execute function if function is packed and return result
	 *         (function(a,b,c){return a+b+c;})(1,2,3) -> result is 6
	 * 
	 */
	private void executeFunction(Element element) {

		Function func = ((Function) element.getType());
		if (func.isPacked()) {
			log.info("packed function found!");

			String script = func.getBodyAsString() + ";";

			Object result = executeJS(script);

			String resultAsString = result.toString();
			resultAsString = resultAsString.substring(1, resultAsString.length() - 1);

			element.removeAllChildren();

			func.setBodyAsString(resultAsString);

			log.info("result is " + result);
		}

	}

	private void executeFunctionCall(Element element) throws Exception {

		Call callStatement = ((Call) element.getType());

		String resultValue = executeJS(callStatement.getValue()).toString();
		if (resultValue.indexOf("NO_EXECUTION") < 0) {

			callStatement.setValue(resultValue);

		}

		resultValue = executeJS(callStatement.getFunction()).toString();
		if (resultValue.indexOf("NO_EXECUTION") < 0) {

			callStatement.setFunction(resultValue);

		}

		String toExecute = callStatement.getName() + "." + callStatement.getFunction() + "();";
		resultValue = executeJS(toExecute).toString();
		if (resultValue.indexOf("NO_EXECUTION") < 0) {

		}
		else {
			if (!handleError)
				throw new Exception("bla");
		}

	}

	private void executeVariable(Element element) throws Exception {

		Variable var = ((Variable) element.getType());

		String value = var.getValue();
		value = value.replace("\"", "\'");
		String resultValue = executeJS(var.getName()+"="+value).toString();
		// statement is executable
		if (resultValue.indexOf("NO_EXECUTION") < 0) {

			var.setValue(resultValue);
			jsScriptBuffer += "var " + var.getName() + "=" + resultValue + ";";

		}
		else {
			if (!handleError) {
				throw new Exception("bla");
			}
		}

		if (var.isObject()) {
			String resultParameter = executeJS(var.getParameter()).toString();
			if (resultParameter.indexOf("NO_EXECUTION") < 0) {

				var.setParameter(resultParameter);
				var.setExecutable(false);

			}
			else {
				if (!handleError)
					throw new Exception("bla");
			}
		}

	}

	private void executeFor(Element element) {

		ForWhile forWhile = ((ForWhile) element.getType());

	}

	private void executeDefault(Element element) {

		Default defaultType = ((Default) element.getType());

	}

	private String executeThis(Element element) {

		This type = ((This) element.getType());

		String name = type.getName();
		type.setName(executeJS(name) + "");

		return type.getName() + ";";
	}

	private Object executeJS(String script) {

		Object result = null;
		try {
			result = client.getJSResult(jsScriptBuffer + script);
		}
		catch (Exception e) {
			result = "NO_EXECUTION";
		}
		return result;
	}

}