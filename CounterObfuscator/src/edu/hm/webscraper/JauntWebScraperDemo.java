package edu.hm.webscraper;

import java.io.File;
import java.io.IOException;

import javax.script.ScriptException;

import org.apache.commons.codec.EncoderException;

import com.jaunt.*;
import com.jaunt.util.HandlerForBinary;
import com.jaunt.util.HandlerForText;

import edu.hm.counterobfuscator.DeObfuscatorFactory;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;

//Jaunt demo: searches for 'butterflies' at Google and prints urls of search results from first page.

public class JauntWebScraperDemo {
	public static void main(String[] args) throws JauntException, IOException, IllegalArgumentException, EncoderException, ScriptException {
		try{
			  //create UserAgent and content handlers.
			  UserAgent userAgent = new UserAgent();   
			  HandlerForText handlerForText = new HandlerForText();
			  HandlerForBinary handlerForBinary = new HandlerForBinary();
			 
			  //register each handler with a specific content-type
			  userAgent.setHandler("text/css", handlerForText);
			  userAgent.setHandler("text/javascript", handlerForText);
			  userAgent.setHandler("application/x-javascript", handlerForText);
			  userAgent.setHandler("image/gif", handlerForBinary);
			  userAgent.setHandler("image/jpeg", handlerForBinary);
			 
			  //retrieve CSS content as String
			  String url ="http://heidelberg.craigslist.de/"; 
//			  String url = "https://familysearch.org/search/record/results?count=20&query=%2Bgivenname%3AJacob~%20%2Bsurname%3AHof~";
			  File file = new File(url);
			 // userAgent.open(file);
			  
			  userAgent.visit(url);
			  
			  String scrapedContent = userAgent.doc.innerHTML();
			  System.out.println(scrapedContent);
			  
			  DeObfuscatorFactory deObfuscatorFactory = new DeObfuscatorFactory(scrapedContent, false,
						url, null);

				IProgrammTree resultTree = deObfuscatorFactory.create();

				resultTree.printOnConsole();

			}
			catch(JauntException e){
			  System.err.println(e);
			}
	}
}
