package edu.hm.webscraper.types;

/*
 * represent an JavaScript VAR
 * assign to @TOKENTYPE.VAR
 */
public class Variable extends AbstractType {

	private int	endPos;
	private int	startPos;

	public Variable(int startPos, int endPos) {
		this.startPos = startPos;
		this.endPos = endPos;
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
	
	public void print() {
		System.out.println("startPos: " + startPos + " -- " + "endPos: " + endPos);

	}

}
