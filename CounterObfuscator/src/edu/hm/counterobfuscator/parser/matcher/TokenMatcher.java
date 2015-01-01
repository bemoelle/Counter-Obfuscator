package edu.hm.counterobfuscator.parser.matcher;

import java.util.HashSet;

public class TokenMatcher implements IMatch {
	
	HashSet<String> matches;
	
	public TokenMatcher(String matchString) {
		
		matches = new HashSet<String>();
		
		String[] matcherArray = matchString.split("\\|");
		
		for(String match: matcherArray) {
			matches.add(match);
		}
	}
	
	public boolean matchAll(String input) {
				
			for(String match: matches) {
				
				if(input.contains(match)) {
					return true;
				}
			}	
			return false;
	}
		
}
