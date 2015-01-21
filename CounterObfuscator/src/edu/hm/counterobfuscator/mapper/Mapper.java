package edu.hm.counterobfuscator.mapper;

import java.util.ArrayList;
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
public class Mapper implements IMapper {

	private List<MapperElement> mappedElements;
	private TYPE[] typeSearchFor;
	private IProgrammTree programmTree;

	public Mapper(IProgrammTree programmTree, TYPE... typeSearchFor) {

		this.typeSearchFor = typeSearchFor;
		this.programmTree = programmTree;
		this.mappedElements = new ArrayList<MapperElement>();
	}

	/**
	 * @param typeSearchFor
	 * @param programmTree
	 * @return a List of mapped Elements
	 */
	public void process() {

		callElement(programmTree);
		testOfReAssign();
	}

	private boolean isSearchedType(TYPE typeToTest) {

		for (TYPE test : typeSearchFor) {

			if (test == typeToTest)
				return true;
		}
		return false;
	}

	/**
	 * @param tree
	 * 
	 *            recursive iteration of an @ITypeTree to collect all elements
	 *            which type we are searching for.
	 * 
	 */
	private void callElement(IProgrammTree tree) {

		for (int i = 0, positionInList = 0; i < tree.size(); i++) {

			Element element = tree.get(i);
			if (isSearchedType(element.getType().getType())) {

				Position scope = null;

				Element parent = element.getParent();
				if (parent != null) {
					scope = new Position(element.getType().getPos()
							.getStartPos(), parent.getType().getPos()
							.getEndPos());
				} else {
					// -1 element has scope until EOF
					scope = new Position(element.getType().getPos()
							.getStartPos(), element.getType().getPos()
							.getEndPos());
				}

				mappedElements.add(new MapperElement(positionInList++, scope,
						element));
			}

			if (element.hasChildren()) {
				callElement(element.getChildren());
			}
		}
	}

	/**
	 * TODO REFACTOR
	 *  
	 * to test the scope of each element, it is possible an element has to be
	 * reassinged. in this case, the scope have to be changed of this element
	 * 
	 */
	private void testOfReAssign() {

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement me = mappedElements.get(i);

			for (int j = i + 1; j < mappedElements.size(); j++) {

				MapperElement me2 = mappedElements.get(j);
				if (me.getElement().getType().getName()
						.equals(me2.getElement().getType().getName())) {
					me.setScope(new Position(me.getScope().getStartPos(), me2
							.getScope().getStartPos() - 1));
				}

			}

		}

	}

	/**
	 * methode to print all mapped Elements on System.out.
	 */
	public void print() {

		for (MapperElement me : mappedElements) {
			System.out.println(me.getElement().getType().getName() + "-->"
					+ me.getScope().getStartPos() + ":"
					+ me.getScope().getEndPos());
		}
	}

	/**
	 * @return a List of mapped Elements in the programm Tree which have the
	 *         type we are looking for
	 */
	public List<MapperElement> getMappedElements() {

		return mappedElements;
	}

}
