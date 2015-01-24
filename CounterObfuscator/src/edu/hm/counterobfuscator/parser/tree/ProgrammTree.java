/**
 * 
 */
package edu.hm.counterobfuscator.parser.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.types.AbstractType;
import edu.hm.counterobfuscator.types.Default;
import edu.hm.counterobfuscator.types.ForWhile;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.types.Call;
import edu.hm.counterobfuscator.types.Return;
import edu.hm.counterobfuscator.types.This;
import edu.hm.counterobfuscator.types.Variable;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 06.01.2015
 * 
 * 
 */
public class ProgrammTree implements IProgrammTree {

	private List<Element>	typeTree;
	private boolean			isFlat;

	/**
	 * 
	 */
	public ProgrammTree() {
		this.typeTree = new ArrayList<Element>();
		this.isFlat = false;
	}

	/**
	 * 
	 */
	public ProgrammTree(List<Element> typeTree) {
		this.typeTree = typeTree;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#isEmpty()
	 */
	public boolean isEmpty() {
		return typeTree.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#add(edu.hm.
	 * counterobfuscator.parser.token.trees.TypeTreeElement)
	 */
	public void add(Element element) {

		typeTree.add(element);
	}

	public void addAll(ProgrammTree other) {

		typeTree.addAll(other.typeTree);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#print()
	 */
	public void print() {

		if (!isFlat)
			printChildElement("", this);
		else {

			for (int i = 0; i < typeTree.size(); i++) {

				Element element = typeTree.get(i);

				String test = "";
				if (element.getType() instanceof Variable) {
					test += ((Variable) element.getType()).getName() + " = ";
					test += ((Variable) element.getType()).getValue();
				}
				if (element.getType() instanceof Default) {
					test += ((Default) element.getType()).getName();
				}

				System.out.println("|__" + element.getType().getType().toString() + " -- " + test);
			}
		}

	}

	/**
	 * @param tab
	 * @param tree
	 */
	private void printChildElement(String tab, IProgrammTree tree) {

		for (int i = 0; i < tree.size(); i++) {

			Element element = tree.get(i);

			String test = "";
			if (element.getType() instanceof Variable) {
				test += ((Variable) element.getType()).getName() + " = ";
				test += ((Variable) element.getType()).getValue();
			}
			if (element.getType() instanceof Default) {
				test += ((Default) element.getType()).getName();
			}

			System.out.println("|" + tab + element.getType().getType().toString() + " -- " + test);

			if (element.hasChildren()) {

				printChildElement(tab + "__", element.getChildren());
			}
		}
	}

	public void prettyPrint() {

		if (!isFlat) {
			System.out.println("prettyPrint() is not possible when tree is flat");
		}
		else {
			System.out.println(prettyPrintChildElement("", this));
		}

	}

	private String prettyPrintChildElement(String tab, IProgrammTree tree) {

		String test = "";

		for (int i = 0; i < tree.size(); i++) {

			Element element = tree.get(i);

			test += call(element.getType());

			if (element.hasChildren()) {
				test += tab + prettyPrintChildElement(tab, element.getChildren());
			}
		}

		return test;
	}

	private String call(AbstractType abstractType) {

		switch (abstractType.getType()) {

		case FUNCTION:
			Function func = (Function) abstractType;
			return "function " + func.getName() + func.getHeadString() + "{\n";
		case CALL:
			Call fc = (Call) abstractType;
			return fc.getName() + "." + fc.getFunction() + "(" + fc.getValue() + ");\n";
		case VARIABLE:
			Variable var = (Variable) abstractType;
			return (var.isGlobal() ? "" : "var ") + var.getName() + "="
					+ (var.isObject() ? "new " : "") + var.getValue()
					+ (var.isObject() ? "(" + var.getParameter() + ")" : "") + ";\n";
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

		return typeTree.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#get(int)
	 */
	public Element get(int index) {

		return typeTree.get(index);
	}

	public Element remove(int index) {
		return typeTree.remove(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#clear()
	 */
	public void clear() {
		typeTree.clear();

	}

	// -----------------------------------------------------
	// Iterator
	public Element getLast() {
		return typeTree.get(typeTree.size() - 1);
	}

	@Override
	public Iterator<Element> iterator() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.hm.counterobfuscator.parser.tree.ITypeTree#removeElementOfType(edu
	 * .hm.counterobfuscator.types.AbstractType)
	 */
	@Override
	public boolean removeElement(Element type) {

		return typeTree.remove(type);
	}

	public Position findGlobalScope() {

		Position globalScope = new Position(0, 0);

		for (int i = 0; i < typeTree.size(); i++) {
			AbstractType element = typeTree.get(i).getType();

			if (element.getPos().getEndPos() > globalScope.getEndPos()) {
				globalScope.setEndPos(element.getPos().getEndPos());
			}
		}

		return globalScope;
	}

	public IProgrammTree flatten() {

		ProgrammTree list = new ProgrammTree();
		this.isFlat = true;
		list.isFlat = true;
		return walkThroughElement(this, list);
	}

	/**
	 * @return
	 */
	private IProgrammTree walkThroughElement(IProgrammTree tree, IProgrammTree flatList) {

		for (int i = 0; i < tree.size(); i++) {

			flatList.add(tree.get(i));

			if (tree.get(i).hasChildren()) {

				walkThroughElement(tree.get(i).getChildren(), flatList);
			}
		}
		return flatList;
	}

	public IProgrammTree reverseOrder() {

		List<Element> reverse = typeTree;

		Collections.reverse(reverse);

		ProgrammTree reversedTree = new ProgrammTree(reverse);
		reversedTree.isFlat = this.isFlat;
		
		return reversedTree;
	}

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.parser.tree.IProgrammTree#isFlat()
	 */
	@Override
	public boolean isFlat() {
		return isFlat;
	}

}
