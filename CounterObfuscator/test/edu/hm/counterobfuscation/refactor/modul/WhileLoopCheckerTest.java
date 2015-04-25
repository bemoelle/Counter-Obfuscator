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
import edu.hm.counterobfuscator.parser.IParser;
import edu.hm.counterobfuscator.parser.ParserFactory;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.FunctionRefactor;
import edu.hm.counterobfuscator.refactor.IRefactor;
import edu.hm.counterobfuscator.refactor.modul.ForLoopChecker;
import edu.hm.counterobfuscator.refactor.modul.FunctionRenamer;
import edu.hm.counterobfuscator.refactor.modul.IModul;
import edu.hm.counterobfuscator.refactor.modul.WhileLoopChecker;

public class WhileLoopCheckerTest {

	IClient client; 
	
	@Before
	public void before() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		client = new HTMLUnitClient("http://www.google.de", BrowserVersion.FIREFOX_24);
	}
	
	@Test
	public void loopCheckerTest1() throws IllegalArgumentException, IOException, EncoderException, ScriptException {
		
		String input = "while(false){var test=0;}";
		
		IParser parser = ParserFactory.create(input, false);
				
		IModul loopCheckerModul = new WhileLoopChecker(parser.getProgrammTree(), client);
		IProgrammTree tree = loopCheckerModul.process(); 
				
		assertEquals(0, tree.size());	
	}
	
	@Test
	public void loopCheckerTest2() throws IllegalArgumentException, IOException, EncoderException, ScriptException {
		
		String input = "while(true){var test=0;}";
		
		IParser parser = ParserFactory.create(input, false);
				
		IModul loopCheckerModul = new WhileLoopChecker(parser.getProgrammTree(), client);
		IProgrammTree tree = loopCheckerModul.process(); 
				
		assertEquals(1, tree.size());	
	}


}
