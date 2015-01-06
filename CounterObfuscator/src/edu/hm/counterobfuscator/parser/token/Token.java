package edu.hm.counterobfuscator.parser.token;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 25.12.2014
 * 
 *       Class represent an Token
 * 
 */
public class Token {

	private int			pos;
	private TOKENTYPE	type;
	private String		value;
	private boolean	hasWhitespace;

	/**
	 * @param int pos
	 * @param TOKENTYPE
	 *           tokentype
	 * @param String
	 *           value
	 * 
	 *           Constructor
	 * 
	 */
	public Token(int pos, TOKENTYPE tokentype, String value) {

		this.pos = pos;
		this.type = tokentype;
		this.value = value;
		this.hasWhitespace = tokentype == TOKENTYPE.RETURN ? true : false;
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

	/**
	 * @param Value
	 *           of Token
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public boolean hasWhitespace() {
		return hasWhitespace;
	}
}
