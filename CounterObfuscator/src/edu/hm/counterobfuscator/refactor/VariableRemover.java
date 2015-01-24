package edu.hm.counterobfuscator.refactor;

import java.util.List;
import javax.script.ScriptException;

import edu.hm.counterobfuscator.helper.Setting;
import edu.hm.counterobfuscator.mapper.Mapper;
import edu.hm.counterobfuscator.mapper.MapperElement;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.parser.tree.ValueExtractor;
import edu.hm.counterobfuscator.types.TYPE;
import edu.hm.counterobfuscator.types.Variable;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 22.01.2015
 * 
 * 
 */
public class VariableRemover implements IRefactor {

	private IProgrammTree			programmTree;
	private List<MapperElement>	mappedElements;

	public VariableRemover(IProgrammTree programmTree, Setting setting) {

		this.programmTree = programmTree.reverseOrder();

		Mapper mapper = new Mapper(programmTree, TYPE.VARIABLE);
		mapper.process();
		this.mappedElements = mapper.getMappedElements();
		// ------------
	}

	/* (non-Javadoc)
	 * @see edu.hm.counterobfuscator.refactor.IRefactor#process()
	 */
	public IProgrammTree process() throws ScriptException {

		removeVars();
		return programmTree.reverseOrder();
	}

	/**
	 * 
	 */
	public void removeVars() {

		for (int i = 0; i < mappedElements.size(); i++) {

			MapperElement me = mappedElements.get(i);
			String nameLookingFor = me.getElement().getType().getName();

			int refCounter = 0;
			for (int j = 0; j < programmTree.size(); j++) {

				Element actualElement = programmTree.get(j);

				String value = ValueExtractor.getValue(actualElement);
				if (value.indexOf(nameLookingFor) > -1) {
					refCounter++;
				}

				String name = ValueExtractor.getName(actualElement);
				if (name.indexOf(nameLookingFor) > -1) {

					if (actualElement.getType().getType() == TYPE.VARIABLE) {

						Variable var = (Variable) actualElement.getType();

						if (refCounter == 0) {

							programmTree.removeElement(actualElement);
							
							Element parent = actualElement.getParent();
							if(parent != null) {
								parent.getChildren().removeElement(actualElement);
							}
						}

						if (!var.isExecutable()) {
							refCounter++;
						}
					}
					else {
						refCounter++;
					}
				}
			}
		}

	}

}
