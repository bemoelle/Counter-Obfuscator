package edu.hm.counterobfuscator.refactor.modul;

import java.util.List;

import javax.script.ScriptException;

import edu.hm.counterobfuscator.client.IClient;
import edu.hm.counterobfuscator.definitions.DEFINITION;
import edu.hm.counterobfuscator.definitions.ForWhile;
import edu.hm.counterobfuscator.definitions.If;
import edu.hm.counterobfuscator.definitions.Variable;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.mapper.MapperElement;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.02.2015
 * 
 *       check if the loop is necessary
 * 
 *       for(i=0; i<0; i++) {...}; not necessary loop and body can be removed
 *       for(i=0; i<1; i++) {...}; loop head is not necessary code can replaced
 *       with code in body 
 *       while(false) {...}; not necessary loop and body can
 *       be removed
 * 
 */
public class IfChecker implements IModul {

	private IProgrammTree programmTree;
	private List<MapperElement> mappedElements;
	private IClient client;
	private Mapper mapper;

	public IfChecker(IProgrammTree programmTree, IClient client) {

		this.programmTree = programmTree;
		this.client = client;

		mapper = new Mapper(programmTree);
		this.mappedElements = mapper.process(DEFINITION.IF); 
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.refactor.IRefactor#process()
	 */
	public IProgrammTree process() throws ScriptException {

//		List<MapperElement> mappedVars= mapper.process(DEFINITION.VARIABLE);
//		
//		for (int i = 0; i < mappedVars.size(); i++) {
//			
//			Variable var = (Variable)mappedVars.get(i).getElement().getDefinition();
//			String script = "var " + var.getName();
//			
//			if(!var.getValue().isEmpty()) {
//						
//				if(var.getValue().contains("++") || var.getValue().contains("--") || var.isObject()) {
//					continue;
//				}
//				
//				script += "=" + var.getValue();
//			}
//			
//			client.getJSResult(script);
//		}
		
		
		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement actualElement = mappedElements.get(i);
			If ifStatement = (If) actualElement.getElement().getDefinition();
			IProgrammTree children = actualElement.getElement().getChildren();

			String head = ifStatement.getHeadString();

			Object result = client.getJSResult(head);

			if ((Boolean) result == false) {
				remove(actualElement, children);
			} 
		}

		return programmTree;
	}

	/**
	 * @param actualElement
	 * @param children
	 */
	private void remove(MapperElement actualElement, IProgrammTree children) {
		
		for (int j = 0; j < children.size(); j++) {
			programmTree.removeElementAndAllChildren(children.get(j));
		}

		programmTree.removeElementAndAllChildren(actualElement.getElement());
	}
}
