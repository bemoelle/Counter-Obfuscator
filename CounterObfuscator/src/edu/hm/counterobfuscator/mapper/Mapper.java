package edu.hm.counterobfuscator.mapper;

import java.util.ArrayList;
import java.util.List;

import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.parser.tree.ITypeTree;
import edu.hm.counterobfuscator.parser.tree.TypeTreeElement;
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

	private List<MapperElement>	mappedElements;
	private TYPE						typeSearchFor;
	private ITypeTree					programmTree;

	public Mapper(TYPE typeSearchFor, ITypeTree programmTree) {

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

	/**
	 * @param tree
	 * 
	 *           recursive iteration of an @ITypeTree to collect all elements
	 *           which type we are searching for.
	 * 
	 */
	private void callElement(ITypeTree tree) {

		for (int i = 0, positionInList = 0; i < tree.size(); i++) {

			TypeTreeElement element = tree.get(i);
			if (element.getType().getType() == typeSearchFor) {

				Position scope = null;

				TypeTreeElement parent = element.getParent();
				if (parent != null) {
					scope = new Position(element.getType().getPos().getStartPos(), parent.getType()
							.getPos().getEndPos());
				}
				else {
					// -1 element has scope until EOF
					scope = new Position(element.getType().getPos().getStartPos(), -1);
				}

				mappedElements.add(new MapperElement(positionInList++, scope, element));
			}

			if (element.hasChildren()) {
				callElement(element.getChildren());
			}
		}
	}

	/**
	 * @param mapperElement
	 * @return
	 */
	public boolean mappedElementHasReassign(MapperElement mapperElement) {

		if (mapperElement.getPositionInList() + 1 < mappedElements.size()) {
			return false;
		}

		String nameToTest = mapperElement.getElement().getType().getName();

		for (int i = mapperElement.getPositionInList() + 1; i < mappedElements.size(); i++) {

			String actualNameOfElement = mappedElements.get(i).getElement().getType().getName();

			if (nameToTest.equals(actualNameOfElement)) {
				return true;
			}

		}
		return false;
	}

	/**
	 * TODO REFACTOR
	 */
	private void testOfReAssign() {

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement me = mappedElements.get(i);

			for (int j = i + 1; j < mappedElements.size(); j++) {

				MapperElement me2 = mappedElements.get(j);
				if (me.getElement().getType().getName().equals(me2.getElement().getType().getName())) {
					me.setScope(new Position(me.getScope().getStartPos(),
							me.getScope().getStartPos() - 1));
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
					+ me.getScope().getStartPos() + ":" + me.getScope().getEndPos());
		}
	}

	/**
	 * @return a List<> of mapped Elements in the programm Tree which has the
	 *         same type
	 */
	public List<MapperElement> getMappedVars() {

		return mappedElements;
	}

}
