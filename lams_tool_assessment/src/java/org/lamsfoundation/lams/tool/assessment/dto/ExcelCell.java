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
package org.lamsfoundation.lams.tool.assessment.dto;

/**
 * A small bean object representing an excel cell for export
 * @author lfoxton
 *
 */
public class ExcelCell {
    private Object cellValue;
    private boolean bold;

    public ExcelCell() {
    }

    public ExcelCell(Object cellValue, boolean bold) {
	super();
	this.cellValue = cellValue;
	this.bold = bold;
    }

    public Object getCellValue() {
	return cellValue;
    }

    public void setCellValue(Object cellValue) {
	this.cellValue = cellValue;
    }

    public boolean isBold() {
	return bold;
    }

    public void setBold(boolean bold) {
	this.bold = bold;
    }
}
