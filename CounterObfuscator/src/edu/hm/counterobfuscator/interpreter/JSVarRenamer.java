package edu.hm.counterobfuscator.interpreter;

import java.util.List;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.mapper.MapperElement;
import edu.hm.counterobfuscator.parser.token.trees.ITypeTree;
import edu.hm.counterobfuscator.parser.token.trees.TypeTreeElement;
import edu.hm.counterobfuscator.types.AbstractType;
import edu.hm.counterobfuscator.types.Variable;

public class JSVarRenamer implements IInterpreter {

	private ITypeTree programmTree;
	private String var = "var";
	private int number = 1;
	private List<MapperElement> mappedVars;
	private Position actualScope = null;

	public JSVarRenamer(ITypeTree programmTree, List<MapperElement> mappedVars) {
		this.programmTree = programmTree;
		this.mappedVars = mappedVars;

	}

	public ITypeTree process() throws ScriptException {

		Position globalScope = findGlobalScope();

		for (MapperElement me : mappedVars) {
			actualScope = me.getScope();

			processTreeElement(programmTree, me.getType());

		}

		return programmTree;

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

	private void processTreeElement(ITypeTree programmTree, AbstractType type) {

		for (int i = 0; i < programmTree.size(); i++) {

			TypeTreeElement element = programmTree.get(i);

			// TODO test if it is same element
			if (element.getType().getType() == type.getType()
					&& (element.getType().getPos().getStartPos() > actualScope
							.getStartPos() && element.getType().getPos()
							.getEndPos() < actualScope.getEndPos())) {

				String name = ((Variable) element.getType()).getName();
				String value = ((Variable) element.getType()).getValue();

				String nameToReplace = ((Variable) type).getName();
				String valueToReplace = ((Variable) type).getValue();

				name = name.replaceAll(nameToReplace, valueToReplace);
				value = value.replaceAll(nameToReplace, valueToReplace);

			}

			if (element.hasChildren()) {

				processTreeElement(element.getChildren(), type);
			}

		}

	}
}
