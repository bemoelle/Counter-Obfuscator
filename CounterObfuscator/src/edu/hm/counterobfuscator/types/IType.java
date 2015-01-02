package edu.hm.counterobfuscator.types;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 30.12.2014
 * 
 *       Interface class for Javascript types, e.g. var, function, while, for,
 *       try-catch, ...
 * 
 */
public interface IType {

	public int getStartPos();

	public void setStartPos(int startPos);

	public int getEndPos();

	public void setEndPos(int endPos);
	
	public String getName();
	
	public void setName(String name);
	
	public void print();

}
