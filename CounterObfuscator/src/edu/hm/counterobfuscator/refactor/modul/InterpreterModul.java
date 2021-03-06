package edu.hm.counterobfuscator.refactor.modul;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.codec.EncoderException;

import edu.hm.counterobfuscator.client.IClient;
import edu.hm.counterobfuscator.definitions.AbstractType;
import edu.hm.counterobfuscator.definitions.Ajax;
import edu.hm.counterobfuscator.definitions.Call;
import edu.hm.counterobfuscator.definitions.Default;
import edu.hm.counterobfuscator.definitions.ForWhile;
import edu.hm.counterobfuscator.definitions.Function;
import edu.hm.counterobfuscator.definitions.This;
import edu.hm.counterobfuscator.definitions.Variable;
import edu.hm.counterobfuscator.helper.Scope;
import edu.hm.counterobfuscator.parser.ParserFactory;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.ProgrammTree;

/**
 * @author
 * @date 30.12.2014
 * 
 *       class to interprete javascript definitions in a given programm
 */
public class InterpreterModul {

	private IClient			client;
	private static Logger	log;
	private String				jsScriptBuffer	= "";
	private boolean			handleError;

	public InterpreterModul(IClient client, boolean handleError) {

		this.handleError = handleError;
		log = Logger.getLogger(InterpreterModul.class.getName());

		this.client = client;
	}

	/**
	 * @param element
	 * @return
	 * @throws Exception
	 */
	public IProgrammTree process(Element element) throws Exception {

		log.info("start javascript interpreter process...");

		return callExecuteElement(element);
	}

	/**
	 * @param buffer
	 */
	public void setJsScriptBuffer(String buffer) {

		jsScriptBuffer += buffer;
	}

	/**
	 * @param element
	 * @throws Exception
	 */
	private IProgrammTree callExecuteElement(Element element) throws Exception {

		switch (element.getDefinition().getDefinition()) {
		case FUNCTION:
			return executeFunction(element);
		case CALL:
			executeFunctionCall(element);
			break;
		case VARIABLE:
			executeVariable(element);
			break;
		case THIS:
			executeThis(element);
			break;
		case AJAX:
			executeAjax(element);
			break;
		default:

		}
		return null;
	}

	/**
	 * @param element
	 * 
	 *           Interprets ajax call
	 */
	private void executeAjax(Element element) {

		Ajax ajax = ((Ajax) element.getDefinition());

		String result = executeJS(ajax.getName() + ";").toString();

		if (result.indexOf("NO_EXECUTION") < 0) {

			result = result.replaceAll("'", "");
			ajax.setName(result);
		}

		result = executeJS(ajax.getValue()).toString();
		if (result.indexOf("NO_EXECUTION") < 0) {

			ajax.setValue(result);

		}

		String script = "$." + ajax.getName() + "" + "(" + ajax.getValue() + ")";

		result = executeJS(script).toString();

		log.info("result is " + result);

	}

	/**
	 * @param element
	 * @return result if a function is executable packed or called
	 * 
	 *         execute function if function is packed and return result
	 *         (function(a,b,c){return a+b+c;})(1,2,3) -> result is 6
	 * @throws EncoderException
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * 
	 */
	private IProgrammTree executeFunction(Element element) throws IllegalArgumentException,
			IOException, EncoderException {

		Function func = ((Function) element.getDefinition());
		IProgrammTree tree = null;
		if (func.isPacked()) {

			log.info("packed function found!");

			String script = func.getBodyAsString();
			
			if(func.getName().indexOf("eval") > -1) {
				
				script = "eval(" + script + ");";
			}
			
			Object result = executeJS(script);
			String resultAsString = result.toString();

			if (resultAsString.indexOf("NO_EXECUTION") < 0) {

				if (resultAsString.length() > 1) {
					resultAsString = resultAsString.substring(1, resultAsString.length() - 1);
				} else {
					resultAsString += ";";
				}
				tree = ParserFactory.create(resultAsString, false).getProgrammTree();
				element.removeAllChildren();
				log.info("result is " + result);

				return tree;
			} else {
				tree = new ProgrammTree();
				AbstractType defaultStatement = new Default(new Scope(0, 100), "undefined");
				tree.add(new Element(null, defaultStatement, 0));
			}
		} else {

			String script = "function " + func.getName() + "(" +func.getHeadString() + ")"
					+ func.getBodyAsString() + ";";
			
			setJsScriptBuffer(script);
			
		}

		return tree;
	}

	/**
	 * @param element
	 * @throws Exception
	 * 
	 *            Interprets function call
	 */
	private void executeFunctionCall(Element element) throws Exception {

		Call callStatement = ((Call) element.getDefinition());

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

		} else {
			if (!handleError)
				throw new Exception("bla");
		}

	}

	/**
	 * @param element
	 * @throws Exception
	 * 
	 *            Interprets var
	 */
	private void executeVariable(Element element) throws Exception {

		Variable var = ((Variable) element.getDefinition());

		String value = var.getValue();
		value = value.replace("\"", "\'");

		// is a asso Array: do nothing to avoid problems with the result of the
		// executeJs
		if (value.matches("\\{.*\\}")) {
			jsScriptBuffer += "var " + var.getName() + "=" + value + ";";
		} else {

			String resultValue = executeJS(var.getName() + "=" + value).toString();
			// statement is executable
			if (resultValue.indexOf("NO_EXECUTION") < 0) {

				var.setValue(resultValue);
				jsScriptBuffer += "var " + var.getName() + "=" + resultValue + ";";

			} else {
				if (!handleError) {
					throw new Exception("bla");
				}
			}

			if (var.isObject()) {
				String resultParameter = executeJS(var.getParameter()).toString();
				if (resultParameter.indexOf("NO_EXECUTION") < 0) {

					var.setParameter(resultParameter);
					var.setExecutable(false);

				} else {
					if (!handleError)
						throw new Exception("bla");
				}
			}
		}

	}

	/**
	 * @param element
	 * 
	 *           Interprets this in function
	 */
	private void executeThis(Element element) {

		This type = ((This) element.getDefinition());

		String name = type.getName();

		String result = executeJS(name).toString();

		if (result.indexOf("NO_EXECUTION") < 0) {

			type.setName(result);
		}

	}

	/**
	 * @param script
	 * @return
	 * 
	 *         execute JavaScript statements
	 */
	private Object executeJS(String script) {

		Object result = null;
		try {
			result = client.getJSResult(jsScriptBuffer + script);
			log.info("result of interpreter is: " + result);
		} catch (Exception e) {

			result = "NO_EXECUTION";
		}
		return result;
	}

}
