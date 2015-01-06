package edu.hm.counterobfuscator.types;

import java.util.List;

import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.parser.token.Token;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 *       represent JavaScript FUNCTION assign to TOKENTYPE.FUNCTION
 * 
 */
public class Function extends AbstractType {

	private String			head;
	private List<Token>	boby;
	private boolean		isPacked;

	public Function(Position pos, String name, String head, boolean isPacked, List<Token> boby) {
		super(TYPE.FUNCTION, pos, name);
		this.head = head;
		this.isPacked = isPacked;
		this.boby = boby;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public List<Token> getBoby() {
		return boby;
	}

	public boolean isPacked() {
		return isPacked;
	}

	public void setBoby(List<Token> boby) {
		this.boby = boby;
	}

}
