package edu.hm.counterobfuscator.interpreter;

import java.util.List;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.mapper.MapperElement;
import edu.hm.counterobfuscator.parser.tree.ITypeTree;
import edu.hm.counterobfuscator.parser.tree.TypeTreeElement;
import edu.hm.counterobfuscator.types.AbstractType;
import edu.hm.counterobfuscator.types.TYPE;
import edu.hm.counterobfuscator.types.Variable;

public class JSVarRenamer implements IInterpreter {

	private ITypeTree					programmTree;
	private String						var			= "var";
	private int							number		= 1;
	private List<MapperElement>	mappedVars;
	private Position					actualScope	= null;

	public JSVarRenamer(ITypeTree programmTree, List<MapperElement> mappedVars) {
		this.programmTree = programmTree;
		this.mappedVars = mappedVars;

	}

	public void process() throws ScriptException {

		Position globalScope = findGlobalScope();

		for (MapperElement me : mappedVars) {
			actualScope = me.getScope();

			processTreeElement(programmTree, me.getType());

		}
	}

	private Position findGlobalScope() {

		Position globalScope = new Position(0, 0);

		for (int i = 0; i < programmTree.size(); i++) {
			AbstractType element = programmTree.get(i).getType();

			if (element.getPos().getEndPos() > globalScope.getEndPos()) {
				globalScope.setEndPos(element.getPos().getEndPos());
			}
		}

		return globalScope;
	}

	private void processTreeElement(ITypeTree programmTree, AbstractType elementToTest) {

		for (int i = 0; i < programmTree.size(); i++) {

			TypeTreeElement actualElement = programmTree.get(i);

			if (actualElement.getType().getType() == TYPE.VARIABLE) {

				Variable var = (Variable) actualElement.getType();
				Variable var2 = (Variable) elementToTest;

				String nameToTest = var2.getName();
				String value = var.getValue();

				if (value.contains(nameToTest)) {

					String valueToTest = var2.getValue();

					value = value.replaceAll(nameToTest, valueToTest);

					var.setValue(value);

				}
			}

			if (actualElement.hasChildren()) {

				processTreeElement(actualElement.getChildren(), elementToTest);
			}

		}

	}
}
