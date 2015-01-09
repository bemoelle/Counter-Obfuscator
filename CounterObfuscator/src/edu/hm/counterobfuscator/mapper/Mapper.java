package edu.hm.counterobfuscator.mapper;

import java.util.ArrayList;
import java.util.List;

import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.parser.token.trees.ITypeTree;
import edu.hm.counterobfuscator.parser.token.trees.TypeTreeElement;
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
public final class Mapper<TypE>  {

	private static List<MapperElement> mappedElements;
	private static TYPE type;
	
	public Mapper() {
		//nothing to do

	}

	public static List<MapperElement> process(TYPE typeSearchFor, ITypeTree programmTree) {

		mappedElements = new ArrayList<MapperElement>();
		type = typeSearchFor;
		
		callElement(programmTree);
		return mappedElements;
	}

	private static void callElement(ITypeTree tree) {

		for (int i = 0; i < tree.size(); i++) {

			TypeTreeElement element = tree.get(i);
			if (element.getType().getType() == type) {

				Position scope = null;

				TypeTreeElement parent = element.getParent();
				if (parent != null) {
					scope = new Position(element.getType().getPos()
							.getStartPos(), parent.getType().getPos()
							.getEndPos());
				} else {
					// -1 element has scope until EOF
					scope = new Position(element.getType().getPos()
							.getStartPos(), -1);
				}

				mappedElements.add(new MapperElement(scope, element.getType()));
			}

			if (element.hasChildren()) {
				callElement(element.getChildren());
			}
		}
	}

	public void print() {

		for (MapperElement me : mappedElements) {
			System.out.println(me.getType().getName() + "-->"
					+ me.getScope().getStartPos() + ":"
					+ me.getScope().getEndPos());
		}
	}

}
