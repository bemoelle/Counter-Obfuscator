/**
 * 
 */
package edu.hm.counterobfuscator.types;

import edu.hm.counterobfuscator.helper.Position;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 15.01.2015
 * 
 *       e.g. obj.SayHello(_0xd237[3]);
 * 
 *       name: obj function: SayHello value: _0xd237[3]
 * 
 */
public class FunctionCall extends AbstractType {
	private String	value;
	private String	parameter;

	public FunctionCall(Position pos, String name, String parameter, String value) {
		super(TYPE.FUNCTIONCALL, pos, name);
		this.parameter = parameter;
		
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFunction() {
		return parameter;
	}

	public void setFunction(String parameter) {
		this.parameter = parameter;
	}

}
