/**
 * 
 */
package edu.hm.counterobfuscation.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.script.ScriptException;

import org.apache.commons.codec.EncoderException;
import org.junit.Ignore;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import edu.hm.counterobfuscator.client.HTMLUnitClient;
import edu.hm.counterobfuscator.client.IClient;
import edu.hm.counterobfuscator.helper.Scope;
import edu.hm.counterobfuscator.parser.IParser;
import edu.hm.counterobfuscator.parser.ParserFactory;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.parser.tree.Element;
import edu.hm.counterobfuscator.refactor.RefactorFactory;
import edu.hm.counterobfuscator.refactor.modul.InterpreterModul;
import edu.hm.counterobfuscator.types.Ajax;
import edu.hm.counterobfuscator.types.Default;
import edu.hm.counterobfuscator.types.ForWhile;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.types.Call;
import edu.hm.counterobfuscator.types.Return;
import edu.hm.counterobfuscator.types.TYPE;
import edu.hm.counterobfuscator.types.This;
import edu.hm.counterobfuscator.types.Variable;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 20.01.2015
 * 
 * 
 */
public class TestJSInterpreter {

	@Test
	public void VariableTest() throws IOException, IllegalArgumentException, EncoderException,
			ScriptException {

		IParser jsParser = ParserFactory.create("functionTest", true);

		IProgrammTree tree = RefactorFactory.create(jsParser);

		assertNotNull(tree);
		assertEquals(3, tree.size());

		Element t1 = tree.get(0);
		Element t2 = tree.get(1);
		Element t3 = tree.get(2);

		// t1 ---------------------------------------------------------
		assertEquals(t1.getChildren().size(), 3);
		assertNull(t1.getParent());
		assertEquals(t1.getType().getType(), TYPE.FUNCTION);

		Function f1 = (Function) t1.getType();

		assertEquals(f1.getName(), "function1");
		assertEquals(f1.getHead().size(), 1);

		Variable f1HeadVar1 = f1.getHead().get(0);

		assertEquals(f1HeadVar1.getName(), "functionVar1");

		Variable f1v1 = (Variable) t1.getChild(0).getType();
		This f1t1 = (This) t1.getChild(1).getType();
		This f1t2 = (This) t1.getChild(2).getType();

		assertEquals(f1v1.getName(), "var1");
		assertEquals(f1v1.getValue(), "0");

		assertEquals(f1t1.getName(), "['SayHello']");
		// TODO
		assertEquals(f1t1.getValue(), "function(_0x4ebex4){_0x4ebex3++;alert(_0x4ebex2+_0x4ebex4);}");
		assertEquals(t1.getChild(1).getChildren().size(), 1);
		assertEquals(t1.getChild(1).getChildren().get(0).getChildren().size(), 2);

		Default def1 = (Default) t1.getChild(1).getChildren().get(0).getChildren().get(0).getType();
		Default def2 = (Default) t1.getChild(1).getChildren().get(0).getChildren().get(1).getType();

		// TODO
		// assertEquals(def1.getName(), "var2++");
		assertEquals(def2.getName(), "alert(functionVar1+functionVar2)");

		assertEquals(f1t2.getName(), "['GetCount']");
		// TODO
		assertEquals(f1t2.getValue(), "function(){return _0x4ebex3;}");
		assertEquals(t1.getChild(2).getChildren().size(), 1);
		assertEquals(t1.getChild(2).getChildren().get(0).getChildren().size(), 1);

		Return return1 = (Return) t1.getChild(2).getChildren().get(0).getChildren().get(0).getType();
		assertEquals(return1.getName(), "var1");
		// -------------------------------------------------------------

		// t2
		assertEquals(t2.getChildren().size(), 0);
		assertNull(t2.getParent());
		assertEquals(t2.getType().getType(), TYPE.VARIABLE);

		Variable v2 = (Variable) t2.getType();

		assertEquals(v2.getName(), "var2");
		assertEquals(v2.isObject(), true);
		assertEquals(v2.getValue(), "function1");
		assertEquals(v2.getParameter(), "'Message : '");
		// assertEquals(v2.getPos(), new Position(0,12));
		// -------------------------------------------------------------

		// t3
		assertEquals(t3.getChildren().size(), 0);
		assertNull(t3.getParent());
		assertEquals(t3.getType().getType(), TYPE.CALL);

		Call fc3 = (Call) t3.getType();

		assertEquals(fc3.getName(), "var2");
		assertEquals(fc3.getValue(), "'You are welcome.'");
		assertEquals(fc3.getFunction(), "SayHello");
		// assertEquals(v2.getPos(), new Position(0,12));
		// -------------------------------------------------------------

	}

	@Test
	public void VariableTest2() throws IOException, IllegalArgumentException, EncoderException,
			ScriptException {

		IParser jsParser = ParserFactory.create("functionTest2", true);

		RefactorFactory.create(jsParser);

		IProgrammTree tree = jsParser.getProgrammTree();

		assertNotNull(tree);
		assertEquals(1, tree.size());

		Element t0 = tree.get(0);

		// t0 ---------------------------------------------------------
		assertEquals(t0.getChildren().size(), 3);
		assertNull(t0.getParent());

		Function func = (Function) t0.getType();

		assertEquals(func.getType(), TYPE.FUNCTION);
		assertEquals(func.getName(), "");
		assertEquals(func.getHeadString(), "");

		Variable v1 = (Variable) t0.getChild(0).getType();
		assertEquals(v1.getType(), TYPE.VARIABLE);
		assertEquals(v1.getName(), "var1");
		assertEquals(v1.getValue(), "''");

		ForWhile for1 = (ForWhile) t0.getChild(1).getType();
		assertEquals(for1.getType(), TYPE.FOR);
		assertEquals(t0.getChild(1).getChildren().size(), 1);

		Variable for1v1 = (Variable) t0.getChild(1).getChild(0).getType();
		assertEquals(for1v1.getType(), TYPE.VARIABLE);
		assertEquals(for1v1.getName(), "var1");
		assertEquals(for1v1.getValue(),
				"String['fromCharCode'](window['parseInt']('test'['slice'](forVar1,forVar1+2),16)-77)");

		Return v3 = (Return) t0.getChild(2).getType();
		assertEquals(v3.getType(), TYPE.RETURN);
		assertEquals(v3.getName(), "var1");
	}

	@Test
	public void packedTest() throws IOException, IllegalArgumentException, EncoderException,
			ScriptException {

		IParser jsParser = ParserFactory.create("packedTest", true);

		IProgrammTree tree = RefactorFactory.create(jsParser);

		assertNotNull(tree);
		assertEquals(1, tree.size());

		Element t0 = tree.get(0);

		// t0 ---------------------------------------------------------
		assertEquals(t0.getChildren().size(), 0);
		assertNull(t0.getParent());

		Function def = (Function) t0.getType();

		assertEquals(def.isPacked(), true);
		assertEquals(def.getBodyAsString(),
				"$.ajax({url:\"test.html\",context:document.body}).done(function(){$(this).addClass(\"done\")});");

	}

	@Test
	public void tryCatchTest() throws IOException, IllegalArgumentException, EncoderException,
			ScriptException {

		IParser jsParser = ParserFactory.create("tryTest", true);

		IProgrammTree tree = RefactorFactory.create(jsParser);

		assertNotNull(tree);
		assertEquals(tree.size(), 2);

		Element t0 = tree.get(0);
		Variable func = (Variable) t0.getType();

		assertEquals(func.getType(), TYPE.VARIABLE);
		assertEquals(func.getName(), "var1");
		assertEquals(func.getValue(), "3");

		Element t1 = tree.get(1);
		Return returnStatement = (Return) t1.getType();

		assertEquals(returnStatement.getType(), TYPE.RETURN);
		assertEquals(returnStatement.getName(), "var1");

	}

	@Test
	public void ajaxTest() throws IOException, IllegalArgumentException, EncoderException,
			ScriptException {

		IParser jsParser = ParserFactory.create("JScrambler", true);

		IProgrammTree tree = RefactorFactory.create(jsParser);

		assertNotNull(tree);
		assertEquals(tree.size(), 1);

		Element t0 = tree.get(0);

		Ajax def = (Ajax) t0.getType();

		assertEquals(def.getType(), TYPE.AJAX);
		assertEquals(def.getName(), "'getScript'");
		assertEquals(def.getValue(), "'demo_ajax_script.js'");

	}

}
