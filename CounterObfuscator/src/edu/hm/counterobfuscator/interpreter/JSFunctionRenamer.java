package edu.hm.counterobfuscator.interpreter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.helper.Setting;
import edu.hm.counterobfuscator.mapper.Mapper;
import edu.hm.counterobfuscator.mapper.MapperElement;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.types.Default;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.types.TYPE;
import edu.hm.counterobfuscator.types.Variable;

public class JSFunctionRenamer implements IInterpreter {

	private IProgrammTree					programmTree;
	private Setting					setting;
	private List<MapperElement>	mappedElements;
	private String						funcVarName	= "funcVar";
	private int							varNumber	= 1;
	private String						funcName		= "function";
	private int							funcNumber	= 1;

	public JSFunctionRenamer(IProgrammTree programmTree, Setting setting) {
		this.programmTree = programmTree.flatten();
		this.setting = setting;

		// TODO refactor to Factory
		Mapper mapper = new Mapper(programmTree, TYPE.FUNCTION);
		mapper.process();

		this.mappedElements = mapper.getMappedElements();

		// ------------
	}

	public void process() throws ScriptException {

		renameFunction();
	}

	private String isStringInMap(Map<String, String> mappedNames, String stringToTest) {

		for (String key : mappedNames.keySet()) {
			if (stringToTest.indexOf(key) > -1) {
				stringToTest = stringToTest.replaceAll(key, mappedNames.get(key));
			}
		}

		return stringToTest;
	}

	private void renameFunction() {

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement actualElement = mappedElements.get(i);
			Function func = (Function) actualElement.getElement().getType();

			Position actualScope = actualElement.getScope();

			// to map parameters
			Map<String, String> mappedNames = new HashMap<String, String>();

			for (int j = 0; j < func.getHead().size(); j++) {
				Variable var = func.getHead().get(j);
				String newName = funcVarName + varNumber++;
				mappedNames.put(var.getName(), newName);
				var.setName(newName);
			}

			if (!func.getName().equals("")) {
				func.setName(funcName + funcNumber++);
			}

			// TODO begin at scope
			for (int k = 0; k < programmTree.size(); k++) {
				Element type = programmTree.get(k);

				if (actualScope.getStartPos() < type.getType().getPos().getStartPos()
						&& type.getType().getPos().getStartPos() < actualScope.getEndPos()) {

					if (type.getType().getType() == TYPE.DEFAULT) {
						Default defaultType = (Default) type.getType();

						String test = defaultType.getName();
						defaultType.setName(isStringInMap(mappedNames, test));
					}

				}

			}

		}
	}

}
