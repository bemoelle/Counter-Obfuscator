package edu.hm.webscraper.parser;

public class Token {

	private int pos;
	private TOKENTYPE type;
	private String value;

	//class represent an Token
	public Token(int pos, TOKENTYPE tokentype, String value) {

		this.pos = pos;
		this.type = tokentype;
		this.value = value;
	}

	public String getKeys() {
		return getPos() + " : " + type;
	}

	public TOKENTYPE getType() {
		return type;
	}

	public int getPos() {
		return pos;
	}

	public String getValue() {
		return value;
	}

}
