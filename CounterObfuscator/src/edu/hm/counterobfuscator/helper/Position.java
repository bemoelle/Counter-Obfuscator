/**
 * 
 */
package edu.hm.counterobfuscator.helper;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.01.2015
 * 
 * 
 */
public class Position {

	private int	startPos;
	private int	endPos;

	public Position(int startPos, int endPos) {
		this.startPos = startPos;
		this.endPos = endPos;
	}

	public int getStartPos() {
		return startPos;
	}
	
	public void setEndPos(int endPos) {
		this.endPos=endPos;
	}

	public int getEndPos() {
		return endPos;
	}

}
