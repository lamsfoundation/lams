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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.daco.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.lamsfoundation.lams.util.excel.ExcelCell;
import org.lamsfoundation.lams.util.excel.ExcelRow;
import org.lamsfoundation.lams.util.excel.ExcelSheet;
import org.lamsfoundation.lams.util.excel.ExcelUtil;

public class DacoExcelUtil {

    /**
     * Exports data in MS Excel (.xls) format.
     *
     * @param out
     *            output stream to which the file written; usually taken from HTTP response; it is not closed afterwards
     * @param sheetName
     *            name of first sheet in Excel workbook; data will be stored in this sheet
     * @param title
     *            title printed in the first (0,0) cell
     * @param dateHeader
     *            text describing current date; if <code>NULL</code> then no date is printed; if not <code>NULL</code>
     *            then text is written out along with current date in the cell; the date is formatted according to
     *            {@link #EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT}
     * @param columnNames
     *            name of the columns that describe <code>data</code> parameter
     * @param inputData
     *            array of data to print out; first index of array describes a row, second a column; dates are formatted
     *            according to {@link #EXPORT_TO_SPREADSHEET_CELL_DATE_FORMAT}
     * @throws IOException
     * @throws JXLException
     */
    public static void exportToExcel(OutputStream out, String sheetName, String title, String dateHeader,
	    String[] columnNames, Object[][] inputData) throws IOException {
	List<ExcelSheet> sheets = new LinkedList<ExcelSheet>();
	ExcelSheet sheet = new ExcelSheet(title);
	sheets.add(sheet);

	ExcelRow summaryTitleRow = new ExcelRow();
	for (String columnName : columnNames) {
	    summaryTitleRow.addCell(columnName, true);
	}
	sheet.addRow(summaryTitleRow);

	if (inputData != null) {
	    // Print data
	    for (int rowIndex = 0; rowIndex < inputData.length; rowIndex++) {
		ExcelRow row = new ExcelRow();
		for (int columnIndex = 0; columnIndex < inputData[rowIndex].length; columnIndex++) {
		    Object content = inputData[rowIndex][columnIndex];
		    if (content != null) {
			row.addCell(content, false);
		    }
		}
		sheet.addRow(row);
	    }
	}

	ExcelUtil.createExcel(out, sheets, dateHeader, true);
    }
}
