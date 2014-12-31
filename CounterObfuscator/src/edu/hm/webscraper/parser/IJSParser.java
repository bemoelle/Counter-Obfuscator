package edu.hm.webscraper.parser;

import edu.hm.webscraper.parser.token.ITokenAnalyser;

public interface IJSParser {

	public ITokenAnalyser getTokenAnalyser();

	public void printAllTokens();
}
