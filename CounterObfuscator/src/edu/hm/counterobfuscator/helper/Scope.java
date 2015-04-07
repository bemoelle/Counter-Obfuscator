/**
 * 
 */
package edu.hm.counterobfuscator.helper;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.01.2015
 * 
 *       this class represent a scope, definitions e.g. vars, functions, etc.
 *       only valid in a given scope. th scope has an start and end position in
 *       a given programm tree {@link ProgrammTree}
 */
public class Scope {

	private int startPos;
	private int endPos;

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
	 * @return the start position
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
	 * @return the end position
	 */
	public int getEndPos() {

		return endPos;
	}

	/**
	 * @param otherPos
	 * @return true if a scope is fully within this.scope, otherwise false
	 */
	public boolean isPosWithin(Scope otherPos) {

		Validate.notNull(otherPos);

		if (startPos > otherPos.startPos)
			return false;
		if (endPos < otherPos.endPos)
			return false;

		if (startPos == otherPos.startPos && endPos == otherPos.endPos)
			return false;

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {

		if (!(other instanceof Scope)) {
			return false;
		}

		Scope otherPosition = (Scope) other;
		if (this.startPos != otherPosition.startPos
				|| this.endPos != otherPosition.endPos) {
			return false;
		}

		return true;
	}
}
