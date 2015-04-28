package edu.hm.counterobfuscator.parser.tree;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import edu.hm.counterobfuscator.helper.Scope;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 09.01.2015
 * 
 */
public interface IProgrammTree extends Iterable<Element> {

	/**
	 * @return true if programm tree is empty
	 */
	public boolean isEmpty();

	/**
	 * @param typeTreeElement
	 *            add element to programmtree
	 */
	public void add(Element element);

	/**
	 * @return size of programmtree
	 */
	public int size();

	/**
	 * @param index
	 * @return element
	 * 
	 *         return element at index
	 */
	public Element get(int index);

	/**
	 * wipe out programmtree
	 */
	public void clear();

	/**
	 * @return last element in programmtree
	 */
	public Element getLast();

	/**
	 * @param index
	 * @return element
	 * 
	 *         remove element at index and return this element
	 * 
	 */
	public Element remove(int index);

	/**
	 * @param element
	 * @return true if removing was successfully
	 * 
	 *         remove element and all of its children
	 */
	public boolean removeElementAndAllChildren(Element element);

	/**
	 * @param element
	 * @return true if removing was successfully
	 * 
	 *         remove element in programmtree and keep its children. childeren
	 *         will get the parent of element as new parent
	 */
	public boolean remove(Element element);

	/**
	 * @return position
	 * 
	 *         find global scope of programmtree
	 */
	public Scope findGlobalScope();

	/**
	 * @param ProgrammTree
	 * 
	 *            add whole programmtree to actual programmtree
	 */
	public void addAll(ProgrammTree ProgrammTree);

	/**
	 * @return Iterator to iterate throw programmtree
	 */
	public Iterator<Element> iterator();

	/**
	 * @return reverseIterator to interate throw programmtree in reverse order
	 */
	public Iterator<Element> reverseIterator();

	/**
	 * print actual programmtree to System.out.
	 */
	public void printOnConsole();

	/**
	 * @return a deep copy od actual programmtree
	 */
	public IProgrammTree copy();

	/**
	 * @param element
	 */
	public void replaceElementWithTree(Element element, IProgrammTree tree);

	/**
	 * @param outputFile
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public void printToFile(String outputFile) throws UnsupportedEncodingException, FileNotFoundException, IOException;
}
