package edu.hm.counterobfuscator;

import java.io.IOException;

import javax.script.ScriptException;

import org.apache.commons.codec.EncoderException;

import edu.hm.counterobfuscator.helper.Setting;
import edu.hm.counterobfuscator.helper.Validate;
import edu.hm.counterobfuscator.parser.IParser;
import edu.hm.counterobfuscator.parser.ParserFactory;
import edu.hm.counterobfuscator.parser.tree.IProgrammTree;
import edu.hm.counterobfuscator.refactor.RefactorFactory;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 08.04.2015
 * 
 * 
 */
public class DeObfuscatorFactory {

	private Boolean	isFile;
	private String		url;
	private String		obfuscatedString;

	public DeObfuscatorFactory(String obfuscatedString, Boolean isFile, String url) {

		this.obfuscatedString = obfuscatedString;
		this.isFile = isFile;
		this.url = url;
	}

	public IProgrammTree create() throws IllegalArgumentException, IOException, EncoderException,
			ScriptException {

		Validate.notEmpty(obfuscatedString);
		Validate.notNull(isFile);
		Validate.notEmpty(url);	

		IParser jsParser = ParserFactory.create(obfuscatedString, isFile);

		return RefactorFactory.create(jsParser, url);
	}

}
