package edu.hm.counterobfuscator.types;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 * 
 */
public abstract class AbstractType {

	private int		endPos;
	private int		startPos;
	private String	name;

	public AbstractType(int startPos, int endPos, String name) {
		this.startPos = startPos;
		this.endPos = endPos;
		this.name = name;
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

	public void print() {
		System.out.println("startPos: " + startPos + " -- " + "endPos: " + endPos);

	}

}
