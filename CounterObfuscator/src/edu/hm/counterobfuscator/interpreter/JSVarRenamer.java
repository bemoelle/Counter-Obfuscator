package edu.hm.counterobfuscator.interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.helper.Setting;
import edu.hm.counterobfuscator.mapper.Mapper;
import edu.hm.counterobfuscator.mapper.MapperElement;
import edu.hm.counterobfuscator.parser.tree.ITypeTree;
import edu.hm.counterobfuscator.parser.tree.TypeTreeElement;
import edu.hm.counterobfuscator.types.TYPE;
import edu.hm.counterobfuscator.types.Variable;

public class JSVarRenamer implements IInterpreter {

	private ITypeTree	programmTree;
	private String		varName	= "var";
	private int			number	= 1;
	private Mapper		mapper;
	private Setting	setting;

	public JSVarRenamer(ITypeTree programmTree, Setting setting) {
		this.programmTree = programmTree.flatten();
		this.setting = setting;

		// TODO refactor to Factory
		this.mapper = new Mapper(TYPE.VARIABLE, programmTree);
		this.mapper.process();
		// ------------
	}

	public void process() throws ScriptException {

		System.out.println("start...............");
		programmTree.print(true);
		replaceVars();
		System.out.println("after process ............................");
		programmTree.print(true);
		removeVars();
		System.out.println("after remove............................");
		programmTree.print(true);
		renameVars();
		System.out.println("after rename............................");
		programmTree.print(true);
	}

	// TODO REFACTOR: move to typetreeelement
	/**
	 * @param elementToTest
	 * @param actualElement
	 * @return
	 */
	private boolean isInScopeOf(MapperElement mappedElement, TypeTreeElement elementToTest) {

		Position pos1 = mappedElement.getScope();
		Position pos2 = elementToTest.getType().getPos();

		if (pos1.getStartPos() < pos2.getStartPos() && pos2.getStartPos() < pos1.getEndPos()) {
			return true;
		}

		return false;
	}

	/**
	 * @param programmTree
	 * @param me
	 */
	private void replaceVars() {

		for (int i = 0; i < mapper.getMappedVars().size(); i++) {

			MapperElement me = mapper.getMappedVars().get(i);

			for (int j = 0; j < programmTree.size(); j++) {

				TypeTreeElement actualElement = programmTree.get(j);

				// TODO gleiches element
				// name = name + 12233; ??!?
				// test ob value überhaupt ausfuehrbar ist -> wenn nicht muss diese
				// nicht ersetzt werden.
				if (actualElement.getType().getType() == TYPE.VARIABLE
						&& isInScopeOf(me, actualElement)) {

					Variable var = (Variable) actualElement.getType();
					Variable var2 = (Variable) me.getElement().getType();

					String nameToTest = var2.getName();
					String value = var.getValue();

					if (value.contains(nameToTest)) {

						String valueToTest = var2.getValue();

						if (valueToTest.length() > 20) {
							continue;
						}

						value = value.replaceAll(nameToTest, valueToTest);

						var.setValue(value);
					}
				}
			}
		}
	}

	/**
	 * @param programmTree
	 */
	public void removeVars() {

		ITypeTree reverseFlat = programmTree.reverseOrder();

		for (int i = 0; i < mapper.getMappedVars().size(); i++) {

			MapperElement me = mapper.getMappedVars().get(i);
			String nameLookingFor = me.getElement().getType().getName();

			int refCounter = 0;
			for (int j = 0; j < reverseFlat.size(); j++) {

				TypeTreeElement actualElement = reverseFlat.get(j);

				if (actualElement.getType().getType() == TYPE.VARIABLE) {
					Variable var = (Variable) actualElement.getType();

					if (var.getValue().indexOf(nameLookingFor) > -1) {
						refCounter++;
					}

					if (var.getName().indexOf(nameLookingFor) > -1) {
						if (var.getNoExe())
							refCounter++;
						if (refCounter == 0) {

							reverseFlat.removeElement(actualElement);
						}
					}
				}
			}
		}

		programmTree = reverseFlat.reverseOrder();
	}

	private List<String> isStringIsInMap(Map<String, String> mappedNames, String stringToTest) {

		List<String> values = new ArrayList<String>();
		for (String key : mappedNames.keySet()) {
			if (stringToTest.indexOf(key) > -1) {
				values.add(key);
			}
		}

		return values;
	}

	/**
	 * @param flatProgrammTree
	 */
	private void renameVars() {

		Map<String, String> mappedNames = new HashMap<String, String>();

		for (int i = 0; i < programmTree.size(); i++) {

			TypeTreeElement actualElement = programmTree.get(i);

			if (actualElement.getType().getType() == TYPE.VARIABLE) {
				Variable var = (Variable) actualElement.getType();

				String oldName = var.getName();

				if (mappedNames.containsKey(oldName)) {
					var.setName(mappedNames.get(oldName));
				}
				else {
					String newName = varName + number++;
					mappedNames.put(oldName, newName);
					var.setName(newName);
				}

				String value = var.getValue();
				List<String> returnValue = isStringIsInMap(mappedNames, value);
				if (returnValue != null) {

					for (String replaceValue : returnValue) {
						var.setValue(var.getValue().replaceAll(replaceValue,
								mappedNames.get(replaceValue)));
					}

				}

			}
		}

	}
}
