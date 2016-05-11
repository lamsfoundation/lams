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

import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * Bean object holding necessary data for excel export.
 */
public class ExcelCell {

    public final static int BORDER_STYLE_LEFT_THIN = 1;
    public final static int BORDER_STYLE_RIGHT_THICK = 2;

    private Object cellValue;
    private Boolean isBold;
    private IndexedColors color;
    private int borderStyle = 0;

    public ExcelCell() {
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
}
