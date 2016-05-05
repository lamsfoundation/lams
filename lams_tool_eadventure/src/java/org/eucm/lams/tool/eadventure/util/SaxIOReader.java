package org.eucm.lams.tool.eadventure.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxIOReader extends DefaultHandler {
    String content = "";
    private IOdatamodel ioParameterList = new IOdatamodel();

    /*
     * Esta funcion el llamada cuando se produce el evento de ver una nueva etiqueta
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
	if ("io-parameter-list".equals(qName)) {
	    ioParameterList = new IOdatamodel();
	}
	if ("i-parameter".equals(qName)) {
	    String name = null;
	    String type = null;
	    for (int i = 0; i < attributes.getLength(); i++) {
		if (attributes.getQName(i).equals("name")) {
		    name = attributes.getValue(i);
		}
		if (attributes.getQName(i).equals("type")) {
		    type = attributes.getValue(i);
		}

	    }
	    ioParameterList.addInput(name, type);

	}

	if ("o-parameter".equals(qName)) {
	    String name = null;
	    String type = null;
	    for (int i = 0; i < attributes.getLength(); i++) {
		if (attributes.getQName(i).equals("name")) {
		    name = attributes.getValue(i);
		}
		if (attributes.getQName(i).equals("type")) {
		    type = attributes.getValue(i);
		}

	    }
	    ioParameterList.addOutput(name, type);

	}

    }

    /*
     * Esta funcion es llamada cuando ve el contenido de una etiqueta
     */
    @Override
    public void characters(char buf[], int offset, int len) throws SAXException {
	content = new String(buf, offset, len);

    }

    /*
     * y esta al llegar al final
     */
    @Override
    public void endElement(String uri, String localName, String qName) {

    }

    public IOdatamodel getIOdatamodel() {
	return ioParameterList;
    }
}
