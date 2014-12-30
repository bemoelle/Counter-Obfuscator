package edu.hm.webscraper.types;

/*
 * represent JavaScript FUNCTION
 * @assign to TOKENTYPE.FUNCTION
 */
public class Function {

	private int pos;
	private String name;
	private String head;
	private String body;
	private String packedVars;
	private boolean hasReturn;
	private boolean isPacked;
	

	public Function(int pos, String name, String head, String body) {

		this.pos = pos;
		this.name = name;
		this.head = head;
		this.body = body;
		this.setPackedVars("");
		this.hasReturn = false;
		this.isPacked = false;
	}

	public int getPos() {
		return pos;
	}

	public String getName() {
		return name;
	}

	public String getHead() {
		return head;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getPackedVars() {
		return packedVars;
	}

	public void setPackedVars(String packedVars) {
		this.packedVars = packedVars;
	}

	public boolean isHasReturn() {
		return hasReturn;
	}

	public void setHasReturn(boolean hasReturn) {
		this.hasReturn = hasReturn;
	}

	public boolean isPacked() {
		return isPacked;
	}

	public void setPacked(boolean isPacked) {
		this.isPacked = isPacked;
	}
}
