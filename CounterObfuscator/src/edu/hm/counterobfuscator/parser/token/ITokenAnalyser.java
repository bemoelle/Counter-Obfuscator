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
	 */
	public void process() throws IllegalArgumentException, EncoderException;

	/**
	 * @return
	 */
	public List<AbstractType> getAllTypes();
}
