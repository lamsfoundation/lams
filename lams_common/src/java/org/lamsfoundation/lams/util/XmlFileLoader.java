/*
 * Created on 9/12/2004
 *
 * Last modified on 9/12/2004
 */
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
 * TODO Add description here
 *
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

