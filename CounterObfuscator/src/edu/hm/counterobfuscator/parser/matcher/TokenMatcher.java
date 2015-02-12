package edu.hm.counterobfuscator.parser.matcher;

import java.util.HashSet;
import java.util.logging.Logger;

import edu.hm.counterobfuscator.helper.Validate;

public class TokenMatcher implements IMatch {

	HashSet<String> matches;
	private Logger log;

	public TokenMatcher(String matchString) {

		Validate.notNull(matchString);

		log = Logger.getLogger(TokenMatcher.class.getName());

		matches = new HashSet<String>();

		String[] matcherArray = matchString.split("\\|");

		for (String match : matcherArray) {
			matches.add(match);
		}
	}

	public boolean matchAll(String input) {

		for (String match : matches) {

			if (input.contains(match)) {
				log.info("found match: " + match + " -- input: " + input);
				return true;
			}
		}
		log.info("no match found input: " + input);
		return false;
	}

}
