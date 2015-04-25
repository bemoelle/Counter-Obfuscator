package edu.hm.counterobfuscation.parser.token;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.hm.counterobfuscator.parser.token.Tokenizer;

public class TokenizerTest {

	@Test
	public void getTokensTest() {
		
		String testInput = "function(){var test=0;}";
		String testArray[] = {"function", "(", ")", "{", "var", " ", "test", "=", "0", ";", "}"};
		
		Tokenizer tokenizer = new Tokenizer(testInput);
		tokenizer.process();
		
		assertEquals(11, tokenizer.getTokens().size());
		
		for(int i=0; i<testArray.length; i++) {
			String value1 = tokenizer.getTokens().get(i).getValue();
			String value2 = (testArray[i]);
			if(!value1.equals(value2)) {
				fail();
			}	
		}
		
		
	}

}
