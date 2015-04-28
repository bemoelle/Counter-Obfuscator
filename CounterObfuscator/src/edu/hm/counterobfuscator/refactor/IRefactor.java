/**
 * 
 */
package edu.hm.counterobfuscator.refactor;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.parser.tree.IProgrammTree;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 02.01.2015
 * 
 *       interface for all refactor classes
 */
public interface IRefactor {

	/**
	 * @return
	 * @throws ScriptException
	 * 
	 *            process method to refactor the actual part of the programm
	 */
	public IProgrammTree process() throws ScriptException;

}
