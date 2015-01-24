package edu.hm.counterobfuscator.refactor;

import java.util.logging.Logger;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.IClient;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.types.Default;
import edu.hm.counterobfuscator.types.ForWhile;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.types.Call;
import edu.hm.counterobfuscator.types.TYPE;
import edu.hm.counterobfuscator.types.This;
import edu.hm.counterobfuscator.types.Variable;

/**
 * @author
 * @date 30.12.2014
 * 
 *       class to rename obfuscated names, replace them with the assigned value
 */
public class Refactor implements IRefactor {

	private IClient			client;
	private static Logger	log;
	private String				jsScriptBuffer	= "";
	private IProgrammTree	flatProgrammTree;
	private IProgrammTree	programmTree;

	public Refactor(IProgrammTree programmTree, IClient client) {

		this.programmTree = programmTree;
		Refactor.log = Logger.getLogger(Function.class.getName());

		this.client = client;

		// create an flat structure of a programmtree
		this.flatProgrammTree = this.programmTree.flatten();
	}

	public IProgrammTree process() throws ScriptException {

		log.info("start javascript interpreter process...");

		for (int i = 0; i < flatProgrammTree.size(); i++) {

			callExecuteElement(flatProgrammTree.get(i));
		}

		log.info("finished javascript interpreter process");

		return flatProgrammTree;

	}

	/**
	 * @param element
	 */
	private void callExecuteElement(Element element) {

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
		case DEFAULT:
			executeDefault(element);
		default:

		}
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

			String script = "(function " + func.getName() + func.getHeadString()
					+ func.getBodyAsString() + ";";
			Object result = executeJS(script);
			element.removeAllChildren();
			func.setBodyAsString(result.toString());
			log.info("result is " + result);
		}

	}

	private void executeFunctionCall(Element element) {

		Call func = ((Call) element.getType());

		String resultValue = executeJS(func.getValue()).toString();
		if (resultValue.indexOf("NO_EXECUTION") < 0) {
			func.setValue(resultValue);
		}

		String resultParameter = executeJS(func.getFunction()).toString();
		if (resultParameter.indexOf("NO_EXECUTION") < 0) {
			func.setFunction(resultParameter);

		}
	}

	private void executeVariable(Element element) {

		Variable var = ((Variable) element.getType());

		String resultValue = executeJS(var.getValue()).toString();
		if (resultValue.indexOf("NO_EXECUTION") < 0) {
			var.setValue(resultValue);
			jsScriptBuffer += "var " + var.getName() + "=" + resultValue + ";";
		}  else {
			var.setExecutable(false);
		}

		if (var.isObject()) {
			String resultParameter = executeJS(var.getParameter()).toString();
			if (resultParameter.indexOf("NO_EXECUTION") < 0) {
				var.setParameter(resultParameter);
				var.setExecutable(false);
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
			System.out.println("Exception: " + e);
			result = "NO_EXECUTION";
		}

		return result;
	}

}
