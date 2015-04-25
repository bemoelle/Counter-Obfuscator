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
import edu.hm.counterobfuscator.definitions.Variable;
import edu.hm.counterobfuscator.parser.IParser;
import edu.hm.counterobfuscator.parser.ParserFactory;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.FunctionRefactor;
import edu.hm.counterobfuscator.refactor.IRefactor;
import edu.hm.counterobfuscator.refactor.modul.ForLoopChecker;
import edu.hm.counterobfuscator.refactor.modul.FunctionRenamer;
import edu.hm.counterobfuscator.refactor.modul.IModul;
import edu.hm.counterobfuscator.refactor.modul.TryCatchChecker;

public class TryCatchCheckerTest {

	IClient client; 
	
	@Before
	public void before() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		client = new HTMLUnitClient("http://www.google.de", BrowserVersion.FIREFOX_24);
	}
	
	@Test
	public void tryCatchCheckerTest() throws IllegalArgumentException, IOException, EncoderException, ScriptException {
		
		String input = "try{var test1=1;var test2=2;Math.lol();var test3=3;}catch{var test4=0;}";
		
		IParser parser = ParserFactory.create(input, false);
				
		IModul tryCheckerModul = new TryCatchChecker(parser.getProgrammTree(), client);
		IProgrammTree tree = tryCheckerModul.process(); 
				
		assertEquals(3, tree.size());
		Variable var1 = (Variable)tree.get(0).getDefinition();
		assertEquals("test1", var1.getName());
		Variable var2 = (Variable)tree.get(1).getDefinition();
		assertEquals("test2", var2.getName());
		Variable var3 = (Variable)tree.get(2).getDefinition();
		assertEquals("test4", var3.getName());
	}
}
