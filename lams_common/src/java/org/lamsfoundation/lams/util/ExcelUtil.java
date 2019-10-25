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


package org.lamsfoundation.lams.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.TimeZone;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.util.LocaleUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Utilities for producing .xlsx files.
 */
public class ExcelUtil {

    private static CellStyle defaultStyle;
    private static CellStyle boldStyle;
    
    //other built in formats https://poi.apache.org/apidocs/dev/org/apache/poi/ss/usermodel/BuiltinFormats.html
    private static final String FORMAT_NUMBER = "#.##";
    private static final String FORMAT_PERCENTAGE = "0.00%";
    
    private static CellStyle numberStyle;
    private static CellStyle dateStyle;
    private static CellStyle timeStyle;
    private static CellStyle percentageStyle;

    private static CellStyle greenColor;
    private static CellStyle blueColor;
    private static CellStyle redColor;
    private static CellStyle yellowColor;

    private static CellStyle borderStyleLeftThin;
    private static CellStyle borderStyleLeftThinPercentage;
    private static CellStyle borderStyleLeftThick;
    private static CellStyle borderStyleRightThick;
    private static CellStyle borderStyleLeftThinBoldFont;
    private static CellStyle borderStyleLeftThinBoldFontPercentage;
    private static CellStyle borderStyleLeftThickBoldFont;
    private static CellStyle borderStyleRightThickBoldFont;
    private static CellStyle borderStyleBottomThin;
    private static CellStyle borderStyleBottomThinBoldFont;
    private static CellStyle borderStyleRightThickPercentage;
    private static CellStyle borderStyleRightThickBoldFontPercentage;

    public final static String DEFAULT_FONT_NAME = "Calibri-Regular";

    
    /**
     * Create .xls file out of provided data and then write out it to an OutputStream. It should be saved with the .xls extension.
     * Only use if you want to read the file back in again afterwards. 
     * 
     * Warning: The styling is untested with this option and may fail. If you want full styling look at createExcel()
     *
     * @param out
     *            output stream to which the file written; usually taken from HTTP response
     * @param dataToExport
     *            array of data to print out; first index of array describes a row, second a column
     * @param dateHeader
     *            text describing current date; if <code>NULL</code> then no date is printed; if not <code>NULL</code>
     *            then text is written out along with current date in the cell; the date is formatted according to
     *            {@link #EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT}
     * @param displaySheetTitle
     *            whether to display title (printed in the first (0,0) cell)
     * @throws IOException
     */
    public static void createExcelXLS(OutputStream out, LinkedHashMap<String, ExcelCell[][]> dataToExport,
	    String dateHeader, boolean displaySheetTitle) throws IOException {
	Workbook workbook = new HSSFWorkbook(); 
	create(workbook, out, dataToExport, dateHeader, displaySheetTitle);
    }

    /**
     * Create .xlsx file out of provided data and then write out it to an OutputStream. It should be saved with the .xlsx extension.
     *
     * @param out
     *            output stream to which the file written; usually taken from HTTP response
     * @param dataToExport
     *            array of data to print out; first index of array describes a row, second a column
     * @param dateHeader
     *            text describing current date; if <code>NULL</code> then no date is printed; if not <code>NULL</code>
     *            then text is written out along with current date in the cell; the date is formatted according to
     *            {@link #EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT}
     * @param displaySheetTitle
     *            whether to display title (printed in the first (0,0) cell)
     * @throws IOException
     */
    public static void createExcel(OutputStream out, LinkedHashMap<String, ExcelCell[][]> dataToExport,
	    String dateHeader, boolean displaySheetTitle) throws IOException {
	//set user time zone, which is required for outputting cells of time format 
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	TimeZone userTimeZone = user.getTimeZone();
	LocaleUtil.setUserTimeZone(userTimeZone);
	
	Workbook workbook = new SXSSFWorkbook(100); // keep 100 rows in memory, exceeding rows will be flushed to disk
	create(workbook, out, dataToExport, dateHeader, displaySheetTitle);
    }
    
    private static void create(Workbook workbook, OutputStream out, LinkedHashMap<String, ExcelCell[][]> dataToExport,
	    String dateHeader, boolean displaySheetTitle) throws IOException {
	
	Font defaultFont = workbook.createFont();
	defaultFont.setFontName(DEFAULT_FONT_NAME);

	//create default style with default font name
	ExcelUtil.defaultStyle = workbook.createCellStyle();
	ExcelUtil.defaultStyle.setFont(defaultFont);

	//create bold style
	boldStyle = workbook.createCellStyle();
	Font boldFont = workbook.createFont();
	boldFont.setBold(true);
	boldFont.setFontName(DEFAULT_FONT_NAME);
	boldStyle.setFont(boldFont);

	//create color style
	blueColor = workbook.createCellStyle();
	blueColor.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
	blueColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	blueColor.setFont(defaultFont);
	redColor = workbook.createCellStyle();
	redColor.setFillForegroundColor(IndexedColors.RED.getIndex());
	redColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	redColor.setFont(defaultFont);
	greenColor = workbook.createCellStyle();
	greenColor.setFillForegroundColor(IndexedColors.LIME.getIndex());
	greenColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	Font whiteFont = workbook.createFont();
	whiteFont.setColor(IndexedColors.WHITE.getIndex());
	whiteFont.setFontName(DEFAULT_FONT_NAME);
	greenColor.setFont(whiteFont);
	yellowColor = workbook.createCellStyle();
	yellowColor.setFillForegroundColor(IndexedColors.GOLD.getIndex());
	yellowColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	yellowColor.setFont(defaultFont);

	// create number style
	numberStyle = workbook.createCellStyle();
	numberStyle.setDataFormat((short)1); // built-in 1 format - "0"
	
	// create date style
	dateStyle = workbook.createCellStyle();
	dateStyle.setDataFormat((short)14);// built-in 0xe format - "m/d/yy"
	
	// create time style
	timeStyle = workbook.createCellStyle();
	timeStyle.setDataFormat((short)19);// built-in 0x13 format - "h:mm:ss AM/PM"
	
	// create percentage style
	percentageStyle = workbook.createCellStyle();
	short percentageDataFormatId = workbook.createDataFormat().getFormat(FORMAT_PERCENTAGE);
	percentageStyle.setDataFormat(percentageDataFormatId);

	//create border style
	borderStyleLeftThin = workbook.createCellStyle();
	borderStyleLeftThin.setBorderLeft(BorderStyle.THIN);
	borderStyleLeftThin.setFont(defaultFont);
	borderStyleLeftThick = workbook.createCellStyle();
	borderStyleLeftThick.setBorderLeft(BorderStyle.THICK);
	borderStyleLeftThick.setFont(defaultFont);
	borderStyleRightThick = workbook.createCellStyle();
	borderStyleRightThick.setBorderRight(BorderStyle.THICK);
	borderStyleRightThick.setFont(defaultFont);
	borderStyleLeftThinBoldFont = workbook.createCellStyle();
	borderStyleLeftThinBoldFont.setBorderLeft(BorderStyle.THIN);
	borderStyleLeftThinBoldFont.setFont(boldFont);
	borderStyleLeftThickBoldFont = workbook.createCellStyle();
	borderStyleLeftThickBoldFont.setBorderLeft(BorderStyle.THICK);
	borderStyleLeftThickBoldFont.setFont(boldFont);
	borderStyleRightThickBoldFont = workbook.createCellStyle();
	borderStyleRightThickBoldFont.setBorderRight(BorderStyle.THICK);
	borderStyleRightThickBoldFont.setFont(boldFont);
	borderStyleBottomThin = workbook.createCellStyle();
	borderStyleBottomThin.setBorderBottom(BorderStyle.THIN);
	borderStyleBottomThin.setFont(defaultFont);
	borderStyleBottomThinBoldFont = workbook.createCellStyle();
	borderStyleBottomThinBoldFont.setBorderBottom(BorderStyle.THIN);
	borderStyleBottomThinBoldFont.setFont(boldFont);

	borderStyleLeftThinPercentage = workbook.createCellStyle();
	borderStyleLeftThinPercentage.setBorderLeft(BorderStyle.THIN);
	borderStyleLeftThinPercentage.setFont(defaultFont);
	borderStyleLeftThinPercentage.setDataFormat(percentageDataFormatId);
	borderStyleLeftThinBoldFontPercentage = workbook.createCellStyle();
	borderStyleLeftThinBoldFontPercentage.setBorderLeft(BorderStyle.THIN);
	borderStyleLeftThinBoldFontPercentage.setFont(boldFont);
	borderStyleLeftThinBoldFontPercentage.setDataFormat(percentageDataFormatId);

	borderStyleRightThickPercentage = workbook.createCellStyle();
	borderStyleRightThickPercentage.setBorderRight(BorderStyle.THICK);
	borderStyleRightThickPercentage.setDataFormat(percentageDataFormatId);
	borderStyleRightThickBoldFontPercentage = workbook.createCellStyle();
	borderStyleRightThickBoldFontPercentage.setBorderRight(BorderStyle.THICK);
	borderStyleRightThickBoldFontPercentage.setFont(boldFont);
	borderStyleRightThickBoldFontPercentage.setDataFormat(percentageDataFormatId);
	
	int i = 0;
	for (String sheetName : dataToExport.keySet()) {
	    if (dataToExport.get(sheetName) != null) {
		String sheetTitle = (displaySheetTitle) ? sheetName : null;
		ExcelUtil.createSheet(workbook, sheetName, sheetTitle, i, dateHeader, dataToExport.get(sheetName));
		i++;
	    }
	}

	workbook.write(out);
	out.close();
    }

    public static void createSheet(Workbook workbook, String sheetName, String sheetTitle, int sheetIndex,
	    String dateHeader, ExcelCell[][] data) throws IOException {
	// Modify sheet name if required. It should contain only allowed letters and sheets are not allowed with
	// the same names (case insensitive)
	sheetName = WorkbookUtil.createSafeSheetName(sheetName);
	while (workbook.getSheet(sheetName) != null) {
	    sheetName += " ";
	}

	Sheet sheet = workbook.createSheet(sheetName);
	//make sure columns are tracked prior to auto-sizing them
	if (workbook instanceof SXSSFWorkbook) {
	    ((SXSSFSheet)sheet).trackAllColumnsForAutoSizing();
	}

	// Print title in bold, if needed
	if (!StringUtils.isBlank(sheetTitle)) {
	    Row row = sheet.createRow(0);
	    ExcelUtil.createCell(new ExcelCell(sheetTitle, true), 0, row, workbook);
	}

	// Print current date, if needed
	if (!StringUtils.isBlank(dateHeader)) {
	    Row row = sheet.createRow(1);
	    ExcelUtil.createCell(new ExcelCell(dateHeader, false), 0, row, workbook);

	    ExcelUtil.createCell(new ExcelCell(FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT.format(new Date()), false), 1, row, workbook);
	}

	if (data != null) {
	    int maxColumnSize = 0;

	    // Print data
	    for (int rowIndex = 0; rowIndex < data.length; rowIndex++) {

		// in case there is a sheet title or dateHeader available start from 4th row
		int rowIndexOffset = (StringUtils.isBlank(sheetTitle) && StringUtils.isBlank(dateHeader)) ? 0 : 4;

		Row row = sheet.createRow(rowIndex + rowIndexOffset);

		int columnSize = data[rowIndex].length;
		for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
		    ExcelCell excelCell = data[rowIndex][columnIndex];
		    ExcelUtil.createCell(excelCell, columnIndex, row, workbook);
		}

		//calculate max column size
		if (columnSize > maxColumnSize) {
		    maxColumnSize = columnSize;
		}
	    }

	    //autoSizeColumns
	    for (int i = 0; i < maxColumnSize; i++) {
		sheet.autoSizeColumn(i);
	    }
	}
    }

    public static void createCell(ExcelCell excelCell, int cellnum, Row row, Workbook workbook) {
	if (excelCell != null) {
	    Cell cell = row.createCell(cellnum);
	    if (excelCell.getCellValue() != null && excelCell.getCellFormat() == ExcelCell.CELL_FORMAT_TIME
		    && excelCell.getCellValue() instanceof Date) {
		cell.setCellValue((Date) excelCell.getCellValue());
		
	    } else if (excelCell.getCellValue() != null && excelCell.getCellValue() instanceof Date) {
		cell.setCellValue(FileUtil.EXPORT_TO_SPREADSHEET_CELL_DATE_FORMAT.format(excelCell.getCellValue()));
		
	    } else if (excelCell.getCellValue() != null && excelCell.getCellValue() instanceof java.lang.Double) {
		cell.setCellValue((Double) excelCell.getCellValue());
		
	    } else if (excelCell.getCellValue() != null && excelCell.getCellValue() instanceof java.lang.Long) {
		cell.setCellValue(((Long) excelCell.getCellValue()).doubleValue());
		
	    } else if (excelCell.getCellValue() != null && excelCell.getCellValue() instanceof java.lang.Integer) {
		cell.setCellValue(((Integer) excelCell.getCellValue()).doubleValue());
		
	    } else if (excelCell.getCellValue() != null) {
		cell.setCellValue(excelCell.getCellValue().toString());
	    }

	    //set default font
	    cell.setCellStyle(defaultStyle);

	    if (excelCell.isBold()) {
		cell.setCellStyle(boldStyle);
	    }
	    
	    boolean isPercentageFormat = excelCell.getCellFormat() == ExcelCell.CELL_FORMAT_PERCENTAGE;
	    switch (excelCell.getCellFormat()) {
		case 0:
		    //default - do nothing
		    break;
		case ExcelCell.CELL_FORMAT_NUMBER:
		    cell.setCellStyle(numberStyle);
		    break;
		case ExcelCell.CELL_FORMAT_DATE:
		    cell.setCellStyle(dateStyle);
		    break;
		case ExcelCell.CELL_FORMAT_TIME:
		    cell.setCellStyle(timeStyle);
		    break;
		case ExcelCell.CELL_FORMAT_PERCENTAGE:
		    cell.setCellStyle(percentageStyle);
		    break;
	    }

	    if (excelCell.getColor() != null) {
		switch (excelCell.getColor()) {
		    case BLUE:
			cell.setCellStyle(blueColor);
			break;
		    case GREEN:
			cell.setCellStyle(greenColor);
			break;
		    case RED:
			cell.setCellStyle(redColor);
			break;
		    case YELLOW:
			cell.setCellStyle(yellowColor);
			break;
		    default:
			break;
		}
	    }

	    if (excelCell.getBorderStyle() != 0) {

		switch (excelCell.getBorderStyle()) {
		    case ExcelCell.BORDER_STYLE_LEFT_THIN:
			if (excelCell.isBold() && isPercentageFormat) {
			    cell.setCellStyle(borderStyleLeftThinBoldFontPercentage);
			} else if (excelCell.isBold()) {
			    cell.setCellStyle(borderStyleLeftThinBoldFont);
			} else if (isPercentageFormat) {
			    cell.setCellStyle(borderStyleLeftThinPercentage);
			} else {
			    cell.setCellStyle(borderStyleLeftThin);
			}
			break;
		    case ExcelCell.BORDER_STYLE_LEFT_THICK:
			if (excelCell.isBold()) {
			    cell.setCellStyle(borderStyleLeftThickBoldFont);
			} else {
			    cell.setCellStyle(borderStyleLeftThick);
			}
			break;
		    case ExcelCell.BORDER_STYLE_RIGHT_THICK:
			if (excelCell.isBold() && isPercentageFormat ) {
			    cell.setCellStyle(borderStyleRightThickBoldFontPercentage);
			} else 	if (excelCell.isBold() ) {
			    cell.setCellStyle(borderStyleRightThickBoldFont);
			} else 	if (isPercentageFormat ) {
			    cell.setCellStyle(borderStyleRightThickPercentage);
			} else {
			    cell.setCellStyle(borderStyleRightThick);
			}
			break;
		    case ExcelCell.BORDER_STYLE_BOTTOM_THIN:
			if (excelCell.isBold()) {
			    cell.setCellStyle(borderStyleBottomThinBoldFont);
			} else {
			    cell.setCellStyle(borderStyleBottomThin);
			}
			break;
		    default:
			break;
		}

	    }
	    
	    if (excelCell.getAlignment() != 0) {
		switch (excelCell.getAlignment()) {
		    case ExcelCell.ALIGN_GENERAL:
			CellUtil.setCellStyleProperty(cell, CellUtil.ALIGNMENT, HorizontalAlignment.GENERAL);
			break;
		    case ExcelCell.ALIGN_LEFT:
			CellUtil.setCellStyleProperty(cell, CellUtil.ALIGNMENT, HorizontalAlignment.LEFT);
			break;
		    case ExcelCell.ALIGN_CENTER:
			CellUtil.setCellStyleProperty(cell, CellUtil.ALIGNMENT, HorizontalAlignment.CENTER);
			break;
		    case ExcelCell.ALIGN_RIGHT:
			CellUtil.setCellStyleProperty(cell, CellUtil.ALIGNMENT, HorizontalAlignment.RIGHT);
			break;
		    default:
			break;
		}

	    }
	}
    }

}
