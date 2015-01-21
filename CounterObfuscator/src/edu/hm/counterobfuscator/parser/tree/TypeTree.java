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
import edu.hm.counterobfuscator.types.FunctionCall;
import edu.hm.counterobfuscator.types.Return;
import edu.hm.counterobfuscator.types.This;
import edu.hm.counterobfuscator.types.Variable;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 06.01.2015
 * 
 * 
 */
public class TypeTree implements ITypeTree {

	private List<TypeTreeElement>	typeTree;

	/**
	 * 
	 */
	public TypeTree() {
		this.typeTree = new ArrayList<TypeTreeElement>();
	}

	/**
	 * 
	 */
	public TypeTree(List<TypeTreeElement> typeTree) {
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
	public void add(TypeTreeElement element) {

		typeTree.add(element);
	}

	public void addAll(List<TypeTreeElement> treeList) {

		typeTree.addAll(treeList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.parser.token.trees.ITypeTree#print()
	 */
	public void print(boolean flat) {

		if (!flat)
			printChildElement("", this);
		else {

			for (int i = 0; i < typeTree.size(); i++) {

				TypeTreeElement element = typeTree.get(i);

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
	private void printChildElement(String tab, ITypeTree tree) {

		for (int i = 0; i < tree.size(); i++) {

			TypeTreeElement element = tree.get(i);

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

		String output = prettyPrintChildElement("", this);
		System.out.println(output);

	}

	private String prettyPrintChildElement(String tab, ITypeTree tree) {

		String test = "";

		for (int i = 0; i < tree.size(); i++) {

			TypeTreeElement element = tree.get(i);

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
		case FUNCTIONCALL:
			FunctionCall fc = (FunctionCall) abstractType;
			return "var " + fc.getName() + "." + fc.getFunction() + "(" + fc.getParameter() + ");\n";
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
	public TypeTreeElement get(int index) {

		return typeTree.get(index);
	}

	public TypeTreeElement remove(int index) {
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
	public TypeTreeElement getLast() {
		return typeTree.get(typeTree.size() - 1);
	}

	@Override
	public Iterator<TypeTreeElement> iterator() {
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
	public boolean removeElement(TypeTreeElement type) {

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

	public ITypeTree flatten() {

		ITypeTree list = new TypeTree();

		return walkThroughElement(this, list);
	}

	/**
	 * @return
	 */
	private ITypeTree walkThroughElement(ITypeTree tree, ITypeTree flatList) {

		for (int i = 0; i < tree.size(); i++) {

			flatList.add(tree.get(i));

			if (tree.get(i).hasChildren()) {

				walkThroughElement(tree.get(i).getChildren(), flatList);
			}
		}
		return flatList;

	}

	public ITypeTree reverseOrder() {

		List<TypeTreeElement> reverse = typeTree;

		Collections.reverse(reverse);

		return new TypeTree(reverse);
	}

}
