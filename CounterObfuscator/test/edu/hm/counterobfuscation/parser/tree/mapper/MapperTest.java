package edu.hm.counterobfuscation.parser.tree.mapper;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import edu.hm.counterobfuscator.definitions.DEFINITION;
import edu.hm.counterobfuscator.parser.IParser;
import edu.hm.counterobfuscator.parser.Parser;
import edu.hm.counterobfuscator.parser.ParserFactory;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.mapper.MapperElement;

public class MapperTest {

	@Test
	public void mapperTest() throws IOException, IllegalArgumentException, EncoderException {
		
		String input = "function(){var test=0;var test2=0;}";
		
		IParser parser = ParserFactory.create(input, false);
		IProgrammTree tree = parser.getProgrammTree();
		
		Mapper mapper = new Mapper(tree);
		List<MapperElement> vars = mapper.process(DEFINITION.VARIABLE);
		List<MapperElement> functions = mapper.process(DEFINITION.FUNCTION);
		
		//vars
		assertEquals(2, vars.size());
		assertEquals("test", vars.get(0).getElement().getDefinition().getName());
		assertEquals(4, vars.get(0).getScope().getStartPos());
		assertEquals(16, vars.get(0).getScope().getEndPos());
		assertEquals("test2", vars.get(1).getElement().getDefinition().getName());
		assertEquals(10, vars.get(1).getScope().getStartPos());
		assertEquals(16, vars.get(1).getScope().getEndPos());
		
		//functions
		assertEquals(1, functions.size());
		assertEquals(0, functions.get(0).getScope().getStartPos());
		assertEquals(100000, functions.get(0).getScope().getEndPos());
		
	}

}
