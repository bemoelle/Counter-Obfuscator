package edu.hm.webscraper;

import java.io.File;
import java.io.IOException;
import com.jaunt.*;
import com.jaunt.util.HandlerForBinary;
import com.jaunt.util.HandlerForText;

//Jaunt demo: searches for 'butterflies' at Google and prints urls of search results from first page.

public class JauntWebScraperDemo {
	public static void main(String[] args) throws JauntException, IOException {
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
			  String url ="/home/benni/test2.html"; 
			  File file = new File(url);
			  userAgent.open(file);
			  //userAgent.visit(url);
			  System.out.println(userAgent.doc.innerHTML());  
//			  System.out.println(handlerForText.getContent());
//			     
//			  //retrieve JS content as String
//			  userAgent.visit("http://jaunt-api.com/syntaxhighlighter/scripts/shCore.js");
//			  System.out.println(handlerForText.getContent());
//			     
//			  //retrieve GIF content as byte[], and print its length
//			  userAgent.visit("http://jaunt-api.com/background.gif");
//			  System.out.println(handlerForBinary.getContent().length);  
			}
			catch(JauntException e){
			  System.err.println(e);
			}
	}
}
