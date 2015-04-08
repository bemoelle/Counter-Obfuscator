package edu.hm.counterobfuscator.parser.token;

import java.util.List;

import org.apache.commons.codec.EncoderException;

import edu.hm.counterobfuscator.definitions.AbstractType;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 31.12.2014
 * 
 * this class is to analyse all tokens with a extracted form input
 */
public interface ITokenAnalyser {

	/**
	 * @throws EncoderException
	 * @throws IllegalArgumentException
	 * 
	 *             analyse tokens which are collected by the tokenizer and
	 *             transform them to javascript types
	 * 
	 */
	public void process() throws IllegalArgumentException, EncoderException;

	/**
	 * @return all types
	 */
	public List<AbstractType> getAllTypes();
}
