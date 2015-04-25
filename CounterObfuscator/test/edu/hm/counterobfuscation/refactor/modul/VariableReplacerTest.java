package edu.hm.counterobfuscation.refactor.modul;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.script.ScriptException;

import org.apache.commons.codec.EncoderException;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import edu.hm.counterobfuscator.client.HTMLUnitClient;
import edu.hm.counterobfuscator.client.IClient;
import edu.hm.counterobfuscator.definitions.DEFINITION;
import edu.hm.counterobfuscator.definitions.Default;
import edu.hm.counterobfuscator.definitions.Variable;
import edu.hm.counterobfuscator.parser.IParser;
import edu.hm.counterobfuscator.parser.ParserFactory;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.FunctionRefactor;
import edu.hm.counterobfuscator.refactor.IRefactor;
import edu.hm.counterobfuscator.refactor.modul.FunctionInterpreter;
import edu.hm.counterobfuscator.refactor.modul.FunctionRenamer;
import edu.hm.counterobfuscator.refactor.modul.IModul;
import edu.hm.counterobfuscator.refactor.modul.VariableInterpreter;
import edu.hm.counterobfuscator.refactor.modul.VariableRemover;
import edu.hm.counterobfuscator.refactor.modul.VariableRenamer;
import edu.hm.counterobfuscator.refactor.modul.VariableReplacer;

public class VariableReplacerTest {

	IClient client; 
	
	@Before
	public void before() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		client = new HTMLUnitClient("http://www.google.de", BrowserVersion.FIREFOX_24);
	}
	
	@Test
	public void renamerVariableTest1() throws IllegalArgumentException, IOException, EncoderException, ScriptException {
		
		String input = "var test2='dasIstEinTest';var test = test2;";
		
		IParser parser = ParserFactory.create(input, false);
				
		IModul varReplacerModul = new VariableReplacer(parser.getProgrammTree());
		IProgrammTree tree = varReplacerModul.process(); 
				
		assertEquals(2, tree.size());
		Variable var1 = (Variable)tree.get(0).getDefinition();
		assertEquals("test2", var1.getName());
		Variable var2 = (Variable)tree.get(1).getDefinition();
		assertEquals("test", var2.getName());
		assertEquals("'dasIstEinTest'", var2.getValue());
	}
	
	
}
