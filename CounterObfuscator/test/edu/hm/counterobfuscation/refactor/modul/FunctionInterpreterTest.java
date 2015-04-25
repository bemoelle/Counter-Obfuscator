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
import edu.hm.counterobfuscator.parser.IParser;
import edu.hm.counterobfuscator.parser.ParserFactory;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.FunctionRefactor;
import edu.hm.counterobfuscator.refactor.IRefactor;
import edu.hm.counterobfuscator.refactor.modul.FunctionInterpreter;
import edu.hm.counterobfuscator.refactor.modul.FunctionRenamer;
import edu.hm.counterobfuscator.refactor.modul.IModul;

public class FunctionInterpreterTest {

	IClient client; 
	
	@Before
	public void before() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		client = new HTMLUnitClient("http://www.google.de", BrowserVersion.FIREFOX_24);
	}
	
	@Test
	public void interpreterFunctionTest() throws IllegalArgumentException, IOException, EncoderException, ScriptException {
		
		String input = "(function(a,b){return a+b;}(1,2))";
		
		IParser parser = ParserFactory.create(input, false);
				
		IModul funInterpreterModul = new FunctionInterpreter(parser.getProgrammTree(), client);
		IProgrammTree tree = funInterpreterModul.process(); 
				
		assertEquals(DEFINITION.DEFAULT, tree.get(0).getDefinition().getDefinition());
		assertEquals("3", tree.get(0).getDefinition().getName());
		
	}
}
