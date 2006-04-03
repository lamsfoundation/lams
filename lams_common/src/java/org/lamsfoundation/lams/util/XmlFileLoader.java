/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * <p>
 * <a href="XmlFileLoader.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */

public class XmlFileLoader {

	private static boolean validating = true;

	/**
	 * @return Returns the validating.
	 */
	public static boolean isValidating() {
		return validating;
	}
	/**
	 * @param validating The validating to set.
	 */
	public static void setValidating(boolean validating) {
		XmlFileLoader.validating = validating;
	}

	/** Get the xml file from the URL and parse it into a Document object.
	 *
	 * @param url the URL from which the xml doc is to be obtained.
	 * @return Document
	 */
	public static Document getDocumentFromURL(URL url) 
		throws IOException,SAXException,SAXParseException,ParserConfigurationException {
		
		InputStream is = null;
		is = url.openStream();
		return getDocument(is);
	}

	/** Get the xml file from the File Path and parse it into a Document object.
	 *
	 * @param filePath the file path from which the xml doc is to be obtained.
	 * @return Document
	 */
	public static Document getDocumentFromFilePath(String filePath) 
		throws IOException,SAXException,SAXParseException,ParserConfigurationException {
		
		InputStream is = null;
		is = new FileInputStream(new File(filePath));
		return getDocument(is);
	}
	
	/** Parses the xml document in is to create a DOM Document. DTD validation
	 * is enabled if validating is true
	 *
	 * @param is the InputStream containing the xml descriptor to parse
	 * @return Document
	 */
	private static Document getDocument(InputStream is)
		throws IOException,SAXException,SAXParseException,ParserConfigurationException {
		
		try{
			InputSource is2 = new InputSource(is);
			return getDocument(is2);
		}
		finally{
			is.close();
		}
	}

	/** Parses the xml document in is to create a DOM Document. DTD validation
	 * is enabled if validating is true.
	 *
	 * @param is the InputSource containing the xml descriptor to parse
	 * @return Document
	 */
	private static Document getDocument(InputSource is)
			throws IOException,SAXException,SAXParseException,ParserConfigurationException {
			
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			// Enable DTD validation based on our validating flag
			docBuilderFactory.setValidating(validating);
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			return docBuilder.parse(is);
	}

}

