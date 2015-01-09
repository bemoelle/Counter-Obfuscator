/**
 * 
 */
package edu.hm.counterobfuscator.interpreter;

import java.io.IOException;
import java.util.List;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.IClient;
import edu.hm.counterobfuscator.mapper.Mapper;
import edu.hm.counterobfuscator.mapper.MapperElement;
import edu.hm.counterobfuscator.parser.IJSParser;
import edu.hm.counterobfuscator.parser.token.trees.ITypeTree;
import edu.hm.counterobfuscator.types.TYPE;



/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 * 
 */
public class InterpreterFactory {

	/**
	 * @param String
	 *           input
	 * @return TokenAnalyser
	 * @throws IOException 
	 * @throws ScriptException 
	 */
	public static void create(IJSParser jsParser, IClient client) throws IOException, ScriptException {

		IInterpreter interpreter = new JSInterpreter(jsParser, client);
		ITypeTree programmTree = interpreter.process();
		programmTree.print();
		
		List<MapperElement> mappedVars = Mapper.process(TYPE.VARIABLE, programmTree);
		
		IInterpreter jsVarRenamer = new JSVarRenamer(programmTree, mappedVars);
		jsVarRenamer.process();
		
	}

}
