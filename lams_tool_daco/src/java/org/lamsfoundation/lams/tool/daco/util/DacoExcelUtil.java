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

/* $Id$ */
package org.lamsfoundation.lams.tool.daco.util;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import jxl.CellView;
import jxl.JXLException;
import jxl.Workbook;
import jxl.format.Font;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.util.FileUtil;

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
     * @param data
     *            array of data to print out; first index of array describes a row, second a column; dates are formatted
     *            according to {@link #EXPORT_TO_SPREADSHEET_CELL_DATE_FORMAT}
     * @throws IOException
     * @throws JXLException
     */
    public static void exportToolToExcel(OutputStream out, String sheetName, String title, String dateHeader,
	    String[] columnNames, Object[][] data) throws IOException, JXLException {
	WritableWorkbook workbook = Workbook.createWorkbook(out);
	WritableSheet sheet = workbook.createSheet(sheetName, 0);
	// Prepare cell formatter used in all columns
	CellView stretchedCellView = new CellView();
	stretchedCellView.setAutosize(true);
	// Pring title in bold, if needed
	if (!StringUtils.isBlank(title)) {
	    Label titleCell = new Label(0, 0, title);
	    Font font = titleCell.getCellFormat().getFont();
	    WritableFont titleFont = new WritableFont(font);
	    titleFont.setBoldStyle(WritableFont.BOLD);
	    WritableCellFormat titleCellFormat = new WritableCellFormat(titleFont);
	    titleCell.setCellFormat(titleCellFormat);
	    sheet.addCell(titleCell);
	}
	// Print current date, if needed
	if (!StringUtils.isBlank(dateHeader)) {
	    sheet.addCell(new Label(0, 1, dateHeader));
	    SimpleDateFormat titleDateFormat = new SimpleDateFormat(FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT);
	    sheet.addCell(new Label(1, 1, titleDateFormat.format(new Date())));
	}
	// Print column names, if needed
	if (columnNames != null) {
	    for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
		sheet.addCell(new Label(columnIndex, 3, columnNames[columnIndex]));
		sheet.setColumnView(columnIndex, stretchedCellView);
	    }
	}
	SimpleDateFormat cellDateFormat = new SimpleDateFormat(FileUtil.EXPORT_TO_SPREADSHEET_CELL_DATE_FORMAT);
	if (data != null) {
	    // Print data
	    for (int rowIndex = 0; rowIndex < data.length; rowIndex++) {
		int sheetRowIndex = rowIndex + 4;
		for (int columnIndex = 0; columnIndex < data[rowIndex].length; columnIndex++) {
		    Object content = data[rowIndex][columnIndex];
		    if (content != null) {
			WritableCell cell = null;
			if (content instanceof Date) {
			    Date date = (Date) content;
			    cell = new Label(columnIndex, sheetRowIndex, cellDateFormat.format(date));
			} else {
			    cell = new Label(columnIndex, sheetRowIndex, content.toString());
			}
			sheet.addCell(cell);
		    }
		}
	    }
	}
	workbook.write();
	workbook.close();
    }
}
