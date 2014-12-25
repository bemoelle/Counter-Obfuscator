package edu.hm.webscraper.parser.token;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 25.12.2014
 * 
 *       Class represent an Token
 * 
 */
public class Token {

	private int pos;
	private TOKENTYPE type;
	private String value;

	/**
	 * @param int pos
	 * @param TOKENTYPE
	 *            tokentype
	 * @param String
	 *            value
	 * 
	 *            Constructor
	 * 
	 */
	public Token(int pos, TOKENTYPE tokentype, String value) {

		this.pos = pos;
		this.type = tokentype;
		this.value = value;
	}

	/**
	 * @return TOKENTYPE
	 */
	public TOKENTYPE getType() {
		return type;
	}

	/**
	 * @return Position of Toekn
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * @return Value of Token
	 */
	public String getValue() {
		return value;
	}

}
