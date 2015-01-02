package edu.hm.counterobfuscator.types;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 *       represent JavaScript FUNCTION assign to TOKENTYPE.FUNCTION
 * 
 */
public class Function extends AbstractType {

	private String	head;
	private String	boby;

	public Function(int startPos, int endPos, String name, String head, String boby) {
		super(startPos, endPos, name);
		this.head = head;
		this.boby = boby;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getBoby() {
		return boby;
	}

	public void setBoby(String boby) {
		this.boby = boby;
	}

}
