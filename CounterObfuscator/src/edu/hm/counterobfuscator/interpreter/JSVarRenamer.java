package edu.hm.counterobfuscator.interpreter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	private String		var			= "var";
	private int			number		= 1;
	private Position	actualScope	= null;
	private Mapper		mapper;
	private Setting	setting;

	public JSVarRenamer(ITypeTree programmTree, Setting setting) {
		this.programmTree = programmTree;
		this.setting = setting;

		// TODO Factory
		this.mapper = new Mapper(TYPE.VARIABLE, programmTree);
		this.mapper.process();
		// ------------
	}

	public void process() throws ScriptException {

		ITypeTree flatProgrammTree = programmTree.flatten();

		// TODO refactor
		for (int i = 0; i < mapper.getMappedVars().size(); i++) {

			MapperElement me = mapper.getMappedVars().get(i);
			actualScope = me.getScope();

			processTreeElement(flatProgrammTree, me);

		}

		removeVars(flatProgrammTree);

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

	// TODO REFACTOR: move in typetree, maybe :)
	/**
	 * @param programmTree
	 * @param me
	 */
	private void processTreeElement(ITypeTree programmTree, MapperElement me) {

		for (int i = 0; i < programmTree.size(); i++) {

			TypeTreeElement actualElement = programmTree.get(i);

			if (isInScopeOf(me, actualElement) && actualElement.getType().getType() == TYPE.VARIABLE) {

				Variable var = (Variable) actualElement.getType();
				Variable var2 = (Variable) me.getElement().getType();

				String nameToTest = var2.getName();
				String value = var.getValue();

				if (value.contains(nameToTest)) {

					String valueToTest = var2.getValue();

					value = value.replaceAll(nameToTest, valueToTest);

					var.setValue(value);
				}
			}

		}
	}

	public void removeVars(ITypeTree programmTree) {

		ITypeTree reverseFlat = programmTree.reverseOrder();

		for (int i = 0; i < programmTree.size(); i++) {
			
			
		}

	}

}
