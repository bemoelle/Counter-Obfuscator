package edu.hm.webscraper.types;

/*
 * represent an JavaScript VAR
 * assign to @TOKENTYPE.VAR
 */
public class Variable extends AbstractType {

	private int	endPos;
	private int	startPos;
	private String	name;
	private String	value;

	public Variable(int startPos, int endPos, String name, String value) {
		this.startPos = startPos;
		this.endPos = endPos;
		this.name = name;
		this.value = value;
	}

	public int getStartPos() {
		return startPos;
	}

	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}

	public int getEndPos() {
		return endPos;
	}

	public void setEndPos(int endPos) {
		this.endPos = endPos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void print() {
		System.out.println("startPos: " + startPos + " -- " + "endPos: " + endPos);

	}

}
