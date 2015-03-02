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
public class Scope {

	private int	startPos;
	private int	endPos;

	/**
	 * @param startPos
	 * @param endPos
	 */
	public Scope(int startPos, int endPos) {
		
		Validate.isTrue(startPos <= endPos);
		Validate.isTrue(startPos > -1);
		
		this.startPos = startPos;
		this.endPos = endPos;
	}

	/**
	 * @return
	 */
	public int getStartPos() {
		return startPos;
	}

	/**
	 * @param endPos
	 */
	public void setEndPos(int endPos) {
		this.endPos = endPos;
	}

	/**
	 * @return
	 */
	public int getEndPos() {
		return endPos;
	}
	
	/**
	 * @param otherPos
	 * @return
	 */
	public boolean isPosWithin(Scope otherPos) {
		
		Validate.notNull(otherPos);
		
		if(startPos > otherPos.startPos) return false;
		if(endPos < otherPos.endPos) return false;
		
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {

		if (!(other instanceof Scope)) {
			return false;
		}
		
		Scope otherPosition = (Scope) other;
		if (this.startPos != otherPosition.startPos || this.endPos != otherPosition.endPos) {
			return false;
		}

		return true;
	}
}
