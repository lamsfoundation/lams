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

package org.lamsfoundation.lams.util.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.util.LocaleUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Utilities for producing .xlsx files.
 */
public class ExcelUtil {
    //other built in formats https://poi.apache.org/apidocs/dev/org/apache/poi/ss/usermodel/BuiltinFormats.html
    private static final String FORMAT_PERCENTAGE = "0%";
    private static short numberFormat;
    private static short floatFormat;
    private static short dateFormat;
    private static short timeFormat;
    private static short percentageFormat;

    private static CellStyle defaultStyle;
    private static CellStyle boldStyle;

    private static CellStyle greenColor;
    private static CellStyle blueColor;
    private static CellStyle redColor;
    private static CellStyle yellowColor;

    private static CellStyle borderStyleLeftThin;
    private static CellStyle borderStyleLeftThick;
    private static CellStyle borderStyleRightThick;
    private static CellStyle borderStyleLeftThinBoldFont;
    private static CellStyle borderStyleLeftThickBoldFont;
    private static CellStyle borderStyleRightThickBoldFont;
    private static CellStyle borderStyleBottomThin;
    private static CellStyle borderStyleBottomThinBoldFont;

    public final static String DEFAULT_FONT_NAME = "Calibri-Regular";

    private final static int MAX_CELL_TEXT_LENGTH = 32767;

    /**
     * Create .xlsx file out of provided data and then write out it to an OutputStream. It will be saved with the .xlsx
     * extension.
     *
     * @param out
     *            output stream to which the file written; usually taken from HTTP response
     * @param sheets
     *            list of sheets to print out
     * @param dateHeader
     *            text describing current date; if <code>NULL</code> then no date is printed; if not <code>NULL</code>
     *            then text is written out along with current date in the cell; the date is formatted according to
     *            {@link #EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT}
     * @param displaySheetTitle
     *            whether to display title (printed in the first (0,0) cell)
     * @throws IOException
     */
    public static void createExcel(OutputStream out, List<ExcelSheet> sheets, String dateHeader,
	    boolean displaySheetTitle) throws IOException {
	ExcelUtil.createExcel(out, sheets, dateHeader, displaySheetTitle, true);
    }

    /**
     * Creates Excel file based on the provided data and writes it out to an OutputStream.
     *
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
     *
     * @param produceXlsxFile
     *            whether excel file should be of .xlsx or .xls format. Use .xls only if you want to read the file back
     *            in again afterwards.
     * @throws IOException
     */
    public static void createExcel(OutputStream out, List<ExcelSheet> sheets, String dateHeader,
	    boolean displaySheetTitle, boolean produceXlsxFile) throws IOException {
	//set user time zone, which is required for outputting cells of time format
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	TimeZone userTimeZone = user.getTimeZone();
	LocaleUtil.setUserTimeZone(userTimeZone);

	//in case .xlsx is requested use SXSSFWorkbook.class (which keeps 100 rows in memory, exceeding rows will be flushed to disk)
	Workbook workbook = produceXlsxFile ? new SXSSFWorkbook(100) : new HSSFWorkbook();
	ExcelUtil.initStyles(workbook);

	for (ExcelSheet sheet : sheets) {
	    ExcelUtil.createSheet(workbook, sheet, dateHeader, displaySheetTitle);
	}

	workbook.write(out);
	out.close();
    }

    private static void initStyles(Workbook workbook) {
	Font defaultFont = workbook.createFont();
	defaultFont.setFontName(DEFAULT_FONT_NAME);

	//create default style with default font name
	defaultStyle = workbook.createCellStyle();
	defaultStyle.setFont(defaultFont);

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

	// create data formats
	floatFormat = workbook.createDataFormat().getFormat("0.00");
	numberFormat = workbook.createDataFormat().getFormat("0");
	dateFormat = (short) 14;// built-in 0xe format - "m/d/yy"
	timeFormat = (short) 19;// built-in 0x13 format - "h:mm:ss AM/PM"
	percentageFormat = workbook.createDataFormat().getFormat(FORMAT_PERCENTAGE);

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
    }

    private static void createSheet(Workbook workbook, ExcelSheet excelSheet, String dateHeader,
	    boolean displaySheetTitle) throws IOException {
	// Modify sheet name if required. It should contain only allowed letters and sheets are not allowed with
	// the same names (case insensitive)
	String sheetName = WorkbookUtil.createSafeSheetName(excelSheet.getSheetName());
	while (workbook.getSheet(sheetName) != null) {
	    sheetName += " ";
	}

	Sheet sheet = workbook.createSheet(sheetName);
	//make sure columns are tracked prior to auto-sizing them
	if (workbook instanceof SXSSFWorkbook) {
	    ((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
	}

	// Print title if requested
	boolean isTitleToBePrinted = displaySheetTitle && StringUtils.isNotBlank(excelSheet.getSheetName());
	if (isTitleToBePrinted) {
	    ExcelRow excelRow = new ExcelRow();
	    excelRow.addCell(excelSheet.getSheetName(), true);
	    ExcelUtil.createRow(excelRow, 0, sheet);
	}

	// Print current date, if needed
	if (StringUtils.isNotBlank(dateHeader)) {
	    ExcelRow excelRow = new ExcelRow();
	    excelRow.addCell(dateHeader);
	    excelRow.addCell(FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT.format(new Date()));
	    ExcelUtil.createRow(excelRow, 1, sheet);
	}

	int maxCellsNumber = 0;

	// Print data
	for (int rowIndex = 0; rowIndex < excelSheet.getRows().size(); rowIndex++) {
	    ExcelRow excelRow = excelSheet.getRow(rowIndex);

	    // in case there is a sheet title or dateHeader available start from 4th row
	    int rowIndexOffset = !isTitleToBePrinted && StringUtils.isBlank(dateHeader) ? 0 : 4;
	    ExcelUtil.createRow(excelRow, rowIndex + rowIndexOffset, sheet);

	    //calculate max column size
	    int cellsNumber = excelRow.getCells().size();
	    if (cellsNumber > maxCellsNumber) {
		maxCellsNumber = cellsNumber;
	    }
	}

	// merge cells
	for (CellRangeAddress mergedCells : excelSheet.mergedCells) {
	    sheet.addMergedRegion(mergedCells);
	}

	//autoSizeColumns
	for (int i = 0; i < maxCellsNumber; i++) {
	    sheet.autoSizeColumn(i);
	}
    }

    private static void createRow(ExcelRow excelRow, int rowIndex, Sheet sheet) {
	Row row = sheet.createRow(rowIndex);

	int columnIndex = 0;
	for (ExcelCell excelCell : excelRow.getCells()) {
	    if (excelCell == null) {
		continue;
	    }

	    Cell cell = row.createCell(columnIndex++);
	    Object excelCellValue = excelCell.getCellValue();

	    //cast excelCell's value
	    if (excelCellValue != null) {
		if (excelCell.getDataFormat() == ExcelCell.CELL_FORMAT_TIME && excelCellValue instanceof Date) {
		    cell.setCellValue((Date) excelCellValue);

		} else if (excelCellValue instanceof Date) {
		    cell.setCellValue(FileUtil.EXPORT_TO_SPREADSHEET_CELL_DATE_FORMAT.format(excelCellValue));

		} else if (excelCellValue instanceof java.lang.Float) {
		    cell.setCellValue((Float) excelCellValue);

		} else if (excelCellValue instanceof java.lang.Double) {
		    cell.setCellValue((Double) excelCellValue);

		} else if (excelCellValue instanceof java.lang.Long) {
		    cell.setCellValue(((Long) excelCellValue).doubleValue());

		} else if (excelCellValue instanceof java.lang.Integer) {
		    cell.setCellValue(((Integer) excelCellValue).doubleValue());

		} else {
		    cell.setCellValue(ExcelUtil.ensureCorrectCellLength(excelCellValue.toString()));
		}
	    }

	    //figure out cell's style
	    CellStyle cellStyle = defaultStyle;
	    if (excelCell.isBold() || excelRow.isBold()) {
		cellStyle = boldStyle;
	    }
	    if (excelCell.getColor() != null) {
		switch (excelCell.getColor()) {
		    case BLUE:
			cellStyle = blueColor;
			break;
		    case GREEN:
			cellStyle = greenColor;
			break;
		    case RED:
			cellStyle = redColor;
			break;
		    case YELLOW:
			cellStyle = yellowColor;
			break;
		    default:
			break;
		}
	    }
	    if (excelCell.getBorderStyle() != 0) {
		switch (excelCell.getBorderStyle()) {
		    case ExcelCell.BORDER_STYLE_LEFT_THIN:
			if (excelCell.isBold()) {
			    cellStyle = borderStyleLeftThinBoldFont;
			} else {
			    cellStyle = borderStyleLeftThin;
			}
			break;
		    case ExcelCell.BORDER_STYLE_LEFT_THICK:
			if (excelCell.isBold()) {
			    cellStyle = borderStyleLeftThickBoldFont;
			} else {
			    cellStyle = borderStyleLeftThick;
			}
			break;
		    case ExcelCell.BORDER_STYLE_RIGHT_THICK:
			if (excelCell.isBold()) {
			    cellStyle = borderStyleRightThickBoldFont;
			} else {
			    cellStyle = borderStyleRightThick;
			}
			break;
		    case ExcelCell.BORDER_STYLE_BOTTOM_THIN:
			if (excelCell.isBold()) {
			    cellStyle = borderStyleBottomThinBoldFont;
			} else {
			    cellStyle = borderStyleBottomThin;
			}
			break;
		    default:
			break;
		}
	    }
	    cell.setCellStyle(cellStyle);

	    //set data format
	    if (excelCellValue != null
		    && (excelCellValue instanceof java.lang.Integer || excelCellValue instanceof java.lang.Long)) {
		CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, numberFormat);

	    } else if (excelCellValue != null
		    && (excelCellValue instanceof java.lang.Float || excelCellValue instanceof java.lang.Double)) {
		CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, floatFormat);

	    } else if (excelCell.getDataFormat() == ExcelCell.CELL_FORMAT_DATE) {
		CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, dateFormat);

	    } else if (excelCell.getDataFormat() == ExcelCell.CELL_FORMAT_TIME) {
		CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, timeFormat);
	    }
	    if (excelCell.getDataFormat() == ExcelCell.CELL_FORMAT_PERCENTAGE) {
		CellUtil.setCellStyleProperty(cell, CellUtil.DATA_FORMAT, percentageFormat);
	    }

	    //set alignment
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

    public static String ensureCorrectCellLength(String cellText) {
	return cellText.length() > MAX_CELL_TEXT_LENGTH ? cellText.substring(0, MAX_CELL_TEXT_LENGTH) : cellText;
    }

}
