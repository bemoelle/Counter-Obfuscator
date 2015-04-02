package edu.hm.counterobfuscator.refactor.modul;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.hm.counterobfuscator.helper.Validate;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.mapper.MapperElement;
import edu.hm.counterobfuscator.types.ForWhile;
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
		Mapper mapper = new Mapper(programmTree);
		this.mappedElements = mapper.process(TYPE.VARIABLE);
		this.mappedNames = new HashMap<String, String>();
	}

	/*
	 * TYPE.THIS, TYPE.CALL (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.refactor.IRefactor#process()
	 */
	public IProgrammTree process() {

		Validate.notNull(mappedElements);
		Validate.notNull(programmTree);

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement actualElement = mappedElements.get(i);

			String oldName = actualElement.getElement().getType().getName();

			// e.g. document.location don't rename such vars
			if (oldName.contains(".")) {
				continue;
			}

			// same variable
			if (!mappedNames.containsKey(oldName))
				mappedNames.put(oldName, varName + number++);
		}

		Iterator<Element> it = programmTree.iterator();

		while (it.hasNext()) {

			Element element = it.next();

			for (Map.Entry<String, String> entry : mappedNames.entrySet()) {

				call(element, entry);
			}

		}

		return programmTree;

	}

	private void call(Element element, Entry<String, String> entry) {

		if (element.getType().getType() == TYPE.FOR) {

			ForWhile loop = (ForWhile) element.getType();

			if (loop.getHeadString().contains(entry.getKey())) {

				System.out.println("sdsdsdsdsds: " + loop.getHeadString());
				loop.setHeadString(loop.getHeadString().replaceAll(entry.getKey(), entry.getValue()));
			}

		}

		String name = element.getType().getName();
		if (name.contains(entry.getKey())) {

			name = name.replace(entry.getKey(), entry.getValue());

			element.getType().setName(name);
		}

	}

}
