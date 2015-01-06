/**
 * 
 */
package edu.hm.counterobfuscator.interpreter;

import javax.script.ScriptException;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 * 
 */
public interface IInterpreter {
	
	public void process() throws ScriptException;

}
