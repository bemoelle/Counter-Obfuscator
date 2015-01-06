package edu.hm.counterobfuscator.interpreter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import edu.hm.counterobfuscator.HTMLUnitClient;
import edu.hm.counterobfuscator.IClient;
import edu.hm.counterobfuscator.parser.IJSParser;
import edu.hm.counterobfuscator.parser.token.TOKENTYPE;
import edu.hm.counterobfuscator.parser.token.Token;
import edu.hm.counterobfuscator.types.AbstractType;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.types.IType;
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

	public JSInterpreter(IJSParser jsParser, IClient client) {

		JSInterpreter.log = Logger.getLogger(Function.class.getName());

		this.jsParser = jsParser;
		this.client = client;
		
		this.output = "";
	}

	public void process() throws ScriptException {

		log.info("start renaming process...");

		List<AbstractType> types = jsParser.getAlltypes();
		
		String jsScriptBuffer = "";
		String script = "";
		
		for(AbstractType actualType: types) {
			
			if(actualType.getType() == TYPE.FUNCTION) {
				
				Function func = ((Function)actualType);
				if(func.isPacked()) {
					//TODO is function packed?!
				} else {
			//		output += "function " + func.getName() + func.getHead() + " {\n";
				}

			} else if(actualType.getType() == TYPE.VARIABLE) {
				Variable var = ((Variable)actualType);
				
				//variable has an value
				if(!var.getValue().equals("undefined")) {
					
					String name = var.getName();
					String value = var.getValue();
										
					Object result = client.getJSResult(jsScriptBuffer + value);
					
					jsScriptBuffer += "var " + name + "=" + result + ";";
					output += "var " + name + " = " + result + ";\n";
				}
				
			}
			
			
		}
		
		
		System.out.println(output);
		

//		for (Variable v : vars) {
//
//			v.print();
//
//			String name = v.getName();
//			String value = v.getValue();
		
//		ScriptEngineManager manager = new ScriptEngineManager ();
//      ScriptEngine engine = manager.getEngineByName ("js");
//	
		
		//		Object result = client.executeJS(jsScriptBuffer+script);

		

//			List<Integer> listOfTokens = jsParser.getAllPosOfTokensByValue(name);
//
//			// for (int pos : listOfTokens) {
//			//
//			// Token actualToken = tokens.get(pos);
//			// if (actualToken.getPos() != v.getStartPos()) {
//			// actualToken.setValue("");
//			// }
//			// }
//			System.out.println(name + "--" + name + "--" + value);
//		}

		log.info("finished renaming process");
		
		//System.out.println(result);

	}

	private void renameVars() {

		log.info("start renaming vars...");

	}

}
