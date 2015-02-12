package edu.hm.counterobfuscator.parser.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.apache.commons.lang3.Validate;

import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.types.AbstractType;
import edu.hm.counterobfuscator.types.Ajax;
import edu.hm.counterobfuscator.types.Call;
import edu.hm.counterobfuscator.types.Default;
import edu.hm.counterobfuscator.types.ForWhile;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.types.Return;
import edu.hm.counterobfuscator.types.This;
import edu.hm.counterobfuscator.types.TryCatch;
import edu.hm.counterobfuscator.types.Variable;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 06.01.2015
 * 
 *       represent the struktur of a javascript programm
 */
public class ProgrammTree implements IProgrammTree, Iterable<Element> {

	private List<Element> tree;
	private AbstractType actualType;
	private static Logger log;

	public ProgrammTree() {

		this.tree = new ArrayList<Element>();
		log = Logger.getLogger(ProgrammTree.class.getName());
	}

	/**
	 * 
	 */
	public ProgrammTree(List<AbstractType> types) {

		this.tree = new ArrayList<Element>();

		for (AbstractType type : types) {

			actualType = type;
			findPositionInTree(this, null, 0);
		}

		log.info("programm tree creation is finished");
	}

	/**
	 * @param tree
	 * @param parent
	 * @param tiefe
	 * 
	 * rec method to find the correct position of actualtype in programmtree
	 */
	private void findPositionInTree(IProgrammTree tree, Element parent,
			int tiefe) {

		// tree is empty add element and leave
		if (tree.size() < 1) {
			tree.add(new Element(parent, actualType, tiefe));
		} else { // tree is not empty

			// get last insert element, it maybe the parent of the actual
			// element
			Element last = tree.getLast();

			// new element is within parent
			if (last != null
					&& last.getType().getPos().isPosWithin(actualType.getPos())) {

				// recursive step
				findPositionInTree(last.getChildren(), last, ++tiefe);

			} else { // is not within
				Element element = new Element(parent, actualType, tiefe);

				// verkettung wird bei der iteration durch den baum ben�tigt
				element.setBefore(last);
				last.setNext(element);
				// -----------------------------

				tree.add(element);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#isEmpty()
	 */
	public boolean isEmpty() {

		return tree.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#add(edu.hm.
	 * counterobfuscator.parser.token.trees.TypeTreeElement)
	 */
	public void add(Element element) {

		log.info("element with name: " + element.getType().getName()
				+ " is added to programm tree");

		if (size() > 0) {
			Element last = getLast();

			last.setNext(element);
			element.setBefore(last);
		}

		tree.add(element);
	}

	public void addAll(ProgrammTree other) {

		tree.addAll(other.tree);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.counterobfuscator.parser.tree.IProgrammTree#prettyPrint(boolean)
	 */
	public void print() {

		Iterator<Element> it = this.iterator();

		while (it.hasNext()) {

			Element element = it.next();

			String toPrint = call(element.getType());

			toPrint = tabPrint(element.getDepth()) + toPrint;

			System.out.println(toPrint);
		}
	}

	/**
	 * @param anz
	 * @return String
	 * 
	 * realize einrücken for print function
	 */
	private String tabPrint(int anz) {

		String tabs = "";

		for (int i = 0; i < anz; i++)
			tabs += tabs + "  ";

		return tabs;

	}

	/**
	 * @param abstractType
	 * @return
	 */
	private String call(AbstractType abstractType) {

		switch (abstractType.getType()) {

		case FUNCTION:
			Function func = (Function) abstractType;
			return "function " + func.getName() + "(" + func.getHeadString()
					+ ") " + "{\n";
		case CALL:
			Call fc = (Call) abstractType;
			return fc.getName() + "." + fc.getFunction() + "(" + fc.getValue()
					+ ");\n";
		case VARIABLE:
			Variable var = (Variable) abstractType;
			return (var.isGlobal() ? "" : "var ") + var.getName() + "="
					+ (var.isObject() ? "new " : "") + var.getValue()
					+ (var.isObject() ? "(" + var.getParameter() + ")" : "")
					+ ";\n";
		case FOR:
		case WHILE:
			ForWhile loop = (ForWhile) abstractType;
			return loop.getName() + loop.getHeadString() + "{\n";
		case THIS:
			This thisStatement = (This) abstractType;
			return "this" + thisStatement.getName() + "=";
		case RETURN:
			Return returnStatement = (Return) abstractType;
			return "return " + returnStatement.getName() + ";\n";
		case DEFAULT:
			Default defaultStatement = (Default) abstractType;
			return defaultStatement.getName() + ";\n";
		case TRYCATCH:
			TryCatch tryCatchStatement = (TryCatch) abstractType;
			return tryCatchStatement.getName().equals("try") ? "try {\n"
					: "} catch(e) {\n";
		case AJAX:
			Ajax ajax = (Ajax) abstractType;
			return "$." + ajax.getName() + "(" + ajax.getValue() + ");\n";
		default:
			break;
		}
		return "";

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#size()
	 */
	public int size() {

		return tree.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#get(int)
	 */
	public Element get(int index) {

		Validate.isTrue(index > -1);
		Validate.isTrue(index < size());

		return tree.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.tree.IProgrammTree#remove(int)
	 */
	public Element remove(int index) {

		Validate.isTrue(index > -1);
		Validate.isTrue(index < size());

		log.info("remove index: " + index + " in programmtree");

		return tree.remove(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#clear()
	 */
	public void clear() {

		log.info("programmtree is clearing...");

		tree.clear();
	}

	// Iterator
	public Element getLast() {

		return tree.get(tree.size() - 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.tree.IProgrammTree#iterator()
	 */
	@Override
	public Iterator<Element> iterator() {

		Iterator<Element> it = new Iterator<Element>() {

			private Element next = get(0);
			private Element lastElement = null;

			@Override
			public boolean hasNext() {

				return next != null;
			}

			@Override
			public Element next() {

				if (!hasNext())
					throw new NoSuchElementException();

				Element r = next;
				lastElement = r;

				if (next != null && next.hasChildren()) {
					next = next.getChildren().get(0);
				} else {

					if (next.getNext() == null) {

						while (next.getParent() != null) {
							next = next.getParent();
							if (next.getNext() != null)
								break;
						}

						next = next.getNext();

					} else {
						next = next.getNext();
					}
				}

				return r;
			}

			@Override
			public void remove() {

				Element parent = lastElement.getParent();
				if (parent == null) {
					removeElementAndAllChildren(lastElement);
				} else {
					parent.getChildren().removeElementAndAllChildren(
							lastElement);
				}

			}
		};
		return it;
	}

	@Override
	public Iterator<Element> reverseIterator() {

		Iterator<Element> it = new Iterator<Element>() {

			private Element before = getLastElement();
			private Element lastElement = null;

			private Element getLastElement() {

				before = getLast();
				while (before.hasChildren()) {
					before = before.getLatestChild();
				}
				return before;

			}

			@Override
			public boolean hasNext() {

				return before != null;
			}

			@Override
			public Element next() {

				if (!hasNext())
					throw new NoSuchElementException();

				Element r = before;
				lastElement = r;

				if (before != null && before.getBefore() != null) {

					before = before.getBefore();

					if (before.hasChildren()) {
						while (before.hasChildren()) {
							before = before.getLatestChild();
						}
					}

					// before
				} else {

					if (before.getParent() != null) {
						before = before.getParent();
					} else if (before.getParent() == null
							&& before.getBefore() == null) {
						before = null;
					}
				}

				return r;
			}

			@Override
			public void remove() {

				Element parent = lastElement.getParent();
				if (parent == null) {
					removeElementAndAllChildren(lastElement);
				} else {
					parent.getChildren().removeElementAndAllChildren(
							lastElement);
				}

			}
		};
		return it;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.counterobfuscator.parser.tree.IProgrammTree#removeElement(edu.hm
	 * .counterobfuscator.parser.tree.Element)
	 */
	public boolean removeElementAndAllChildren(Element element) {

		Element beforeEle = element.getBefore();
		Element nextEle = element.getNext();

		if (beforeEle != null)
			beforeEle.setNext(nextEle);

		if (nextEle != null)
			nextEle.setBefore(beforeEle);

		return tree.remove(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.counterobfuscator.parser.tree.IProgrammTree#removeElement(edu.hm
	 * .counterobfuscator.parser.tree.Element)
	 */
	public boolean remove(Element element) {

		if (element.hasChildren()) {

			Element beforeEle = element.getBefore();
			Element nextEle = element.getNext();
			Element parent = element.getParent();

			int index = tree.indexOf(element);

			for (int i = 0; i < element.getChildren().size(); i++) {

				Element child = element.getChild(i);
				child.setParent(parent);
				child.setDepth(element.getDepth());

				tree.add(index++, child);
			}

			if (beforeEle != null) {
				element.getChild(0).setBefore(beforeEle);
				beforeEle.setNext(element.getChild(0));
			}

			if (nextEle != null) {
				element.getLatestChild().setNext(nextEle);
				nextEle.setBefore(element.getLatestChild());
			}

		}
		element.setBefore(null);
		element.setNext(null);
		return removeElementAndAllChildren(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.tree.IProgrammTree#findGlobalScope()
	 */
	public Position findGlobalScope() {

		Position globalScope = new Position(0, 0);

		for (int i = 0; i < tree.size(); i++) {
			AbstractType element = tree.get(i).getType();

			if (element.getPos().getEndPos() > globalScope.getEndPos()) {
				globalScope.setEndPos(element.getPos().getEndPos());
			}
		}

		return globalScope;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.tree.IProgrammTree#copy()
	 */
	public IProgrammTree copy() {

		IProgrammTree newTree = new ProgrammTree();

		newTree.addAll(this);

		return newTree;
	}

}
