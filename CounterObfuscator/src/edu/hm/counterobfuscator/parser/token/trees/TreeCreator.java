package edu.hm.counterobfuscator.parser.token.trees;

import java.util.List;

import edu.hm.counterobfuscator.mapper.IMapper;
import edu.hm.counterobfuscator.types.AbstractType;

public final class TreeCreator {

	private static ITypeTree programmTree;

	public TreeCreator() {
		//noting to do here
	}

	public static ITypeTree createTypeTree(List<AbstractType> allTypes) {

		int highestEndPos = -1;
		TypeTreeElement parent = null;
		programmTree = new TypeTree();
		
		for (AbstractType actualType : allTypes) {

			int startPos = actualType.getPos().getStartPos();
			int endPos = actualType.getPos().getEndPos();
			if (programmTree.isEmpty() || startPos > highestEndPos) {

				TypeTreeElement tte = new TypeTreeElement(parent, actualType);
				programmTree.add(tte);
				highestEndPos = endPos;
				parent = tte;
			} else {
				findPositionForChild(parent, actualType);
			}
		}
		return programmTree;
	}

	private static void findPositionForChild(TypeTreeElement parent, AbstractType child) {

		if (!parent.hasChildren()) {
			parent.addChild(new TypeTreeElement(parent, child));
		} else {
			int startPos = child.getPos().getStartPos();

			TypeTreeElement latestChild = parent.getLatestChild();

			if (startPos > latestChild.getType().getPos().getEndPos()) {
				parent.addChild(new TypeTreeElement(parent, child));
			} else {
				findPositionForChild(latestChild, child);
			}
		}
	}

}
