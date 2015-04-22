package edu.hm.counterobfuscator.refactor.modul;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import edu.hm.counterobfuscator.client.IClient;
import edu.hm.counterobfuscator.definitions.DEFINITION;
import edu.hm.counterobfuscator.definitions.Variable;
import edu.hm.counterobfuscator.helper.Validate;
import edu.hm.counterobfuscator.parser.Parser;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.mapper.MapperElement;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.02.2015
 * 
 * 
 */
public class AjaxCaller implements IModul {

	private IProgrammTree programmTree;
	private List<MapperElement> mappedElements;
	private InterpreterModul interpreter;
	private List<MapperElement> mappedVariableElements;
	private String jqueryCode;

	public AjaxCaller(IProgrammTree programmTree, IClient client) throws IOException {

		this.programmTree = programmTree;

		interpreter = new InterpreterModul(client, false);
		Mapper mapper = new Mapper(programmTree);
		this.mappedElements = mapper.process(DEFINITION.AJAX);
		this.mappedVariableElements = mapper.process(DEFINITION.VARIABLE);

		this.jqueryCode = "";

		BufferedReader br = new BufferedReader(new FileReader("src/edu/hm/counterobfuscator/refactor/modul/jquery"));
		String line = "";

		while ((line = br.readLine()) != null) {
			jqueryCode += line;
		}

		br.close();
		
		interpreter.setJsScriptBuffer(jqueryCode);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.hm.counterobfuscator.refactor.IRefactor#process()
	 */
	public IProgrammTree process() {

		Validate.notNull(mappedElements);

//		for (int i = 0; i < mappedVariableElements.size(); i++) {
//
//			MapperElement actualElement = mappedVariableElements.get(i);
//			Variable var = (Variable) actualElement.getElement()
//					.getDefinition();
//			String buffer = "var " + var.getName() + "=" + var.getValue() + ";";
//
//			
//		}

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement actualElement = mappedElements.get(i);

			try {
				interpreter.process(actualElement.getElement());
			} catch (Exception e) {

			}
		}
		return programmTree;
	}
}
