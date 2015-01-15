package edu.hm.counterobfuscator.interpreter;

import java.util.logging.Logger;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.IClient;
import edu.hm.counterobfuscator.parser.tree.ITypeTree;
import edu.hm.counterobfuscator.parser.tree.TypeTreeElement;
import edu.hm.counterobfuscator.types.Default;
import edu.hm.counterobfuscator.types.ForWhile;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.types.TYPE;
import edu.hm.counterobfuscator.types.This;
import edu.hm.counterobfuscator.types.Variable;

/**
 * @author
 * @date 30.12.2014
 * 
 *       class to rename obfuscated names, replace them with the assigned value
 */
public class JSInterpreter implements IInterpreter {

	private IClient			client;
	private static Logger	log;
	private String				output;
	private String				jsScriptBuffer	= "";
	private ITypeTree			programmTree;

	public JSInterpreter(ITypeTree programmTree, IClient client) {

		JSInterpreter.log = Logger.getLogger(Function.class.getName());

		this.client = client;

		this.programmTree = programmTree;
		programmTree.print(false);
	}

	public void process() throws ScriptException {

		log.info("start renaming process...");

		for (int i = 0; i < programmTree.size(); i++) {

			processTreeElement(programmTree.get(i));

		}

		log.info("finished renaming process");
		
	}

	private String processTreeElement(TypeTreeElement element) {

		String result = callExecuteElement(element);

		if (element.hasChildren()) {
			String body = "";
			for (int i = 0; i < element.getChildren().size(); i++) {
				TypeTreeElement child = element.getChild(i);

				body += processTreeElement(child);
			}
			//output += " }\n";

			// TODO refactor
			if (element.getType().getType() == TYPE.FUNCTION) {
				((Function) element.getType()).setBody(body);
			}
			else if (element.getType().getType() == TYPE.FOR) {
				((ForWhile) element.getType()).setBody(body);
			}

			return result + body + "";
		}
		return result;
	};

	private String callExecuteElement(TypeTreeElement element) {

		switch (element.getType().getType()) {
		case FUNCTION:
			return executeFunction(element);
		case VARIABLE:
			return executeVariable(element);
		case FOR:
			return executeFor(element);
		case THIS:
			return executeThis(element);
		case DEFAULT:
			return executeDefault(element);
		default:

		}
		return "";

	}

	private String executeFunction(TypeTreeElement element) {

		Function func = ((Function) element.getType());
		if (func.isPacked()) {
			// TODO execute
			element.removeAllChildren();
		}
		else {
			output += "function " + func.getName() + func.getHeadString() + " {\n";
		}

		return "function " + func.getName() + func.getHeadString() + " {";
	}

	private String executeVariable(TypeTreeElement element) {

		Variable var = ((Variable) element.getType());

		Object result = executeJS(var.getValue());

		if(result.toString().indexOf("NOTEXE") > -1) {
			result = result.toString().substring(6);
			var.setNoExe(true);
		}
		
		var.setValue(result.toString());

		if (!var.isGlobal()) {
			jsScriptBuffer += "var ";
			output += "var ";
		}
		
		

		jsScriptBuffer += var.getName() + "=" + result + ";";
		output += var.getName() + "=" + result + ";\n";

		return var.getName() + "=" + result + ";";

	}

	private String executeFor(TypeTreeElement element) {

		ForWhile forWhile = ((ForWhile) element.getType());

		output += forWhile.getName() + forWhile.getHeadString() + " {\n";

		return forWhile.getName() + forWhile.getHeadString() + " {";

	}
	
	private String executeDefault(TypeTreeElement element) {

		Default defaultType = ((Default) element.getType());

		output += defaultType.getName() + ";\n";

		return defaultType.getName() + ";";
	}
	
	private String executeThis(TypeTreeElement element) {

		This type = ((This) element.getType());

		String name = type.getName();
		type.setName(executeJS(name)+"");

		return type.getName() + ";";
	}

	private Object executeJS(String script) {

		Object result = null;
		try {
			result = client.getJSResult(jsScriptBuffer + script);
		}
		catch (Exception e) {
			System.out.println("Exception:" + script);
			result = "NOTEXE" + script;
		}

		return result;
	}

}
