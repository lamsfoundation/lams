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

import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * Bean object holding necessary data for excel export.
 */
public class ExcelCell {

    public final static int BORDER_STYLE_LEFT_THIN = 1;
    public final static int BORDER_STYLE_LEFT_THICK = 4;
    public final static int BORDER_STYLE_RIGHT_THICK = 2;
    public final static int BORDER_STYLE_BOTTOM_THIN = 3;

    public final static int ALIGN_GENERAL = 1;
    public final static int ALIGN_LEFT = 2;
    public final static int ALIGN_CENTER = 3;
    public final static int ALIGN_RIGHT = 4;
    
    public final static int CELL_FORMAT_DEFAULT = 0;
    public final static int CELL_FORMAT_DATE = 1;
    public final static int CELL_FORMAT_TIME = 2;
    public final static int CELL_FORMAT_PERCENTAGE = 3;
    
    private Object cellValue;
    private int dataFormat = ExcelCell.CELL_FORMAT_DEFAULT;//default format is 0
    private Boolean isBold = false;
    private IndexedColors color;
    private int borderStyle = 0;
    private int alignment = 0;

    public ExcelCell() {
    }
    
    public ExcelCell(Object cellValue) {
	this.cellValue = cellValue;
    }

    public ExcelCell(Object cellValue, Boolean isBold) {
	this.cellValue = cellValue;
	this.isBold = isBold;
    }

    public ExcelCell(Object cellValue, IndexedColors color) {
	this.cellValue = cellValue;
	this.isBold = false;
	this.color = color;
    }

    public ExcelCell(Object cellValue, int borderStyle) {
	this.cellValue = cellValue;
	this.isBold = false;
	this.borderStyle = borderStyle;
    }

    public ExcelCell(Object cellValue, Boolean isBold, int borderStyle) {
	this.cellValue = cellValue;
	this.isBold = isBold;
	this.borderStyle = borderStyle;
    }

    public Object getCellValue() {
	return cellValue;
    }

    public void setCellValue(Object cellValue) {
	this.cellValue = cellValue;
    }
    
    public int getDataFormat() {
	return dataFormat;
    }

    public void setDataFormat(int cellFormat) {
	this.dataFormat = cellFormat;
    }

    public Boolean isBold() {
	return isBold;
    }

    public void setIsBold(Boolean isBold) {
	this.isBold = isBold;
    }

     public IndexedColors getColor() {
	return color;
    }

    public void setColor(IndexedColors color) {
	this.color = color;
    }

    public int getBorderStyle() {
	return borderStyle;
    }

    public void setBorderStyle(int borderStyle) {
	this.borderStyle = borderStyle;
    }

    public int getAlignment() {
	return alignment;
    }

    // return the current cell to allow chaining.
    public ExcelCell setAlignment(int alignment) {
	this.alignment = alignment;
	return this;
    }
}
