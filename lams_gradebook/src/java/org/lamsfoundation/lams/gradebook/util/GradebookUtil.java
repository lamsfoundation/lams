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

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import jxl.CellView;
import jxl.JXLException;
import jxl.Workbook;
import jxl.format.Font;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.gradebook.dto.ExcelCell;
import org.lamsfoundation.lams.gradebook.dto.GBActivityGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GradebookGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.comparators.GBAverageMarkComparator;
import org.lamsfoundation.lams.gradebook.dto.comparators.GBAverageTimeTakenComparator;
import org.lamsfoundation.lams.gradebook.dto.comparators.GBIDComparator;
import org.lamsfoundation.lams.gradebook.dto.comparators.GBMarkComparator;
import org.lamsfoundation.lams.gradebook.dto.comparators.GBRowNameComparator;
import org.lamsfoundation.lams.gradebook.dto.comparators.GBStartDateComparator;
import org.lamsfoundation.lams.gradebook.dto.comparators.GBTimeTakenComparator;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GradebookUtil {

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
    public static String toGridXML(List gridRows, GBGridView view, String sortBy, boolean isSearch, String searchField,
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
    public static String toGridXML(List gridRows, int page, int totalPages, GBGridView view) {
	String xml = "";
	try {
	    Document document = getDocument();

	    // root element
	    Element rootElement = document.createElement(GradebookConstants.ELEMENT_ROWS);

	    Element pageElement = document.createElement(GradebookConstants.ELEMENT_PAGE);
	    pageElement.appendChild(document.createTextNode("" + page));
	    rootElement.appendChild(pageElement);

	    Element totalPageElement = document.createElement(GradebookConstants.ELEMENT_TOTAL);
	    totalPageElement.appendChild(document.createTextNode("" + totalPages));
	    rootElement.appendChild(totalPageElement);

	    Element recordsElement = document.createElement(GradebookConstants.ELEMENT_RECORDS);
	    recordsElement.appendChild(document.createTextNode("" + gridRows.size()));
	    rootElement.appendChild(recordsElement);

	    Iterator iter = gridRows.iterator();
	    while (iter.hasNext()) {
		GradebookGridRowDTO gridRow = (GradebookGridRowDTO) iter.next();
		Element rowElement = document.createElement(GradebookConstants.ELEMENT_ROW);
		rowElement.setAttribute(GradebookConstants.ELEMENT_ID, gridRow.getId().toString());

		// Work out which grid we want to put the data into
		ArrayList<String> gridRowStringArray = new ArrayList<String>();

		gridRowStringArray = gridRow.toStringArray(view);

		for (String gradebookItem : gridRowStringArray) {
		    Element cellElement = document.createElement(GradebookConstants.ELEMENT_CELL);
		    gradebookItem = (gradebookItem != null) ? gradebookItem : "";
		    cellElement.appendChild(document.createTextNode(gradebookItem));
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
	    if (sortBy.equals(GradebookConstants.PARAM_ROW_NAME)) {
		Collections.sort(gridRows, new GBRowNameComparator());
	    } else if (sortBy.equals(GradebookConstants.PARAM_MARK)) {
		Collections.sort(gridRows, new GBMarkComparator());
	    } else if (sortBy.equals(GradebookConstants.PARAM_ID)) {
		Collections.sort(gridRows, new GBIDComparator());
	    } else if (sortBy.equals(GradebookConstants.PARAM_TIME_TAKEN)) {
		Collections.sort(gridRows, new GBTimeTakenComparator());
	    } else if (sortBy.equals(GradebookConstants.PARAM_AVG_TIME_TAKEN)) {
		Collections.sort(gridRows, new GBAverageTimeTakenComparator());
	    } else if (sortBy.equals(GradebookConstants.PARAM_AVG_MARK)) {
		Collections.sort(gridRows, new GBAverageMarkComparator());
	    } else if (sortBy.equals(GradebookConstants.PARAM_START_DATE)) {
		Collections.sort(gridRows, new GBStartDateComparator());
	    } else {
		Collections.sort(gridRows, new GBRowNameComparator());
	    }
	} else {
	    Collections.sort(gridRows, new GBRowNameComparator());
	}

	// if it is a search fix up the set
	if (isSearch && searchField != null && searchOper != null && searchString != null) {
	    gridRows = (List<GradebookGridRowDTO>) doRowNameSearch(gridRows, searchField, searchOper, searchString
		    .toLowerCase());
	}

	// Reverse the order if requested
	if (sortOrder != null && sortOrder.equals(GradebookConstants.SORT_DESC)) {
	    Collections.reverse(gridRows);
	}

	return gridRows;

    }

    /**
     * Does the search operation on the set for row names
     * 
     * @param gradebookRows
     * @param searchField
     * @param searchOper
     * @param searchString
     * @return
     */
    @SuppressWarnings("unchecked")
    private static List doRowNameSearch(List gradebookRows, String searchField, String searchOper, String searchString) {
	List<GradebookGridRowDTO> ret = new ArrayList<GradebookGridRowDTO>();

	if (searchField.equals(GradebookConstants.PARAM_ROW_NAME)) {
	    Iterator it = gradebookRows.iterator();

	    while (it.hasNext()) {
		GradebookGridRowDTO userRow = (GradebookGridRowDTO) it.next();

		String rowName = userRow.getRowName();
		rowName = rowName.toLowerCase();

		if (searchOper.equals(GradebookConstants.SEARCH_EQUALS)) {
		    if (rowName.equals(searchString)) {
			ret.add(userRow);
		    }
		} else if (searchOper.equals(GradebookConstants.SEARCH_NOT_EQUALS)) {
		    if (!rowName.equals(searchString)) {
			ret.add(userRow);
		    }
		} else if (searchOper.equals(GradebookConstants.SEARCH_BEGINS_WITH)) {
		    if (rowName.startsWith(searchString)) {
			ret.add(userRow);
		    }
		} else if (searchOper.equals(GradebookConstants.SEARCH_ENDS_WITH)) {
		    if (rowName.endsWith(searchString)) {
			ret.add(userRow);
		    }
		} else if (searchOper.equals(GradebookConstants.SEARCH_CONTAINS)) {
		    if (rowName.contains(searchString)) {
			ret.add(userRow);
		    }
		}
	    }

	}
	return ret;
    }

    public static GBGridView readGBGridViewParam(HttpServletRequest request, String param_mode, boolean optional) {
	String view = WebUtil.readStrParam(request, param_mode, optional);
	if (view == null) {
	    return null;
	} else if (view.equals(GradebookConstants.VIEW_MON_USER))
	    return GBGridView.MON_USER;
	else if (view.equals(GradebookConstants.VIEW_MON_ACTIVITY))
	    return GBGridView.MON_ACTIVITY;
	else if (view.equals(GradebookConstants.VIEW_MON_COURSE))
	    return GBGridView.MON_COURSE;
	else if (view.equals(GradebookConstants.VIEW_LRN_COURSE))
	    return GBGridView.LRN_COURSE;
	else if (view.equals(GradebookConstants.VIEW_LRN_ACTIVITY))
	    return GBGridView.LRN_ACTIVITY;
	else
	    throw new IllegalArgumentException("[" + view + "] is not a legal gradebook view");
    }

    public static void exportGradebookLessonToExcel(OutputStream out, String dateHeader,
	    LinkedHashMap<String, ExcelCell[][]> dataToExport) throws IOException, JXLException {
	WritableWorkbook workbook = Workbook.createWorkbook(out);

	int i = 0;
	for (String sheetName : dataToExport.keySet()) {
	    if (dataToExport.get(sheetName) != null) {
		createSheet(workbook, sheetName, sheetName, i, dateHeader, dataToExport.get(sheetName));
		i++;
	    }
	}

	workbook.write();
	workbook.close();
    }

    public static void createSheet(WritableWorkbook workbook, String sheetName, String sheetTitle, int sheetIndex,
	    String dateHeader, ExcelCell[][] data) throws IOException, JXLException {
	WritableSheet sheet = workbook.createSheet(sheetName, sheetIndex);

	// Prepare cell formatter used in all columns
	CellView stretchedCellView = new CellView();
	stretchedCellView.setAutosize(true);

	// Print title in bold, if needed
	if (!StringUtils.isBlank(sheetTitle)) {
	    sheet.addCell(createWritableCell(new ExcelCell(sheetTitle, true), 0, 0));
	}

	// Print current date, if needed
	if (!StringUtils.isBlank(dateHeader)) {
	    sheet.addCell(new Label(0, 1, dateHeader));
	    SimpleDateFormat titleDateFormat = new SimpleDateFormat(FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT);
	    sheet.addCell(new Label(1, 1, titleDateFormat.format(new Date())));
	}

	if (data != null) {
	    // Print data
	    for (int rowIndex = 0; rowIndex < data.length; rowIndex++) {
		int sheetRowIndex = rowIndex + 4;
		for (int columnIndex = 0; columnIndex < data[rowIndex].length; columnIndex++) {
		    ExcelCell excelCell = data[rowIndex][columnIndex];
		    if (excelCell != null) {
			sheet.addCell(createWritableCell(excelCell, columnIndex, sheetRowIndex));
		    }
		}
	    }
	}
    }

    public static WritableCell createWritableCell(ExcelCell cell, int col, int row) throws WriteException {

	if (cell != null) {
	    WritableCell writeableCell = null;
	    if (cell.getCellValue() != null && cell.getCellValue() instanceof Date) {
		SimpleDateFormat cellDateFormat = new SimpleDateFormat(FileUtil.EXPORT_TO_SPREADSHEET_CELL_DATE_FORMAT);
		writeableCell = new Label(col, row, cellDateFormat.format(cell.getCellValue()));
	    } else if (cell.getCellValue() != null && cell.getCellValue() instanceof java.lang.Double) {
		writeableCell = new Number(col, row, (Double) cell.getCellValue());
	    } else if (cell.getCellValue() != null && cell.getCellValue() instanceof java.lang.Long) {
		writeableCell = new Number(col, row, ((Long) cell.getCellValue()).doubleValue());
	    } else if (cell.getCellValue() != null) {
		writeableCell = new Label(col, row, cell.getCellValue().toString());
	    } else {
		writeableCell = new Label(col, row, null);
	    }

	    if (cell.getIsBold() == Boolean.TRUE) {
		Font font = writeableCell.getCellFormat().getFont();
		WritableFont labelFont = new WritableFont(font);
		labelFont.setBoldStyle(WritableFont.BOLD);
		WritableCellFormat labelCellFormat = new WritableCellFormat(labelFont);
		writeableCell.setCellFormat(labelCellFormat);
	    }
	    return writeableCell;
	} else {
	    return null;
	}
    }

}
