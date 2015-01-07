package edu.hm.counterobfuscator.interpreter;

import java.util.List;
import java.util.logging.Logger;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.IClient;
import edu.hm.counterobfuscator.parser.IJSParser;
import edu.hm.counterobfuscator.parser.token.trees.TypeTreeElement;
import edu.hm.counterobfuscator.types.AbstractType;
import edu.hm.counterobfuscator.types.ForWhile;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.types.TYPE;
import edu.hm.counterobfuscator.types.Variable;

/**
 * @author
 * @date 30.12.2014
 * 
 *       class to rename obfuscated names, replace them with the assigned value
 */
public class JSInterpreter implements IInterpreter {

	private IClient			client;
	private IJSParser			jsParser;
	private static Logger	log;
	private String				output;
	private String				jsScriptBuffer	= "";
	private List<TypeTreeElement>	treeElements;

	public JSInterpreter(IJSParser jsParser, IClient client) {

		JSInterpreter.log = Logger.getLogger(Function.class.getName());

		this.jsParser = jsParser;
		this.client = client;

		this.output = "";
	}

	private void processTreeElement(TypeTreeElement element) {

		callExecuteElement(element);

		if (element.hasChildren()) {

			for (TypeTreeElement child : element.getChildren()) {
				processTreeElement(child);
			}
			output += "END\n";
		}

	};

	public void callExecuteElement(TypeTreeElement element) {

		switch (element.getType().getType()) {
		case FUNCTION:
			executeFunction(element);
			break;
		case VARIABLE:
			executeVariable(element);
			break;
		case FOR:
			executeFor(element);
			break;
		default:

		}

	}

	public void executeFunction(TypeTreeElement element) {

		Function func = ((Function) element.getType());
		if (func.isPacked()) {
			// execute
			element.removeAllChildren();
		}
		else {
			output += "function " + func.getName() + func.getHeadString() + " {\n";
		}

	}

	public void executeVariable(TypeTreeElement element) {

		Variable var = ((Variable) element.getType());

		Object result = executeJS(var.getValue());

		var.setValue(result.toString());

		if (!var.isGlobal()) {
			jsScriptBuffer += "var ";
			output += "var ";
		}

		jsScriptBuffer += var.getName() + "=" + result + ";";
		output += var.getName() + "=" + result + ";\n";

	}

	public void executeFor(TypeTreeElement element) {

		ForWhile forWhile = ((ForWhile) element.getType());

		output += forWhile.getName() + forWhile.getHeadString() + " {\n";

	}

	private Object executeJS(String script) {

		Object result = null;
		try {
			result = client.getJSResult(jsScriptBuffer + script);
		}
		catch (Exception e) {
			result = script;
		}

		return result;
	}

	public void process() throws ScriptException {

		log.info("start renaming process...");

		treeElements = jsParser.getProgrammTree();

		for (TypeTreeElement actualElement : treeElements) {

			processTreeElement(actualElement);

		}

		System.out.println(output);

		log.info("finished renaming process");

	}


}
