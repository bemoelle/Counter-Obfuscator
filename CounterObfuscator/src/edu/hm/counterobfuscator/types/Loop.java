/**
 * 
 */
package edu.hm.counterobfuscator.types;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 * 
 */
public class Loop  {

	private String	head;
	private String	boby;

	public Loop(int startPos, int endPos, String name, String head, String boby) {
		
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
