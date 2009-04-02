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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.lamsfoundation.lams.gradebook.dto.GradeBookGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.comparators.GBIDComparator;
import org.lamsfoundation.lams.gradebook.dto.comparators.GBMarkComparator;
import org.lamsfoundation.lams.gradebook.dto.comparators.GBRowNameComparator;
import org.lamsfoundation.lams.gradebook.dto.comparators.GBTimeTakenComparator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GradeBookUtil {

    /**
     * Wrapper method for printing the xml for grid rows
     * 
     * It takes the list of rows along with the grid parameter and returns the 
     * xml for the altered set
     * 
     * 
     * @param gridRows
     * @param view
     * @param sortBy
     * @param isSearch
     * @param searchField
     * @param searchOper
     * @param searchString
     * @param sortOrder
     * @param rowLimit
     * @param page
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String toGridXML(List gridRows, String view, String sortBy, boolean isSearch, String searchField,
	    String searchOper, String searchString, String sortOrder, int rowLimit, int page) {
	
	// Alter the set based on the parameters
	gridRows = makeGridRowAlterations(gridRows, sortBy, isSearch, searchField, searchOper, searchString, sortOrder,
		rowLimit, page);
	// Work out the sublist to fetch based on rowlimit and current page.
	int totalPages = 1;
	if (rowLimit < gridRows.size()) {

	    totalPages = new Double(Math.ceil(new Integer(gridRows.size()).doubleValue()
		    / new Integer(rowLimit).doubleValue())).intValue();
	    int firstRow = (page - 1) * rowLimit;
	    int lastRow = firstRow + rowLimit;

	    if (lastRow > gridRows.size()) {
		gridRows = gridRows.subList(firstRow, gridRows.size());
	    } else {
		gridRows = gridRows.subList(firstRow, lastRow);
	    }

	}

	return toGridXML(gridRows, page, totalPages, view);
    }

    /**
     * Tranlates a list of grid rows into the required jqGrid xml
     * 
     * @param gridRows
     * @param page
     * @param totalPages
     * @param view
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String toGridXML(List gridRows, int page, int totalPages, String view) {
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
		rowElement.setAttribute("id", gridRow.getId().toString());

		// Work out which grid we want to put the data into
		ArrayList<String> gridRowStringArray = new ArrayList<String>();

		gridRowStringArray = gridRow.toStringArray(view);

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

    /**
     * Alters a grid row for sorting and searching
     * 
     * @param gridRows
     * @param sortBy
     * @param isSearch
     * @param searchField
     * @param searchOper
     * @param searchString
     * @param sortOrder
     * @param rowLimit
     * @param page
     * @return
     */
    @SuppressWarnings("unchecked")
    private static List makeGridRowAlterations(List gridRows, String sortBy, boolean isSearch, String searchField,
	    String searchOper, String searchString, String sortOrder, int rowLimit, int page) {

	// Sort the list appropriately
	if (sortBy != null) {
	    if (sortBy.equals("rowName")) {
		Collections.sort(gridRows, new GBRowNameComparator());
	    } else if (sortBy.equals("mark")) {
		Collections.sort(gridRows, new GBMarkComparator());
	    } else if (sortBy.equals("id")) {
		Collections.sort(gridRows, new GBIDComparator());
	    } else if (sortBy.equals("timeTaken")) {
		Collections.sort(gridRows, new GBTimeTakenComparator());
	    } else {
		Collections.sort(gridRows, new GBRowNameComparator());
	    }
	} else {
	    Collections.sort(gridRows, new GBRowNameComparator());
	}

	// if it is a search fix up the set
	if (isSearch && searchField != null && searchOper != null && searchString != null) {
	    gridRows = (List<GradeBookGridRowDTO>) doRowNameSearch(gridRows, searchField, searchOper, searchString
		    .toLowerCase());
	}

	// Reverse the order if requested
	if (sortOrder != null && sortOrder.equals("desc")) {
	    Collections.reverse(gridRows);
	}

	return gridRows;

    }

    /**
     * Does the search operation on the set for row names
     * 
     * @param gradeBookRows
     * @param searchField
     * @param searchOper
     * @param searchString
     * @return
     */
    @SuppressWarnings("unchecked")
    private static List doRowNameSearch(List gradeBookRows, String searchField, String searchOper, String searchString) {
	List<GradeBookGridRowDTO> ret = new ArrayList<GradeBookGridRowDTO>();

	if (searchField.equals("rowName")) {
	    Iterator it = gradeBookRows.iterator();

	    while (it.hasNext()) {
		GradeBookGridRowDTO userRow = (GradeBookGridRowDTO) it.next();

		String rowName = userRow.getRowName();
		rowName = rowName.toLowerCase();

		if (searchOper.equals("eq")) {
		    if (rowName.equals(searchString)) {
			ret.add(userRow);
		    }
		} else if (searchOper.equals("ne")) {
		    if (!rowName.equals(searchString)) {
			ret.add(userRow);
		    }
		} else if (searchOper.equals("bw")) {
		    if (rowName.startsWith(searchString)) {
			ret.add(userRow);
		    }
		} else if (searchOper.equals("ew")) {
		    if (rowName.endsWith(searchString)) {
			ret.add(userRow);
		    }
		} else if (searchOper.equals("cn")) {
		    if (rowName.contains(searchString)) {
			ret.add(userRow);
		    }
		}
	    }

	}
	return ret;
    }

}
