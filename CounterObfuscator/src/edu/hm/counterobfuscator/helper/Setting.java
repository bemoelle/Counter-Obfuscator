/**
 * 
 */
package edu.hm.counterobfuscator.helper;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 13.01.2015
 * 
 * 
 */
public class Setting {

	private Properties properties;

	public Setting() throws IOException {

		properties = new Properties();
		BufferedInputStream stream = new BufferedInputStream(
				new FileInputStream("settings.properties"));

		properties.load(stream);

		stream.close();

	}

	public boolean isConfigured(String string) {

		return properties.getProperty("VariableRenamer").indexOf("true") > -1;
	}

}
