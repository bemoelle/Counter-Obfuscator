package edu.hm.counterobfuscator.refactor.modul;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.helper.Validate;
import edu.hm.counterobfuscator.mapper.Mapper;
import edu.hm.counterobfuscator.mapper.MapperElement;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.ValueExtractor;
import edu.hm.counterobfuscator.types.TYPE;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 22.01.2015
 * 
 * 
 */
public class VariableRenamer implements IModul {

	private IProgrammTree			programmTree;
	private String						varName	= "var";
	private int							number	= 1;
	private List<MapperElement>	mappedElements;

	private Map<String, String>	mappedNames;

	/**
	 * @param programmTree
	 * @param setting
	 */
	public VariableRenamer(IProgrammTree programmTree) {

		this.programmTree = programmTree;

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
	public IProgrammTree process() {

		Validate.notNull(mappedElements);
		Validate.notNull(programmTree);

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement actualElement = mappedElements.get(i);

			Position actualScope = actualElement.getScope();

			String oldName = ValueExtractor.getName(actualElement.getElement());

			// same variable
			if (!mappedNames.containsKey(oldName))
				mappedNames.put(oldName, varName + number++);
		}

		for (int k = 0; k < programmTree.size(); k++) {

			Element element = programmTree.get(k);

			for (Map.Entry<String, String> entry : mappedNames.entrySet()) {

				call(element, entry);
			}

		}

		return programmTree;

	}

	private void call(Element element, Entry<String, String> entry) {

		String name = ValueExtractor.getName(element);
		if (name.contains(entry.getKey())) {
			ValueExtractor.setName(element, entry.getValue());
		}

		String value = "";

		switch (element.getType().getType()) {
		case VARIABLE:
			value = ValueExtractor.getValue(element);
			break;
		case FUNCTION:
			break;
		case CALL:
			break;
		case DEFAULT:
			break;
		case FOR:
			break;
		case RETURN:

			break;
		default:
		}

	}

}
