/**
 * 
 */
package edu.hm.counterobfuscation.parser;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import edu.hm.counterobfuscator.definitions.DEFINITION;
import edu.hm.counterobfuscator.parser.IParser;
import edu.hm.counterobfuscator.parser.ParserFactory;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 * 
 */
public class TestJSParser {

	@Test
	public void ParserTest() throws IllegalArgumentException, IOException, EncoderException {
		
		String testInput = "function() {var test=0;}";
		
		IParser parser = ParserFactory.create(testInput, false);
		
		IProgrammTree tree = parser.getProgrammTree();
		
		assertEquals(1, tree.size());
		assertEquals(1, tree.get(0).getChildren().size());
		assertEquals(DEFINITION.VARIABLE, tree.get(0).getChild(0).getDefinition().getDefinition());
		assertEquals("test", tree.get(0).getChild(0).getDefinition().getName());
		assertEquals("0", tree.get(0).getChild(0).getDefinition().getValue());

	}
}
