/**
 * 
 */
package edu.hm.counterobfuscator.interpreter;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.parser.token.trees.ITypeTree;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 * 
 */
public interface IInterpreter {
	
	public ITypeTree process() throws ScriptException;

}
