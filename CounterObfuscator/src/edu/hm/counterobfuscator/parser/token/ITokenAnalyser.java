package edu.hm.counterobfuscator.parser.token;

import java.util.List;

import org.apache.commons.codec.EncoderException;

import edu.hm.counterobfuscator.types.AbstractType;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 31.12.2014
 * 
 * 
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
