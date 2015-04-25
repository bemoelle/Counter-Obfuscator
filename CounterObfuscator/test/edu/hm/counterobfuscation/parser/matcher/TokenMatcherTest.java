package edu.hm.counterobfuscation.parser.matcher;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.hm.counterobfuscator.parser.matcher.TokenMatcher;

public class TokenMatcherTest {

	@Test
	public void matchAllTest1() {
		
		String matchString = ".|:";
		
		TokenMatcher matcher = new TokenMatcher(matchString);
		
		assertEquals(true, matcher.matchAll("."));
		assertEquals(true, matcher.matchAll(":"));
		assertEquals(false, matcher.matchAll(";"));
		assertEquals(false, matcher.matchAll(","));
		assertEquals(false, matcher.matchAll(""));
		assertEquals(false, matcher.matchAll(" "));
		
	}

}
