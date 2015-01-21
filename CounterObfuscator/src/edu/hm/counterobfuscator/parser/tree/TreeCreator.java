package edu.hm.counterobfuscator.parser.tree;

import java.util.List;

import edu.hm.counterobfuscator.types.AbstractType;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 09.01.2015
 *
 */
public final class TreeCreator {

	private static IProgrammTree programmTree;

	/**
	 * 
	 */
	public TreeCreator() {
		//noting to do here
	}

	/**
	 * @param allTypes
	 * @return
	 */
	public static IProgrammTree createTypeTree(List<AbstractType> allTypes) {

		int highestEndPos = -1;
		Element parent = null;
		programmTree = new ProgrammTree();
		
		for (AbstractType actualType : allTypes) {

			int startPos = actualType.getPos().getStartPos();
			int endPos = actualType.getPos().getEndPos();
			if (programmTree.isEmpty() || startPos > highestEndPos) {

				Element tte = new Element(null, actualType);
				programmTree.add(tte);
				highestEndPos = endPos;
				parent = tte;
			} else {
				findPositionForChild(parent, actualType);
			}
		}
		return programmTree;
	}

	/**
	 * @param parent
	 * @param child
	 */
	private static void findPositionForChild(Element parent, AbstractType child) {

		if (!parent.hasChildren()) {
			parent.addChild(new Element(parent, child));
		} else {
			int startPos = child.getPos().getStartPos();

			Element latestChild = parent.getLatestChild();

			if (startPos > latestChild.getType().getPos().getEndPos()) {
				parent.addChild(new Element(parent, child));
			} else {
				findPositionForChild(latestChild, child);
			}
		}
	}

}
