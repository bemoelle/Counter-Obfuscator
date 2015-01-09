package edu.hm.counterobfuscator.mapper;


import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.types.AbstractType;

public class MapperElement {
	
	private Position scope;
	private AbstractType type;

	public MapperElement(Position scope, AbstractType type) {
		this.scope = scope;
		this.type = type;
	}

	public AbstractType getType() {
		return type;
	}

	public void setType(AbstractType type) {
		this.type = type;
	}

	public Position getScope() {
		return scope;
	}

	public void setScope(Position scope) {
		this.scope = scope;
	}

}
