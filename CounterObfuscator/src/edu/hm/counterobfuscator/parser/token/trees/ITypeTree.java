package edu.hm.counterobfuscator.parser.token.trees;

import java.util.function.Consumer;

public interface ITypeTree extends Iterable<TypeTreeElement> {

	public boolean isEmpty();

	public void add(TypeTreeElement tte);
	
	public void print();

	public int size();

	public TypeTreeElement get(int index);

	public void clear();

	public TypeTreeElement getLast();
}
