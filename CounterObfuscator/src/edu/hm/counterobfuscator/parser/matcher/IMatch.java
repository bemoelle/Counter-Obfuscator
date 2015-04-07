package edu.hm.counterobfuscator.parser.matcher;

public interface IMatch {

	/**
	 * @param match
	 * @return true if a string matches with a given match-string. the
	 *         match-string is committed with the contructor
	 */
	boolean matchAll(String match);

}
