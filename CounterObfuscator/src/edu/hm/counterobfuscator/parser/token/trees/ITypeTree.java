package edu.hm.counterobfuscator.parser.token.trees;

public interface ITypeTree {

	public boolean isEmpty();

	public void add(TypeTreeElement tte);
	
	public void print();

	public int size();

	public TypeTreeElement get(int index);

	public void clear();

	public TypeTreeElement getLast();

}
