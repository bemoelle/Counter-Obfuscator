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

import edu.hm.counterobfuscator.HTMLUnitClient;
import edu.hm.counterobfuscator.IClient;
import edu.hm.counterobfuscator.helper.Position;
import edu.hm.counterobfuscator.interpreter.JSInterpreter;
import edu.hm.counterobfuscator.interpreter.JSInterpreterFactory;
import edu.hm.counterobfuscator.parser.IJSParser;
import edu.hm.counterobfuscator.parser.JSParserFactory;
import edu.hm.counterobfuscator.parser.tree.ITypeTree;
import edu.hm.counterobfuscator.parser.tree.TypeTreeElement;
import edu.hm.counterobfuscator.types.Default;
import edu.hm.counterobfuscator.types.Function;
import edu.hm.counterobfuscator.types.FunctionCall;
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

	@Test @Ignore
	public void test() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		IClient client = new HTMLUnitClient("http://www.google.com/", BrowserVersion.FIREFOX_24);

		String javaScript = "var _0xd237 = ['test', 'test2'];";

		String testScript = "_0xd237[1]";

		Object result = client.getJSResult(javaScript + testScript);
		
		assertEquals(result, "'test2'");

	}

	@Test
	public void VariableTest() throws IOException, IllegalArgumentException, EncoderException,
			ScriptException {
		IJSParser jsParser = JSParserFactory.create("functionTest");

		JSInterpreterFactory.create(jsParser);

		ITypeTree tree = jsParser.getProgrammTree();

		assertNotNull(tree);
		assertEquals(4, tree.size());

		TypeTreeElement t0 = tree.get(0);
		TypeTreeElement t1 = tree.get(1);
		TypeTreeElement t2 = tree.get(2);
		TypeTreeElement t3 = tree.get(3);

		// t0 ---------------------------------------------------------
		assertEquals(t0.getChildren().size(), 0);
		assertNull(t0.getParent());
		assertEquals(t0.getType().getType(), TYPE.VARIABLE);

		Variable v0 = (Variable) t0.getType();

		assertEquals(v0.getName(), "var1");
		assertEquals(v0.getValue(), "['SayHello','GetCount','Message : ','You are welcome.']");
		assertEquals(v0.getPos(), new Position(0, 12));
		// assertEquals(v0.getNoExe(), false);
		// assertEquals(v0.isArray(), true);
		// -------------------------------------------------------------

		// t1 ---------------------------------------------------------
		assertEquals(t1.getChildren().size(), 3);
		assertNull(t1.getParent());
		assertEquals(t1.getType().getType(), TYPE.FUNCTION);

		Function f1 = (Function) t1.getType();

		assertEquals(f1.getName(), "function1");
		assertEquals(f1.getHead().size(), 1);

		Variable f1HeadVar1 = f1.getHead().get(0);

		assertEquals(f1HeadVar1.getName(), "funcVar1");
		
		
		Variable f1v1 = (Variable) t1.getChild(0).getType();
		This f1t1 = (This) t1.getChild(1).getType();
		This f1t2 = (This) t1.getChild(2).getType();
		
		assertEquals(f1v1.getName(), "var2");
		assertEquals(f1v1.getValue(), "0.0");
		
		assertEquals(f1t1.getName(), "['SayHello']");
		assertEquals(f1t1.getValue(), "function(_0x4ebex4){_0x4ebex3++;alert(_0x4ebex2+_0x4ebex4);}");
		assertEquals(t1.getChild(1).getChildren().size(), 1);
		assertEquals(t1.getChild(1).getChildren().get(0).getChildren().size(), 2);
		
		Default def1 = (Default)t1.getChild(1).getChildren().get(0).getChildren().get(0).getType();
		Default def2 = (Default)t1.getChild(1).getChildren().get(0).getChildren().get(1).getType();
		
	//	assertEquals(def1.getName(), "var2++");
		assertEquals(def2.getName(), "alert(funcVar1+funcVar2)");
		
		assertEquals(f1t2.getName(), "['GetCount']");
		assertEquals(f1t2.getValue(), "function(){return _0x4ebex3;}");
		assertEquals(t1.getChild(2).getChildren().size(), 1);
		assertEquals(t1.getChild(2).getChildren().get(0).getChildren().size(), 1);
		
		Return return1 = (Return)t1.getChild(2).getChildren().get(0).getChildren().get(0).getType();
		assertEquals(return1.getName(), "var2");
		// -------------------------------------------------------------

		// t2
		assertEquals(t2.getChildren().size(), 0);
		assertNull(t2.getParent());
		assertEquals(t2.getType().getType(), TYPE.VARIABLE);

		Variable v2 = (Variable) t2.getType();

		assertEquals(v2.getName(), "var3");
		assertEquals(v2.isObject(), true);
		assertEquals(v2.getValue(), "NewObject");
		assertEquals(v2.getParameter(), "'Message : '");
		// assertEquals(v2.getPos(), new Position(0,12));
		// -------------------------------------------------------------

		// t3
		assertEquals(t3.getChildren().size(), 0);
		assertNull(t3.getParent());
		assertEquals(t3.getType().getType(), TYPE.FUNCTIONCALL);

		FunctionCall fc3 = (FunctionCall) t3.getType();

		assertEquals(fc3.getName(), "var3");
		assertEquals(fc3.getValue(), "'You are welcome.'");
		assertEquals(fc3.getFunction(), "SayHello");
		// assertEquals(v2.getPos(), new Position(0,12));
		// -------------------------------------------------------------

	}
	
	@Test
	public void VariableTest2() throws IOException, IllegalArgumentException, EncoderException,
			ScriptException {
		IJSParser jsParser = JSParserFactory.create("functionTest2");

		JSInterpreterFactory.create(jsParser);

		ITypeTree tree = jsParser.getProgrammTree();

		assertNotNull(tree);
		assertEquals(1, tree.size());

		TypeTreeElement t0 = tree.get(0);
		

		// t0 ---------------------------------------------------------
		assertEquals(t0.getChildren().size(), 12);
		assertNull(t0.getParent());
		
		Function func = (Function)t0.getType();
		
		assertEquals(func.getType(), TYPE.FUNCTION);
		assertEquals(func.getName(), "");
		assertEquals(func.getHeadString(), "");
		
		Variable v1 = (Variable)t0.getChild(0).getType();
		assertEquals(v1.getType(), TYPE.VARIABLE);
		assertEquals(v1.getName(), "var1");
		assertEquals(v1.getValue(), "'b1bcb0c2bab2bbc17bb9bcb0aec1b6bcbb8a74b5c1c1bd877c7cc1b2c0c17bb1b2'");
				
		Variable v2 = (Variable)t0.getChild(1).getType();
		assertEquals(v2.getType(), TYPE.VARIABLE);
		assertEquals(v2.getName(), "var2");
		assertEquals(v2.getValue(), "199");
		
		Variable v3 = (Variable)t0.getChild(2).getType();
		assertEquals(v3.getType(), TYPE.VARIABLE);
		assertEquals(v3.getName(), "var3");
		assertEquals(v3.getValue(), "window");
		
		Variable v4 = (Variable)t0.getChild(3).getType();
		assertEquals(v4.getType(), TYPE.VARIABLE);
		assertEquals(v4.getName(), "var4");
		assertEquals(v4.getValue(), "String");
		
		Variable v5 = (Variable)t0.getChild(4).getType();
		assertEquals(v5.getType(), TYPE.VARIABLE);
		assertEquals(v5.getName(), "var5");
		assertEquals(v5.getValue(), "'replace'");
		
		Variable v6 = (Variable)t0.getChild(5).getType();
		assertEquals(v6.getType(), TYPE.VARIABLE);
		assertEquals(v6.getName(), "var6");
		assertEquals(v6.getValue(), "'parseInt'");
		
		Variable v7 = (Variable)t0.getChild(6).getType();
		assertEquals(v7.getType(), TYPE.VARIABLE);
		assertEquals(v7.getName(), "var7");
		assertEquals(v7.getValue(), "'fromCharCode'");
		
		Variable v8 = (Variable)t0.getChild(7).getType();
		assertEquals(v8.getType(), TYPE.VARIABLE);
		assertEquals(v8.getName(), "var8");
		assertEquals(v8.getValue(), "'b1bcb0c2bab2bbc17bb9bcb0aec1b6bcbb8a74b5c1c1bd877c7cc1b2c0c17bb1b2'");
		
		
		
		

	

	}

}
