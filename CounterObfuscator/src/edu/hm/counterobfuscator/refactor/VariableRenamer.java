package edu.hm.counterobfuscator.refactor;

import java.util.ArrayList;
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
import edu.hm.counterobfuscator.parser.tree.ValueExtractor;
import edu.hm.counterobfuscator.types.AbstractType;
import edu.hm.counterobfuscator.types.Default;
import edu.hm.counterobfuscator.types.Call;
import edu.hm.counterobfuscator.types.TYPE;
import edu.hm.counterobfuscator.types.Variable;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 22.01.2015
 * 
 * 
 */
public class VariableRenamer implements IRefactor {

	private IProgrammTree			programmTree;
	private String						varName	= "var";
	private int							number	= 1;
	private List<MapperElement>	mappedElements;
	private Setting					setting;
	private Map<String, String>	mappedNames;

	/**
	 * @param programmTree
	 * @param setting
	 */
	public VariableRenamer(IProgrammTree programmTree, Setting setting) {

		this.programmTree = programmTree;
		this.setting = setting;

		// TODO refactor to Factory
		Mapper mapper = new Mapper(programmTree, TYPE.VARIABLE);
		mapper.process();
		this.mappedElements = mapper.getMappedElements();
		// ------------

		this.mappedNames = new HashMap<String, String>();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.refactor.IRefactor#process()
	 */
	public IProgrammTree process() throws ScriptException {

		renameVars();

		return programmTree;
	}

	/**
	 * @param actualScope
	 * @param pos
	 * @return
	 */
	private boolean isInScope(Position actualScope, Position pos) {

		if (actualScope.getStartPos() <= pos.getStartPos()
				&& pos.getStartPos() < actualScope.getEndPos())
			return true;
		return false;
	}

	/**
	 * @param mappedNames
	 * @return
	 */
	private List<String> isStringInMap(String stringToTest) {

		List<String> values = new ArrayList<String>();
		for (String key : mappedNames.keySet()) {
			if (stringToTest.indexOf(key) > -1) {
				values.add(key);
			}
		}

		return values;
	}

	private void renameVars() {

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement actualElement = mappedElements.get(i);

			Position actualScope = actualElement.getScope();

			String oldName = ValueExtractor.getName(actualElement.getElement());
		

			for (int k = 0; k < programmTree.size(); k++) {

				Element element = programmTree.get(k);

				if (isInScope(actualScope, element.getType().getPos())) {

					String name = ValueExtractor.getName(element);
					if(isStringInMap(name).size() > 0) {
						
					} else {
						String newName = "var"+number;
						mappedNames.put(oldName, newName);
						ValueExtractor.setName(element, newName);
					}
					
					String value = ValueExtractor.getValue(element);
					if (value.indexOf(oldName) > -1) {
						String toReplace = ValueExtractor.getValue(actualElement.getElement());
						String test = value.replaceAll(oldName, toReplace);
						ValueExtractor.setValue(element, test);
					}
				}

			}

		}

	}

	// // TODO change operations to work on mapped elements instead of original
	// // programmtree
	//
	// public void process() throws ScriptException {
	//
	// System.out.println("start...............");
	// programmTree.print(true);
	// replaceVars();
	// System.out.println("after process ............................");
	// programmTree.print(true);
	// removeVars();
	// System.out.println("after remove............................");
	// programmTree.print(true);
	// rename();
	// System.out.println("after rename............................");
	// programmTree.print(true);
	// }
	//
	// // TODO REFACTOR: move to typetreeelement
	// /**
	// * @param elementToTest
	// * @param actualElement
	// * @return
	// */
	// private boolean isInScopeOf(MapperElement mappedElement, MapperElement
	// elementToTest) {
	//
	// Position pos1 = mappedElement.getScope();
	// Position pos2 = elementToTest.getElement().getType().getPos();
	//
	// if (pos1.getStartPos() < pos2.getStartPos() && pos2.getStartPos() <
	// pos1.getEndPos()) {
	// return true;
	// }
	//
	// return false;
	// }
	//
	// /**
	// * @param programmTree
	// * @param me
	// */
	// private void replaceVars() {
	//
	// for (int i = 0; i < mapperElements.size(); i++) {
	//
	// MapperElement me = mapperElements.get(i);
	//
	// for (int j = i + 1; j < mapperElements.size(); j++) {
	//
	// MapperElement me2 = mapperElements.get(j);
	//
	// // TODO gleiches element
	// // name = name + 12233; ??!?
	// // test ob value �berhaupt ausfuehrbar ist -> wenn nicht muss
	// // diese
	// // nicht ersetzt werden.
	// if (isInScopeOf(me, me2)) {
	//
	// Variable var = (Variable) me2.getElement().getType();
	// Variable var2 = (Variable) me.getElement().getType();
	//
	// String nameToTest = var2.getName();
	// String value = var.getValue();
	//
	// if (value.contains(nameToTest)) {
	//
	// String valueToTest = var2.getValue();
	//
	// if (valueToTest.length() > 20) {
	// continue;
	// }
	//
	// value = value.replaceAll(nameToTest, valueToTest);
	//
	// var.setValue(value);
	// }
	// }
	// }
	// }
	// }
	//
	// /**
	// * @param programmTree
	// */
	// public void removeVars() {
	//
	// // ITypeTree reverseFlat = programmTree.reverseOrder();
	// //
	// // for (int i = 0; i < mapper.getMappedElements().size(); i++) {
	// //
	// // MapperElement me = mapper.getMappedElements().get(i);
	// // String nameLookingFor = me.getElement().getType().getName();
	// //
	// // int refCounter = 0;
	// // for (int j = 0; j < reverseFlat.size(); j++) {
	// //
	// // TypeTreeElement actualElement = reverseFlat.get(j);
	// //
	// // if (actualElement.getType().getType() == TYPE.VARIABLE) {
	// // Variable var = (Variable) actualElement.getType();
	// //
	// // if (var.getValue().indexOf(nameLookingFor) > -1) {
	// // refCounter++;
	// // }
	// //
	// // if (var.getName().indexOf(nameLookingFor) > -1) {
	// // if (var.getNoExe())
	// // refCounter++;
	// // if (refCounter == 0) {
	// //
	// // reverseFlat.removeElement(actualElement);
	// // }
	// // }
	// // } else if (actualElement.getType().getType() == TYPE.FUNCTIONCALL) {
	// // FunctionCall functionCall = (FunctionCall) actualElement
	// // .getType();
	// // String name = functionCall.getName();
	// // String parameter = functionCall.getValue();
	// //
	// // if (name.indexOf(nameLookingFor) > -1
	// // || parameter.indexOf(nameLookingFor) > -1) {
	// // refCounter++;
	// // }
	// // }
	// // }
	// // }
	//
	// // programmTree = reverseFlat.reverseOrder();
	// }
	//
	// private List<String> isStringInMap(Map<String, String> mappedNames, String
	// stringToTest) {
	//
	// List<String> values = new ArrayList<String>();
	// for (String key : mappedNames.keySet()) {
	// if (stringToTest.indexOf(key) > -1) {
	// values.add(key);
	// }
	// }
	//
	// return values;
	// }
	//
	// private void rename() {
	//
	// for (int i = 0; i < mapperElements.size(); i++) {
	//
	// // AbstractType type = mapperElements.get(i).getElement().getType();
	//
	// MapperElement me = mapperElements.get(i);
	// String oldName = me.getElement().getType().getName();
	// String newName = varName + number++;
	// if (me.getElement().getType().getType() == TYPE.VARIABLE)
	// me.getElement().getType().setName(newName);
	//
	// for (int j = i + 1; j < mapperElements.size(); j++) {
	//
	// AbstractType type = mapperElements.get(j).getElement().getType();
	//
	// if (type.getType() == TYPE.VARIABLE) {
	// renameVar(type, oldName, newName);
	// }
	// else if (type.getType() == TYPE.FUNCTIONCALL) {
	// renameFunctionCall(type, oldName, newName);
	// }
	// }
	//
	// }
	//
	// }

	/**
	 * @param type
	 * 
	 *           before each FunctionCall a variable with the same
	 * @param oldName
	 * @param newName
	 */
	private void renameFunctionCall(AbstractType type, String oldName, String newName) {

		Call fc = (Call) type;

		String nerere = fc.getName().replaceAll(oldName, newName);
		fc.setName(nerere);

	}

	/**
	 * @param type
	 * @param newName
	 * @param oldName2
	 * 
	 */
	private void renameVar(AbstractType type, String oldName, String newName) {

		Variable var = (Variable) type;

		var.setValue(var.getValue().replaceAll(oldName, newName));

	}
}
