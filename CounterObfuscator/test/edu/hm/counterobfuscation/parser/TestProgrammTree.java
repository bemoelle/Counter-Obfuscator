/**
 * 
 */
package edu.hm.counterobfuscation.parser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.hm.counterobfuscator.definitions.AbstractType;
import edu.hm.counterobfuscator.definitions.Default;
import edu.hm.counterobfuscator.helper.Scope;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.ProgrammTree;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 08.02.2015
 * 
 * 
 */
public class TestProgrammTree {

	private List<AbstractType>	list;

	@Before
	public void before() {

		AbstractType a1 = new Default(new Scope(1, 2), "1");
		AbstractType a2 = new Default(new Scope(3, 50), "2");
		AbstractType a3 = new Default(new Scope(4, 5), "3");

		AbstractType a4 = new Default(new Scope(6, 20), "4");
		AbstractType a5 = new Default(new Scope(7, 19), "5");
		AbstractType a6 = new Default(new Scope(8, 13), "6");
		AbstractType a7 = new Default(new Scope(14, 15), "7");

		AbstractType a8 = new Default(new Scope(21, 49), "8");
		AbstractType a9 = new Default(new Scope(22, 48), "9");
		AbstractType a10 = new Default(new Scope(23, 47), "10");

		AbstractType a11 = new Default(new Scope(51, 52), "11");
		AbstractType a12 = new Default(new Scope(53, 54), "12");

		list = new ArrayList<AbstractType>();
		list.add(a1);
		list.add(a2);
		list.add(a3);
		list.add(a4);
		list.add(a5);
		list.add(a6);
		list.add(a7);
		list.add(a8);
		list.add(a9);
		list.add(a10);
		list.add(a11);
		list.add(a12);
	}

	@Test
	public void interatorTest() {

		Integer counter = 1;

		ProgrammTree ttt = new ProgrammTree(list);

		Iterator<Element> it = ttt.iterator();

		while (it.hasNext()) {

			Element el = it.next();
			assertEquals(el.getDefinition().getName(), (counter++).toString());

		}
	}

	@Test
	public void reverseInteratorTest() {

		Integer counter = 12;

		ProgrammTree ttt = new ProgrammTree(list);

		Iterator<Element> it = ttt.reverseIterator();

		while (it.hasNext()) {

			Element el = it.next();
			assertEquals(el.getDefinition().getName(), (counter--).toString());

		}
	}

	@Test
	public void interatorRemoveTest() {

		Integer counter = 0;
		
		String[] testElements = {"1","2","3","8","9","10","11","12"};

		ProgrammTree ttt = new ProgrammTree(list);

		Iterator<Element> it1 = ttt.iterator();

		while (it1.hasNext()) {

			Element el = it1.next();
			
			if(el.getDefinition().getName().equals("4"))
				it1.remove();
		
		}
		
		Element ele3 = null;
		Element ele8 = null;

		Iterator<Element> it2 = ttt.iterator();

		while (it2.hasNext()) {

			Element el = it2.next();
			assertEquals(el.getDefinition().getName(), testElements[counter++]);
			
			if(el.getDefinition().getName().equals("3"))
				ele3 = el;
			
			if(el.getDefinition().getName().equals("8"))
				ele8 = el;
			
		}
		
		assertNotNull(ele3);
		assertNotNull(ele8);
		
		assertEquals(ele3.getNext().getDefinition().getName(), "8");
		assertEquals(ele8.getBefore().getDefinition().getName(), "3");
		
	}
}
