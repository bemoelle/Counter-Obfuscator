package edu.hm.counterobfuscator.parser.tree.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.types.TYPE;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 09.01.2015
 * 
 *       class to map VARs in actual programmTree, makes it easier to track
 *       these, e.g. for renaming, ... creates an overview of all VARs and the
 *       scope of each VAR in the programmTree
 * 
 */
public final class Mapper {

	private static TYPE[]					typeSearchFor;
	private static List<MapperElement>	mappedElements;

	/**
	 * 
	 */
	private Mapper() {

	}

	/**
	 * @param typeSearchFor
	 * @param programmTree
	 * @return a List of mapped Elements
	 */
	public static List<MapperElement> process(IProgrammTree programmTree, TYPE... typeSearchForX) {

		typeSearchFor = typeSearchForX;
		mappedElements = new ArrayList<MapperElement>();

		Iterator<Element> it = programmTree.iterator();

		while (it.hasNext()) {

			Element element = it.next();
			if (isSearchedType(element.getType().getType())) {

				Position scope = null;

				Element parent = element.getParent();
				if (parent != null) {
					scope = new Position(element.getType().getPos().getStartPos(), parent.getType()
							.getPos().getEndPos());
				} else {
					// -1 element has scope until EOF
					scope = new Position(element.getType().getPos().getStartPos(), 100000);
				}

				mappedElements.add(new MapperElement(scope, element));
			}
		}
		testOfReAssign();

		return mappedElements;
	}

	private static boolean isSearchedType(TYPE typeToTest) {

		for (TYPE test : typeSearchFor) {

			if (test == typeToTest)
				return true;
		}
		return false;
	}

	/**
	 * TODO REFACTOR
	 * 
	 * to test the scope of each element, it is possible an element has to be
	 * reassinged. in this case, the scope have to be changed of this element
	 * 
	 */
	private static void testOfReAssign() {

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement me = mappedElements.get(i);

			for (int j = i + 1; j < mappedElements.size(); j++) {

				MapperElement me2 = mappedElements.get(j);
				if (me.getElement().getType().hasSameName(me2.getElement().getType())) {

					me.setScope(new Position(me.getScope().getStartPos(),
							me2.getScope().getStartPos() - 1));
				}
			}
		}
	}
}
