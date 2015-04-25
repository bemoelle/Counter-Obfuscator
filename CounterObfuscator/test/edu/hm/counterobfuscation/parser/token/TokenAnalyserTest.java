package edu.hm.counterobfuscation.parser.token;

import static org.junit.Assert.*;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import edu.hm.counterobfuscator.definitions.AbstractType;
import edu.hm.counterobfuscator.definitions.DEFINITION;
import edu.hm.counterobfuscator.parser.token.ITokenizer;
import edu.hm.counterobfuscator.parser.token.TokenAnalyser;
import edu.hm.counterobfuscator.parser.token.Tokenizer;

public class TokenAnalyserTest {

	@Test
	public void getAllTypesTest() throws IllegalArgumentException, EncoderException {
		
		String testInput = "function(){var test=0;}";
		
		ITokenizer tokenizer = new Tokenizer(testInput);
		tokenizer.process();
		
		TokenAnalyser tokenanlyser = new TokenAnalyser(tokenizer);
		tokenanlyser.process();
		
		assertEquals(2, tokenanlyser.getAllTypes().size());
		
		AbstractType type0 = tokenanlyser.getAllTypes().get(0);
		AbstractType type1 = tokenanlyser.getAllTypes().get(1);
		
		assertEquals(DEFINITION.FUNCTION, type0.getDefinition());
		assertEquals(DEFINITION.VARIABLE, type1.getDefinition());
		assertEquals("test", type1.getName());
		assertEquals("0", type1.getValue());
		
	}

}
