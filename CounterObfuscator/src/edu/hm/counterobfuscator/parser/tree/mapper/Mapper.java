package edu.hm.counterobfuscator.parser.tree.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.hm.counterobfuscator.definitions.DEFINITION;
import edu.hm.counterobfuscator.helper.Scope;
import edu.hm.counterobfuscator.helper.Validate;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.Element;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 09.01.2015
 * 
 *       class to map VARs in actual programmTree, makes it easier to track
 *       these, e.g. for renaming, ... creates an overview of all VARs and the
 *       scope of each VAR in the programmTree
 * 
 */
public class Mapper {

	private IProgrammTree programmTree;

	public Mapper(IProgrammTree programmTree) {
		
		Validate.notNull(programmTree);
		
		this.programmTree = programmTree;
	}

	/**
	 * @param typeSearchFor
	 * @param programmTree
	 * @return a List of mapped Elements
	 */
	public List<MapperElement> process(DEFINITION... typeSearchFor) {
		
		Validate.notNull(programmTree);
		Validate.notNull(typeSearchFor);

		List<MapperElement> mappedElements = new ArrayList<MapperElement>();

		Iterator<Element> it = programmTree.iterator();

		while (it.hasNext()) {

			Element element = it.next();
			if (isSearchedType(typeSearchFor, element.getType().getType())) {

				Scope scope = null;

				Element parent = element.getParent();
				if (parent != null) {
					scope = new Scope(element.getType().getPos().getStartPos(), parent.getType()
							.getPos().getEndPos());
				} else {
					// -1 element has scope until EOF
					scope = new Scope(element.getType().getPos().getStartPos(), 100000);
				}

				mappedElements.add(new MapperElement(scope, element));
			}
		}
		return testOfReAssign(mappedElements);
	}

	private boolean isSearchedType(DEFINITION[] typeSearchFor, DEFINITION typeToTest) {
		
		for (DEFINITION test : typeSearchFor) {

			if (test == typeToTest)
				return true;
		}
		return false;
	}

	/**
	 * 
	 * to test the scope of each element, it is possible an element has to be
	 * reassinged. in this case, the scope have to be changed of this element
	 * 
	 */
	private List<MapperElement> testOfReAssign(List<MapperElement> mappedElements) {
		
		Validate.notNull(mappedElements);

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement me = mappedElements.get(i);

			for (int j = i + 1; j < mappedElements.size(); j++) {

				MapperElement me2 = mappedElements.get(j);
				if (me.getElement().getType().hasSameName(me2.getElement().getType())) {

					me.setScope(new Scope(me.getScope().getStartPos(),
							me2.getScope().getStartPos() - 1));
				}
			}
		}
		
		return mappedElements;
	}
	
	/**
	 * @param elementToTest
	 * @param scope
	 * @return
	 */
	public List<MapperElement> searchForNameOfElement(String name,
			Scope scope) {
		
		Validate.notNull(programmTree);
		Validate.notNull(name);
		Validate.notNull(scope);

		List<MapperElement> elements = new ArrayList<MapperElement>();

		Iterator<Element> it = programmTree.iterator();

		while (it.hasNext()) {

			Element actualElement = it.next();

			if (scope.isPosWithin(actualElement.getType().getPos())) {

				if (actualElement.getType().hasNameInIt(name))
					elements.add(new MapperElement(null, actualElement));
			}
		}
		return elements;
	}

	/**
	 * @param oldName
	 * @return
	 */
	public List<MapperElement> searchForName(String oldName) {
		
		Validate.notNull(programmTree);
		Validate.notNull(oldName);

		List<MapperElement> elements = new ArrayList<MapperElement>();

		Iterator<Element> it = programmTree.iterator();

		while (it.hasNext()) {

			Element actualElement = it.next();

			if (actualElement.getType().hasNameInIt(oldName))
				elements.add(new MapperElement(null, actualElement));
		}
		return elements;
	}
}
