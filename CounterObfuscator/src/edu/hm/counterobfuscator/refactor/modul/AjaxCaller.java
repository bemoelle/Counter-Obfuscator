package edu.hm.counterobfuscator.refactor.modul;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import edu.hm.counterobfuscator.client.IClient;
import edu.hm.counterobfuscator.definitions.DEFINITION;
import edu.hm.counterobfuscator.helper.Validate;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.mapper.Mapper;
import edu.hm.counterobfuscator.parser.tree.mapper.MapperElement;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 05.02.2015
 * 
 *       to refactor ajax calls
 */
public class AjaxCaller implements IModul {

	private IProgrammTree			programmTree;
	private List<MapperElement>	mappedElements;
	private InterpreterModul		interpreter;
	private String						jqueryCode;

	public AjaxCaller(IProgrammTree programmTree, IClient client) throws IOException {

		this.programmTree = programmTree;

		interpreter = new InterpreterModul(client, false);
		Mapper mapper = new Mapper(programmTree);
		this.mappedElements = mapper.process(DEFINITION.AJAX);

		this.jqueryCode = "";

		BufferedReader br = new BufferedReader(new FileReader(
				"src/edu/hm/counterobfuscator/refactor/modul/jquery"));
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
