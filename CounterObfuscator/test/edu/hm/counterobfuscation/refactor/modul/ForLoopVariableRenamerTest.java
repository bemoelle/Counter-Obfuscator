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
import edu.hm.counterobfuscator.definitions.ForWhile;
import edu.hm.counterobfuscator.definitions.Function;
import edu.hm.counterobfuscator.parser.IParser;
import edu.hm.counterobfuscator.parser.ParserFactory;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.FunctionRefactor;
import edu.hm.counterobfuscator.refactor.IRefactor;
import edu.hm.counterobfuscator.refactor.modul.ForLoopVariableRenamer;
import edu.hm.counterobfuscator.refactor.modul.FunctionRenamer;
import edu.hm.counterobfuscator.refactor.modul.FunctionVariableRenamer;
import edu.hm.counterobfuscator.refactor.modul.IModul;

public class ForLoopVariableRenamerTest {

	IClient client; 
	
	@Before
	public void before() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		client = new HTMLUnitClient("http://www.google.de", BrowserVersion.FIREFOX_24);
	}
	
	@Test
	public void renameForLoopVar0Test() throws IllegalArgumentException, IOException, EncoderException, ScriptException {
		
		String input = "for(var xxx=0; xxx<0; xxx++){var test=0;}";
		
		IParser parser = ParserFactory.create(input, false);
				
		IModul loopVariableRenamer = new ForLoopVariableRenamer(parser.getProgrammTree());
		IProgrammTree tree = loopVariableRenamer.process(); 
		
		ForWhile loop = (ForWhile)tree.get(0).getDefinition();
				
		assertEquals("forVar1", loop.getHead().getName());	
	}

}
