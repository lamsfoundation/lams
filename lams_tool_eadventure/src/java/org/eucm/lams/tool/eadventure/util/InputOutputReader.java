package org.eucm.lams.tool.eadventure.util;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class InputOutputReader {

    //IOdatamodel ioParameterList;

    public static IOdatamodel parseParameters(String url) {
	SaxIOReader io = null;
	try {

	    io = new SaxIOReader();
	    SAXParserFactory spf = SAXParserFactory.newInstance();
	    SAXParser sp = spf.newSAXParser();
	    sp.parse(url, io);

	} catch (ParserConfigurationException e) {
	    System.err.println("Parameters parse erros");
	} catch (SAXException e2) {
	    System.err.println("sax error: " + e2.getStackTrace());
	} catch (IOException e3) {
	    // TODO Auto-generated catch block
	    System.err.println("io error: " + e3.getMessage());
	}

	return io.getIOdatamodel();

    }

    public static HashMap<String, String> getOutputParameterList(String url) {
	return InputOutputReader.parseParameters(url).getOutput();
    }

}
