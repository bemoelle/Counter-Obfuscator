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

public class VariableRemoverTest {

	IClient client; 
	
	@Before
	public void before() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		client = new HTMLUnitClient("http://www.google.de", BrowserVersion.FIREFOX_24);
	}
	
	@Test
	public void removerVariableTest1() throws IllegalArgumentException, IOException, EncoderException, ScriptException {
		
		String input = "var test = 1;";
		
		IParser parser = ParserFactory.create(input, false);
				
		IModul varRemoverModul = new VariableRemover(parser.getProgrammTree());
		IProgrammTree tree = varRemoverModul.process(); 
				
		assertEquals(1, tree.size());

	}
	
	@Test
	public void removerVariableTest2() throws IllegalArgumentException, IOException, EncoderException, ScriptException {
		
		String input = "var test = 1; var test2=0;";
		
		IParser parser = ParserFactory.create(input, false);
				
		IModul varRemoverModul = new VariableRemover(parser.getProgrammTree());
		IProgrammTree tree = varRemoverModul.process(); 
				
		assertEquals(0, tree.size());

	}
	
	@Test
	public void removerVariableTest3() throws IllegalArgumentException, IOException, EncoderException, ScriptException {
		
		String input = "var test = 1; var test2=0; return test;";
		
		IParser parser = ParserFactory.create(input, false);
				
		IModul varRemoverModul = new VariableRemover(parser.getProgrammTree());
		IProgrammTree tree = varRemoverModul.process(); 
				
		assertEquals(2, tree.size());
	}
}
