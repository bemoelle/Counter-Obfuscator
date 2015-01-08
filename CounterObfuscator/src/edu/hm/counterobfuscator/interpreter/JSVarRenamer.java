package edu.hm.counterobfuscator.interpreter;

import java.util.List;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.parser.token.trees.ITypeTree;
import edu.hm.counterobfuscator.parser.token.trees.TypeTreeElement;
import edu.hm.counterobfuscator.types.AbstractType;
import edu.hm.counterobfuscator.types.ForWhile;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.types.TYPE;

public class JSVarRenamer implements IInterpreter {

	private ITypeTree programmTree;
	private String var = "var";
	private int number = 1;

	public JSVarRenamer(ITypeTree programmTree) {
		this.programmTree = programmTree;

	}

	public ITypeTree process() throws ScriptException {

		processTreeElement(programmTree);
		
		return null;

	}

	private void processTreeElement(List<TypeTreeElement> tree) {

		for (TypeTreeElement element : tree) {

			AbstractType type = element.getType();

			if (element.hasChildren()) {

				processTreeElement(element.getChildren());
			}

		}

	}

}
