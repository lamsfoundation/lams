/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.gradebook.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.lamsfoundation.lams.gradebook.dto.GradeBookGridRowDTO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GradeBookUtil {

    public static final int GRID_TYPE_MONITOR_USER_VIEW = 0;
    public static final int GRID_TYPE_MONITOR_ACTIVITY_VIEW = 1;

    @SuppressWarnings("unchecked")
    public static String toGridXML(Collection gridRows, int page, int totalPages, int gridType) {
	String xml = "";
	try {
	    Document document = getDocument();

	    // root element
	    Element rootElement = document.createElement("rows");

	    Element pageElement = document.createElement("page");
	    pageElement.appendChild(document.createTextNode("" + page));
	    rootElement.appendChild(pageElement);

	    Element totalPageElement = document.createElement("total");
	    totalPageElement.appendChild(document.createTextNode("" + totalPages));
	    rootElement.appendChild(totalPageElement);

	    Element recordsElement = document.createElement("records");
	    recordsElement.appendChild(document.createTextNode("" + gridRows.size()));
	    rootElement.appendChild(recordsElement);

	    Iterator iter = gridRows.iterator();
	    while (iter.hasNext()) {
		GradeBookGridRowDTO gridRow = (GradeBookGridRowDTO) iter.next();
		Element rowElement = document.createElement("row");
		rowElement.setAttribute("id", gridRow.getRowId());

		// Work out which grid we want to put the data into
		ArrayList<String> gridRowStringArray = new ArrayList<String>();
		switch (gridType) {
    		case GRID_TYPE_MONITOR_USER_VIEW:
    		    gridRowStringArray = gridRow.toMonitorUserViewStringArray();
    		    break;
    		case GRID_TYPE_MONITOR_ACTIVITY_VIEW:
    		    gridRowStringArray = gridRow.toMonitorActViewStringArray();
    		    break;

		}

		for (String gradeBookItem : gridRowStringArray) {
		    Element cellElement = document.createElement("cell");
		    gradeBookItem = (gradeBookItem != null) ? gradeBookItem : "";
		    cellElement.appendChild(document.createTextNode(gradeBookItem));
		    rowElement.appendChild(cellElement);
		}
		rootElement.appendChild(rowElement);
	    }

	    document.appendChild(rootElement);
	    xml = getStringFromDocument(document);

	} catch (ParserConfigurationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (TransformerException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return xml;
    }

    private static Document getDocument() throws ParserConfigurationException {
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = factory.newDocumentBuilder();
	Document document = builder.newDocument();
	return document;

    }

    private static String getStringFromDocument(Document document) throws TransformerException {
	DOMSource domSource = new DOMSource(document);
	StringWriter writer = new StringWriter();
	StreamResult result = new StreamResult(writer);
	TransformerFactory tf = TransformerFactory.newInstance();
	Transformer transformer = tf.newTransformer();
	transformer.transform(domSource, result);
	return writer.toString();
    }

}
