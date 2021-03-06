/*
    Copyright (c) 2012,2013 Mirco Attocchi
	
    This file is part of WebAppCommon.

    WebAppCommon is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    WebAppCommon is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with WebAppCommon.  If not, see <http://www.gnu.org/licenses/>.
*/

package it.attocchi.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * <p>PropertiesUtils class.</p>
 *
 * @author mirco
 * @version $Id: $Id
 */
public class PropertiesUtils {

	/** Constant <code>logger</code> */
	protected static final Logger logger = Logger.getLogger(PropertiesUtils.class.getName());

	/**
	 * <p>getProperties.</p>
	 *
	 * @param in
	 *            InputStrem like a getResourceAsStrem from ServletContext
	 * @return a {@link java.util.Properties} object.
	 */
	public static Properties getProperties(InputStream in) {

		Properties properties = new Properties();

		try {

			// InputStream in =
			// getServletContext().getResourceAsStream(WEB_INF_CONFIG_PORTAL_PROPERTIES);
			properties.load(in);
			// in.close();

		} catch (Exception ex) {
			logger.error("Error loading properties", ex);
			properties = null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception ex) {
					logger.error("Error closing InputStream", ex);
				}
			}
		}

		return properties;
	}

	/**
	 * <p>saveProperties.</p>
	 *
	 * @param properties a {@link java.util.Properties} object.
	 * @param filePath a {@link java.lang.String} object.
	 */
	public static void saveProperties(Properties properties, String filePath) {

		// Properties properties = new Properties();

		FileOutputStream out = null;
		try {

			File f = new File(filePath);
			out = new FileOutputStream(f);
			properties.store(out, "---No Comment---");
			out.close();

		} catch (Exception ex) {
			logger.error("Error saving properties", ex);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception ex) {
					logger.error("Error closing FileOutputStream", ex);
				}
			}
		}

	}
}
