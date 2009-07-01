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
package org.lamsfoundation.lams.tool.assessment.util; 

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

import jxl.CellView;
import jxl.JXLException;
import jxl.Workbook;
import jxl.format.Font;
import jxl.write.Boolean;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.tool.assessment.dto.ExcelCell;
import org.lamsfoundation.lams.util.FileUtil;
 

/**
 * This is a helper class for the excel export or assessment data
 * 
 * @author lfoxton
 *
 */
public class AssessmentExportXLSUtil {

    /**
     * Exports a list of worksheets to a file
     * 
     * @param out
     * @param dateHeader
     * @param dataToExport
     * @throws IOException
     * @throws JXLException
     */
    public static void exportAssessmentToExcel(OutputStream out, String dateHeader,
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

    /**
     * Creates a worksheet to export 
     * 
     * @param workbook
     * @param sheetName
     * @param sheetTitle
     * @param sheetIndex
     * @param dateHeader
     * @param data
     * @throws IOException
     * @throws JXLException
     */
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

    /** 
     * Creates a WritableCell from an ExcelCell object
     * @param cell
     * @param col
     * @param row
     * @return
     * @throws WriteException
     */
    public static WritableCell createWritableCell(ExcelCell cell, int col, int row) throws WriteException {

	if (cell != null) {    
	    WritableCell writeableCell = null;
	    if (cell.getCellValue() != null && (cell.getCellValue() instanceof Date || cell.getCellValue() instanceof Timestamp)) {
		SimpleDateFormat cellDateFormat = new SimpleDateFormat(FileUtil.EXPORT_TO_SPREADSHEET_CELL_DATE_FORMAT);
		writeableCell = new Label(col, row, cellDateFormat.format(cell.getCellValue()));
	    } else if (cell.getCellValue() != null && cell.getCellValue() instanceof java.lang.Double) {
		writeableCell = new Number(col, row, (Double) cell.getCellValue());
	    } else if (cell.getCellValue() != null && cell.getCellValue() instanceof java.lang.Long) {
		writeableCell = new Number(col, row, ((Long) cell.getCellValue()).doubleValue());
	    }else if (cell.getCellValue() != null && cell.getCellValue() instanceof java.lang.Float) {
		writeableCell = new Number(col, row, ((Float) cell.getCellValue()).doubleValue());
	    } else if (cell.getCellValue() != null && cell.getCellValue() instanceof java.lang.Boolean) {
		writeableCell = new Boolean(col, row, ((java.lang.Boolean) cell.getCellValue()));
	    } else if (cell.getCellValue() != null) {
		writeableCell = new Label(col, row, cell.getCellValue().toString());
	    } else {
		writeableCell = new Label(col, row, null);
	    }

	    if (cell.isBold()) {
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
 