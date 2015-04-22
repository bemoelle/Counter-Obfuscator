/**
 * 
 */
package edu.hm.counterobfuscation.parser;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.script.ScriptException;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import edu.hm.counterobfuscator.DeObfuscatorFactory;
import edu.hm.counterobfuscator.definitions.Ajax;
import edu.hm.counterobfuscator.definitions.Call;
import edu.hm.counterobfuscator.definitions.DEFINITION;
import edu.hm.counterobfuscator.definitions.Default;
import edu.hm.counterobfuscator.definitions.ForWhile;
import edu.hm.counterobfuscator.definitions.Function;
import edu.hm.counterobfuscator.definitions.Return;
import edu.hm.counterobfuscator.definitions.This;
import edu.hm.counterobfuscator.definitions.Variable;
import edu.hm.counterobfuscator.parser.IParser;
import edu.hm.counterobfuscator.parser.ParserFactory;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.refactor.RefactorFactory;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 20.01.2015
 * 
 * 
 */
public class TestDeobfuscator {

	String path = "test/testdata/";
	String url = "http://www.google.com/";
	boolean isFile = true;

	@Test
	public void javascriptobfuscatorTest() throws IOException,
			IllegalArgumentException, EncoderException, ScriptException {

		DeObfuscatorFactory deObfuscatorFactory = new DeObfuscatorFactory(path
				+ "javascriptobfuscator", true, url, null);

		IProgrammTree resultTree = deObfuscatorFactory.create();

		assertNotNull(resultTree);
		assertEquals(3, resultTree.size());

		Element t1 = resultTree.get(0);
		Element t2 = resultTree.get(1);
		Element t3 = resultTree.get(2);

		// t1 ---------------------------------------------------------
		assertEquals(t1.getChildren().size(), 3);
		assertNull(t1.getParent());
		assertEquals(t1.getDefinition().getDefinition(), DEFINITION.FUNCTION);

		Function f1 = (Function) t1.getDefinition();

		assertEquals(f1.getName(), "object1");
		assertEquals(f1.getHead().size(), 1);

		Variable f1HeadVar1 = f1.getHead().get(0);

		assertEquals(f1HeadVar1.getName(), "functionVar1");

		Variable f1v1 = (Variable) t1.getChild(0).getDefinition();
		This f1t1 = (This) t1.getChild(1).getDefinition();
		This f1t2 = (This) t1.getChild(2).getDefinition();

		assertEquals(f1v1.getName(), "var1");
		assertEquals(f1v1.getValue(), "0");
		assertEquals(f1t1.getName(), "SayHello");

		assertEquals(t1.getChild(1).getChildren().size(), 1);
		assertEquals(t1.getChild(1).getChildren().get(0).getChildren().size(),
				2);

		Variable def1 = (Variable) t1.getChild(1).getChildren().get(0)
				.getChildren().get(0).getDefinition();
		Default def2 = (Default) t1.getChild(1).getChildren().get(0)
				.getChildren().get(1).getDefinition();

		assertEquals(def1.getName(), "var1");
		assertEquals(def1.getValue(), "++");
		assertEquals(def2.getName(), "alert(functionVar1+functionVar2);");

		assertEquals(f1t2.getName(), "GetCount");

		assertEquals(t1.getChild(2).getChildren().size(), 1);
		assertEquals(t1.getChild(2).getChildren().get(0).getChildren().size(),
				1);

		Return return1 = (Return) t1.getChild(2).getChildren().get(0)
				.getChildren().get(0).getDefinition();
		assertEquals(return1.getName(), "var1");
		// -------------------------------------------------------------

		// t2
		assertEquals(t2.getChildren().size(), 0);
		assertNull(t2.getParent());
		assertEquals(t2.getDefinition().getDefinition(), DEFINITION.VARIABLE);

		Variable v2 = (Variable) t2.getDefinition();

		assertEquals(v2.getName(), "var2");
		assertEquals(v2.isObject(), true);
		assertEquals(v2.getValue(), "object1");
		assertEquals(v2.getParameter(), "'Message : '");
		// -------------------------------------------------------------

		// t3
		assertEquals(t3.getChildren().size(), 0);
		assertNull(t3.getParent());
		assertEquals(t3.getDefinition().getDefinition(), DEFINITION.CALL);

		Call fc3 = (Call) t3.getDefinition();

		assertEquals(fc3.getName(), "var2");
		assertEquals(fc3.getValue(), "'You are welcome.'");
		assertEquals(fc3.getFunction(), "SayHello");
		// -------------------------------------------------------------

	}

	@Test
	public void jsobfuscateTest() throws IOException, IllegalArgumentException,
			EncoderException, ScriptException {

		DeObfuscatorFactory deObfuscatorFactory = new DeObfuscatorFactory(path
				+ "jsobfuscate", true, url, null);

		IProgrammTree resultTree = deObfuscatorFactory.create();

		assertNotNull(resultTree);
		assertEquals(3, resultTree.size());

		Element t1 = resultTree.get(0);
		Element t2 = resultTree.get(1);
		Element t3 = resultTree.get(2);

		// t1 ---------------------------------------------------------
		assertEquals(t1.getChildren().size(), 3);
		assertNull(t1.getParent());
		assertEquals(t1.getDefinition().getDefinition(), DEFINITION.FUNCTION);

		Function f1 = (Function) t1.getDefinition();

		assertEquals(f1.getName(), "object1");
		assertEquals(f1.getHead().size(), 1);

		Variable f1HeadVar1 = f1.getHead().get(0);

		assertEquals(f1HeadVar1.getName(), "functionVar1");

		Variable f1v1 = (Variable) t1.getChild(0).getDefinition();
		This f1t1 = (This) t1.getChild(1).getDefinition();
		This f1t2 = (This) t1.getChild(2).getDefinition();

		assertEquals(f1v1.getName(), "var1");
		assertEquals(f1v1.getValue(), "0");
		assertEquals(f1t1.getName(), ".SayHello");

		assertEquals(t1.getChild(1).getChildren().size(), 1);
		assertEquals(t1.getChild(1).getChildren().get(0).getChildren().size(),
				2);

		Variable def1 = (Variable) t1.getChild(1).getChildren().get(0)
				.getChildren().get(0).getDefinition();
		Default def2 = (Default) t1.getChild(1).getChildren().get(0)
				.getChildren().get(1).getDefinition();

		assertEquals(def1.getName(), "var1");
		assertEquals(def1.getValue(), "++");
		assertEquals(def2.getName(), "alert(functionVar1+functionVar2)");

		assertEquals(f1t2.getName(), ".GetCount");

		assertEquals(t1.getChild(2).getChildren().size(), 1);
		assertEquals(t1.getChild(2).getChildren().get(0).getChildren().size(),
				1);

		Return return1 = (Return) t1.getChild(2).getChildren().get(0)
				.getChildren().get(0).getDefinition();
		assertEquals(return1.getName(), "var1");
		// -------------------------------------------------------------

		// t2
		assertEquals(t2.getChildren().size(), 0);
		assertNull(t2.getParent());
		assertEquals(t2.getDefinition().getDefinition(), DEFINITION.VARIABLE);

		Variable v2 = (Variable) t2.getDefinition();

		assertEquals(v2.getName(), "var2");
		assertEquals(v2.isObject(), true);
		assertEquals(v2.getValue(), "object1");
		assertEquals(v2.getParameter(), "'Message : '");
		// -------------------------------------------------------------

		// t3
		assertEquals(t3.getChildren().size(), 0);
		assertNull(t3.getParent());
		assertEquals(t3.getDefinition().getDefinition(), DEFINITION.CALL);

		Call fc3 = (Call) t3.getDefinition();

		assertEquals(fc3.getName(), "var2");
		assertEquals(fc3.getValue(), "'You are welcome.'");
		assertEquals(fc3.getFunction(), "SayHello");

	}

	@Test
	public void javascriptobfuscatorJsobfuscateTest() throws IOException,
			IllegalArgumentException, EncoderException, ScriptException {

		DeObfuscatorFactory deObfuscatorFactory = new DeObfuscatorFactory(path
				+ "javascriptobfuscatorJsobfuscate", true, url, null);

		IProgrammTree resultTree = deObfuscatorFactory.create();

		assertNotNull(resultTree);
		assertEquals(3, resultTree.size());

		Element t1 = resultTree.get(0);
		Element t2 = resultTree.get(1);
		Element t3 = resultTree.get(2);

		// t1 ---------------------------------------------------------
		assertEquals(t1.getChildren().size(), 3);
		assertNull(t1.getParent());
		assertEquals(t1.getDefinition().getDefinition(), DEFINITION.FUNCTION);

		Function f1 = (Function) t1.getDefinition();

		assertEquals(f1.getName(), "object1");
		assertEquals(f1.getHead().size(), 1);

		Variable f1HeadVar1 = f1.getHead().get(0);

		assertEquals(f1HeadVar1.getName(), "functionVar1");

		Variable f1v1 = (Variable) t1.getChild(0).getDefinition();
		This f1t1 = (This) t1.getChild(1).getDefinition();
		This f1t2 = (This) t1.getChild(2).getDefinition();

		assertEquals(f1v1.getName(), "var1");
		assertEquals(f1v1.getValue(), "0");
		assertEquals(f1t1.getName(), "SayHello");

		assertEquals(t1.getChild(1).getChildren().size(), 1);
		assertEquals(t1.getChild(1).getChildren().get(0).getChildren().size(),
				2);

		Variable def1 = (Variable) t1.getChild(1).getChildren().get(0)
				.getChildren().get(0).getDefinition();
		Default def2 = (Default) t1.getChild(1).getChildren().get(0)
				.getChildren().get(1).getDefinition();

		assertEquals(def1.getName(), "var1");
		assertEquals(def1.getValue(), "++");
		assertEquals(def2.getName(), "alert(functionVar1+functionVar2)");

		assertEquals(f1t2.getName(), "GetCount");

		assertEquals(t1.getChild(2).getChildren().size(), 1);
		assertEquals(t1.getChild(2).getChildren().get(0).getChildren().size(),
				1);

		Return return1 = (Return) t1.getChild(2).getChildren().get(0)
				.getChildren().get(0).getDefinition();
		assertEquals(return1.getName(), "var1");
		// -------------------------------------------------------------

		// t2
		assertEquals(t2.getChildren().size(), 0);
		assertNull(t2.getParent());
		assertEquals(t2.getDefinition().getDefinition(), DEFINITION.VARIABLE);

		Variable v2 = (Variable) t2.getDefinition();

		assertEquals(v2.getName(), "var2");
		assertEquals(v2.isObject(), true);
		assertEquals(v2.getValue(), "object1");
		assertEquals(v2.getParameter(), "'Message : '");
		// -------------------------------------------------------------

		// t3
		assertEquals(t3.getChildren().size(), 0);
		assertNull(t3.getParent());
		assertEquals(t3.getDefinition().getDefinition(), DEFINITION.CALL);

		Call fc3 = (Call) t3.getDefinition();

		assertEquals(fc3.getName(), "var2");
		assertEquals(fc3.getValue(), "'You are welcome.'");
		assertEquals(fc3.getFunction(), "SayHello");

	}

	@Test
	public void variableTest() throws IOException, IllegalArgumentException,
			EncoderException, ScriptException {

		DeObfuscatorFactory deObfuscatorFactory = new DeObfuscatorFactory(path
				+ "variableTest", true, url, null);

		IProgrammTree resultTree = deObfuscatorFactory.create();

		resultTree.print();

		assertNotNull(resultTree);
		assertEquals(1, resultTree.size());

		Element t0 = resultTree.get(0);

		// t0 ---------------------------------------------------------
		assertEquals(t0.getChildren().size(), 3);
		assertNull(t0.getParent());

		Function func = (Function) t0.getDefinition();

		assertEquals(func.getDefinition(), DEFINITION.FUNCTION);
		assertEquals(func.getName(), "");
		assertEquals(func.getHeadString(), "");

		Variable v1 = (Variable) t0.getChild(0).getDefinition();
		assertEquals(v1.getDefinition(), DEFINITION.VARIABLE);
		assertEquals(v1.getName(), "var1");
		assertEquals(v1.getValue(), "''");

		ForWhile for1 = (ForWhile) t0.getChild(1).getDefinition();
		assertEquals(for1.getDefinition(), DEFINITION.FOR);
		assertEquals(t0.getChild(1).getChildren().size(), 1);

		Variable for1v1 = (Variable) t0.getChild(1).getChild(0).getDefinition();
		assertEquals(for1v1.getDefinition(), DEFINITION.VARIABLE);
		assertEquals(for1v1.getName(), "var1");
		assertEquals(
				for1v1.getValue(),
				"String['fromCharCode'](window['parseInt']('test'['slice'](forVar1, forVar1 + 2), 16) - 77)");

		Return v3 = (Return) t0.getChild(2).getDefinition();
		assertEquals(v3.getDefinition(), DEFINITION.RETURN);
		assertEquals(v3.getName(), "var1");
	}

	@Test
	public void nestedFunctionsTest() throws IOException,
			IllegalArgumentException, EncoderException, ScriptException {

		DeObfuscatorFactory deObfuscatorFactory = new DeObfuscatorFactory(path
				+ "nestedFunctions", true, url, null);

		IProgrammTree resultTree = deObfuscatorFactory.create();

		assertNotNull(resultTree);
		assertEquals(1, resultTree.size());

		Element t0 = resultTree.get(0);
		assertEquals(1, t0.getChildren().size());
		assertEquals("functionVar1", ((Function) t0.getDefinition()).getHead()
				.get(0).getName());

		Element t0_0 = t0.getChild(0);
		assertEquals(1, t0_0.getChildren().size());
		assertEquals("functionVar2", ((Function) t0_0.getDefinition())
				.getHead().get(0).getName());

		Element t0_0_0 = t0_0.getChild(0);
		assertEquals(1, t0_0_0.getChildren().size());
		assertEquals("functionVar3", ((Function) t0_0_0.getDefinition())
				.getHead().get(0).getName());

		Return ret = (Return) t0_0_0.getChild(0).getDefinition();
		assertEquals("functionVar1+functionVar2+functionVar3", ret.getName());

		resultTree.print();

	}

	@Test
	public void renameVarInNestedFunctionTest() throws IOException,
			IllegalArgumentException, EncoderException, ScriptException {

		DeObfuscatorFactory deObfuscatorFactory = new DeObfuscatorFactory(path
				+ "renameVarInNestedFunction", true, url, null);

		IProgrammTree resultTree = deObfuscatorFactory.create();

		assertNotNull(resultTree);
		assertEquals(1, resultTree.size());

		resultTree.print();

		Element t0 = resultTree.get(0);
		assertEquals(2, t0.getChildren().size());

		// function
		Element t2 = t0.getChild(0);
		assertEquals(1, t2.getChildren().size());

		Element t2_0 = t2.getChild(0);
		assertEquals(1, t2_0.getChildren().size());

		Element t2_0_0 = t2_0.getChild(0);
		assertEquals("0+0", t2_0_0.getDefinition().getName());

		// return
		Element t3 = t0.getChild(1);
		assertEquals("0", t3.getDefinition().getName());

	}

	@Test
	public void ifTest() throws IOException, IllegalArgumentException,
			EncoderException, ScriptException {

		DeObfuscatorFactory deObfuscatorFactory = new DeObfuscatorFactory(path
				+ "ifTest", true, url, null);

		IProgrammTree resultTree = deObfuscatorFactory.create();

		assertNotNull(resultTree);
		assertEquals(1, resultTree.size());

		Element t0 = resultTree.get(0);
		assertEquals(1, t0.getChildren().size());
		assertEquals("sdsdsdsds", t0.getChild(0).getDefinition().getName());

		resultTree.print();

	}

	@Test
	public void whileTest() throws IOException, IllegalArgumentException,
			EncoderException, ScriptException {

		DeObfuscatorFactory deObfuscatorFactory = new DeObfuscatorFactory(path
				+ "whileTest", true, url, null);

		IProgrammTree resultTree = deObfuscatorFactory.create();

		assertNotNull(resultTree);
		assertEquals(1, resultTree.size());

		Element t0 = resultTree.get(0);
		assertEquals(3, t0.getChildren().size());

		// Var
		assertEquals("sdsdsdsds", t0.getChild(0).getDefinition().getName());

		// while
		Element t0_1 = t0.getChild(1);
		assertEquals(1, t0_1.getChildren().size());

		// return
		assertEquals("sdsdsdsds", t0.getChild(2).getDefinition().getName());

		resultTree.print();

	}

	@Test
	public void forTest() throws IOException, IllegalArgumentException,
			EncoderException, ScriptException {

		DeObfuscatorFactory deObfuscatorFactory = new DeObfuscatorFactory(path
				+ "forTest", true, url, null);

		IProgrammTree resultTree = deObfuscatorFactory.create();

		assertNotNull(resultTree);
		assertEquals(1, resultTree.size());

		Element t0 = resultTree.get(0);
		assertEquals(5, t0.getChildren().size());

		// var
		Element t0_0 = t0.getChild(0);
		assertEquals("sdsdsds", t0_0.getDefinition().getName());

		// for
		Element t0_1 = resultTree.get(1);
		assertEquals(1, t0_1.getChildren().size());

		// var
		Element t0_2 = resultTree.get(2);
		assertEquals("sdsdsdsd", t0_2.getDefinition().getName());

		// for
		Element t0_3 = resultTree.get(3);
		assertEquals(1, t0_3.getChildren().size());

		// return
		Element t0_4 = resultTree.get(4);
		assertEquals("sdsdsdsd", t0_4.getDefinition().getName());

		resultTree.print();

	}

	@Test
	public void tryCatchTest() throws IOException, IllegalArgumentException,
			EncoderException, ScriptException {

		DeObfuscatorFactory deObfuscatorFactory = new DeObfuscatorFactory(path
				+ "tryTest", true, url, null);

		IProgrammTree resultTree = deObfuscatorFactory.create();

		assertNotNull(resultTree);
		assertEquals(resultTree.size(), 2);

		Element t0 = resultTree.get(0);
		Variable func = (Variable) t0.getDefinition();

		assertEquals(func.getDefinition(), DEFINITION.VARIABLE);
		assertEquals(func.getName(), "var1");
		assertEquals(func.getValue(), "3");

		Element t1 = resultTree.get(1);
		Return returnStatement = (Return) t1.getDefinition();

		assertEquals(returnStatement.getDefinition(), DEFINITION.RETURN);
		assertEquals(returnStatement.getName(), "var1");
	}

	@Test
	public void interpretationTest() throws IOException,
			IllegalArgumentException, EncoderException, ScriptException {

		DeObfuscatorFactory deObfuscatorFactory = new DeObfuscatorFactory(path
				+ "interpretationTest", true, url, null);

		IProgrammTree resultTree = deObfuscatorFactory.create();

		assertNotNull(resultTree);
		assertEquals(1, resultTree.size());

		Element t0 = resultTree.get(0);
		assertEquals(1, t0.getChildren().size());
		
		Element t0_0 = t0.getChild(0);
		assertEquals("'fromCharCode'", t0_0.getDefinition().getName());
		
		resultTree.print();

	}
	
	@Test
	public void varRemoveTest() throws IOException, IllegalArgumentException,
			EncoderException, ScriptException {

		DeObfuscatorFactory deObfuscatorFactory = new DeObfuscatorFactory(path
				+ "varRemoveTest", true, url, null);

		IProgrammTree resultTree = deObfuscatorFactory.create();

		assertNotNull(resultTree);
		assertEquals(0, resultTree.size());

		resultTree.print();

	}

	@Test
	public void ajaxTest() throws IOException, IllegalArgumentException,
			EncoderException, ScriptException {

		DeObfuscatorFactory deObfuscatorFactory = new DeObfuscatorFactory(path
				+ "ajaxTest", true, "http://code.jquery.com", null);

		IProgrammTree resultTree = deObfuscatorFactory.create();

		assertNotNull(resultTree);

		resultTree.print();

	}

	@Test
	public void emptyTest() throws IOException, IllegalArgumentException,
			EncoderException, ScriptException {

		DeObfuscatorFactory deObfuscatorFactory = new DeObfuscatorFactory(path
				+ "emptyTest", true, url, null);

		IProgrammTree resultTree = deObfuscatorFactory.create();

		assertNotNull(resultTree);
		assertEquals(0, resultTree.size());

		resultTree.print();

	}
	//
	// @Test
	// public void packedTest() throws IOException, IllegalArgumentException,
	// EncoderException,
	// ScriptException {
	//
	// IParser jsParser = ParserFactory.create("jsobfuscate", true);
	//
	// IProgrammTree tree = RefactorFactory.create(jsParser);
	//
	// assertNotNull(tree);
	// assertEquals(3, tree.size());
	//
	// Element t1 = tree.get(0);
	// Element t2 = tree.get(1);
	// Element t3 = tree.get(2);
	//
	// // t1 ---------------------------------------------------------
	// assertEquals(t1.getChildren().size(), 3);
	// assertNull(t1.getParent());
	// assertEquals(t1.getDefinition().getDefinition(), DEFINITION.FUNCTION);
	//
	// Function f1 = (Function) t1.getDefinition();
	//
	// assertEquals(f1.getName(), "function1");
	// assertEquals(f1.getHead().size(), 1);
	//
	// Variable f1HeadVar1 = f1.getHead().get(0);
	//
	// assertEquals(f1HeadVar1.getName(), "functionVar1");
	//
	// Variable f1v1 = (Variable) t1.getChild(0).getDefinition();
	// This f1t1 = (This) t1.getChild(1).getDefinition();
	// This f1t2 = (This) t1.getChild(2).getDefinition();
	//
	// assertEquals(f1v1.getName(), "var1");
	// assertEquals(f1v1.getValue(), "0");
	// assertEquals(f1t1.getName(), ".SayHello");
	//
	// assertEquals(t1.getChild(1).getChildren().size(), 1);
	// assertEquals(t1.getChild(1).getChildren().get(0).getChildren().size(),
	// 2);
	//
	// Variable def1 = (Variable)
	// t1.getChild(1).getChildren().get(0).getChildren().get(0).getDefinition();
	// Default def2 = (Default)
	// t1.getChild(1).getChildren().get(0).getChildren().get(1).getDefinition();
	//
	// assertEquals(def1.getName(), "var1");
	// assertEquals(def1.getValue(), "++");
	// assertEquals(def2.getName(), "alert(functionVar1+functionVar2)");
	//
	// assertEquals(f1t2.getName(), ".GetCount");
	//
	// assertEquals(t1.getChild(2).getChildren().size(), 1);
	// assertEquals(t1.getChild(2).getChildren().get(0).getChildren().size(),
	// 1);
	//
	// Return return1 = (Return)
	// t1.getChild(2).getChildren().get(0).getChildren().get(0).getDefinition();
	// assertEquals(return1.getName(), "var1");
	// // -------------------------------------------------------------
	//
	// // t2
	// assertEquals(t2.getChildren().size(), 0);
	// assertNull(t2.getParent());
	// assertEquals(t2.getDefinition().getDefinition(), DEFINITION.VARIABLE);
	//
	// Variable v2 = (Variable) t2.getDefinition();
	//
	// assertEquals(v2.getName(), "var2");
	// assertEquals(v2.isObject(), true);
	// assertEquals(v2.getValue(), "function1");
	// assertEquals(v2.getParameter(), "'Message : '");
	// // -------------------------------------------------------------
	//
	// // t3
	// assertEquals(t3.getChildren().size(), 0);
	// assertNull(t3.getParent());
	// assertEquals(t3.getDefinition().getDefinition(), DEFINITION.CALL);
	//
	// Call fc3 = (Call) t3.getDefinition();
	//
	// assertEquals(fc3.getName(), "var2");
	// assertEquals(fc3.getValue(), "'You are welcome.'");
	// assertEquals(fc3.getFunction(), "SayHello");
	//
	// }
	//
	// @Test
	// public void tryCatchTest() throws IOException, IllegalArgumentException,
	// EncoderException,
	// ScriptException {
	//
	// IParser jsParser = ParserFactory.create("tryTest", true);
	//
	// IProgrammTree tree = RefactorFactory.create(jsParser);
	//
	// assertNotNull(tree);
	// assertEquals(tree.size(), 2);
	//
	// Element t0 = tree.get(0);
	// Variable func = (Variable) t0.getDefinition();
	//
	// assertEquals(func.getDefinition(), DEFINITION.VARIABLE);
	// assertEquals(func.getName(), "var1");
	// assertEquals(func.getValue(), "3");
	//
	// Element t1 = tree.get(1);
	// Return returnStatement = (Return) t1.getDefinition();
	//
	// assertEquals(returnStatement.getDefinition(), DEFINITION.RETURN);
	// assertEquals(returnStatement.getName(), "var1");
	//
	// }
	//
	// @Test
	// public void ajaxTest() throws IOException, IllegalArgumentException,
	// EncoderException,
	// ScriptException {
	//
	// IParser jsParser = ParserFactory.create("JScrambler", true);
	//
	// IProgrammTree tree = RefactorFactory.create(jsParser);
	//
	// assertNotNull(tree);
	// assertEquals(tree.size(), 1);
	//
	// Element t0 = tree.get(0);
	//
	// Ajax def = (Ajax) t0.getDefinition();
	//
	// assertEquals(def.getDefinition(), DEFINITION.AJAX);
	// assertEquals(def.getName(), "'getScript'");
	// assertEquals(def.getValue(), "'demo_ajax_script.js'");
	//
	// }

}
