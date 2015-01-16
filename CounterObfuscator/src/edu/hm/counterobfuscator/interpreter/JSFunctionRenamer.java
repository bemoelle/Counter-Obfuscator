package edu.hm.counterobfuscator.interpreter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.helper.Setting;
import edu.hm.counterobfuscator.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.ITypeTree;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.types.TYPE;
import edu.hm.counterobfuscator.types.Variable;

public class JSFunctionRenamer implements IInterpreter {

	private ITypeTree programmTree;
	private Setting setting;
	private Mapper mapper;
	private String funcVarName = "funcVar";
	private int varNumber = 1;
	private String funcName = "func";
	private int funcNumber = 1;

	public JSFunctionRenamer(ITypeTree programmTree, Setting setting) {
		this.programmTree = programmTree.flatten();
		this.setting = setting;

		// TODO refactor to Factory
		this.mapper = new Mapper(programmTree, TYPE.FUNCTION);
		this.mapper.process();
		// ------------
	}

	public void process() throws ScriptException {

		renameFunction();
	}

	
	private void renameFunction() {
		
		
		Map<String, String> mappedNames = new HashMap<String, String>();
		
		String test = mapper.getMappedElements().get(0).getElement().getType().getName();
		
		mapper.getMappedElements().get(0).getElement().getType().setName("test");

		for (int i = 0; i < programmTree.size(); i++) {
			
			if(programmTree.get(i).getType().getType() == TYPE.FUNCTION) {
				
				Function func = (Function)programmTree.get(i).getType();
				List<Variable> vars = func.getHead();
			}
			
		}
		
	}
}
