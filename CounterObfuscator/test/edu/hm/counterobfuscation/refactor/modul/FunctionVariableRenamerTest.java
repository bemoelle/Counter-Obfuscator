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
import edu.hm.counterobfuscator.definitions.Function;
import edu.hm.counterobfuscator.parser.IParser;
import edu.hm.counterobfuscator.parser.ParserFactory;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.FunctionRefactor;
import edu.hm.counterobfuscator.refactor.IRefactor;
import edu.hm.counterobfuscator.refactor.modul.FunctionRenamer;
import edu.hm.counterobfuscator.refactor.modul.FunctionVariableRenamer;
import edu.hm.counterobfuscator.refactor.modul.IModul;

public class FunctionVariableRenamerTest {

	IClient client; 
	
	@Before
	public void before() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		client = new HTMLUnitClient("http://www.google.de", BrowserVersion.FIREFOX_24);
	}
	
	@Test
	public void renameFunctionVar0Test() throws IllegalArgumentException, IOException, EncoderException, ScriptException {
		
		String input = "function xxxxx(){var test=0;}";
		
		IParser parser = ParserFactory.create(input, false);
				
		IModul funVariableRenamer = new FunctionVariableRenamer(parser.getProgrammTree());
		IProgrammTree tree = funVariableRenamer.process(); 
		
		Function func = (Function)tree.get(0).getDefinition();
				
		assertEquals(0, func.getHead().size());
		
	}
	
	@Test
	public void renameFunctionVar1Test() throws IllegalArgumentException, IOException, EncoderException, ScriptException {
		
		String input = "function xxxxx(xxxx){var test=0;}";
		
		IParser parser = ParserFactory.create(input, false);
				
		IModul funVariableRenamer = new FunctionVariableRenamer(parser.getProgrammTree());
		IProgrammTree tree = funVariableRenamer.process(); 
		
		Function func = (Function)tree.get(0).getDefinition();
				
		assertEquals("functionVar1", func.getHead().get(0).getName());
		
	}
	
	@Test
	public void renameFunctionVar2Test() throws IllegalArgumentException, IOException, EncoderException, ScriptException {
		
		String input = "function xxxxx(xxxx,yyyy){var test=0;}";
		
		IParser parser = ParserFactory.create(input, false);
				
		IModul funVariableRenamer = new FunctionVariableRenamer(parser.getProgrammTree());
		IProgrammTree tree = funVariableRenamer.process(); 
		
		Function func = (Function)tree.get(0).getDefinition();
				
		assertEquals("functionVar1", func.getHead().get(0).getName());
		assertEquals("functionVar2", func.getHead().get(1).getName());
		
	}

}
