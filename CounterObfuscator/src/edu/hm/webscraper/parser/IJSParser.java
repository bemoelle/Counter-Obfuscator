package edu.hm.webscraper.parser;

import edu.hm.webscraper.parser.token.TokenAnalyser;

public interface IJSParser {

	public TokenAnalyser getTokenAnalyser();

	public void printAllTokens();
}
