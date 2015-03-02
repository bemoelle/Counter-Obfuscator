/**
 * 
 */
package edu.hm.counterobfuscator.refactor.modul;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.parser.tree.IProgrammTree;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.02.2015
 * 
 * 
 */
public interface IModul {

	/**
	 * @return
	 * @throws ScriptException 
	 */
	IProgrammTree process() throws ScriptException;

}
