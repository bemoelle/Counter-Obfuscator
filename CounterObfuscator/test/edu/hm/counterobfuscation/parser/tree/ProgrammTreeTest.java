package edu.hm.counterobfuscation.parser.tree;

import static org.junit.Assert.*;

import org.apache.commons.codec.EncoderException;
import org.junit.Before;
import org.junit.Test;

import edu.hm.counterobfuscator.definitions.AbstractType;
import edu.hm.counterobfuscator.definitions.DEFINITION;
import edu.hm.counterobfuscator.definitions.Variable;
import edu.hm.counterobfuscator.parser.token.ITokenizer;
import edu.hm.counterobfuscator.parser.token.TokenAnalyser;
import edu.hm.counterobfuscator.parser.token.Tokenizer;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.ProgrammTree;

public class ProgrammTreeTest {
	
	private IProgrammTree tree;

	@Before
	public void before() throws IllegalArgumentException, EncoderException {
		
		String testInput = "function(){var test=0;}";
		
		ITokenizer tokenizer = new Tokenizer(testInput);
		tokenizer.process();
		
		TokenAnalyser tokenanlyser = new TokenAnalyser(tokenizer);
		tokenanlyser.process();
		
		tree = new ProgrammTree(tokenanlyser.getAllTypes());	
	}
	
	@Test
	public void isEmptyTest() {
		
		assertEquals(false, tree.isEmpty());
		tree.remove(0);
		assertEquals(true, tree.isEmpty());	
	}
	
	@Test
	public void addTest() {
		
		AbstractType var2 = new Variable(null, "test2", "=", "0", false);
		
		assertEquals(false, tree.isEmpty());
		assertEquals(1, tree.get(0).getChildren().size());
		tree.get(0).getChildren().add(new Element(null, var2, 1));
		assertEquals(2, tree.get(0).getChildren().size());
		
		tree.add(new Element(null, var2, 0));
		assertEquals(2, tree.size());
	}
	
	@Test
	public void getLastTest() {
				
		assertEquals(false, tree.isEmpty());
		assertNotNull(tree.getLast());
		
		Element element = tree.getLast();
		assertEquals("", element.getDefinition().getName());
		
		Element element1 = element.getChildren().getLast();
		assertEquals("test", element1.getDefinition().getName());
	}
	
	@Test
	public void clearTest() {
				
		assertEquals(false, tree.isEmpty());
		tree.clear();
		assertEquals(true, tree.isEmpty());
	}

}
