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
package org.lamsfoundation.lams.tool.assessment.dto;

import java.util.List;

public class JQGridJSONModel {

    private String page;
    private String total;
    private Integer records;
    private List<JQGridRow> rows;

    /**
     * @return the page
     */
    public String getPage() {
	return page;
    }

    /**
     * @param page
     *            the page to set
     */
    public void setPage(String page) {
	this.page = page;
    }

    /**
     * @return the total
     */
    public String getTotal() {
	return total;
    }

    /**
     * @param total
     *            the total to set
     */
    public void setTotal(String total) {
	this.total = total;
    }

    /**
     * @return the records
     */
    public Integer getRecords() {
	return records;
    }

    /**
     * @param records
     *            the records to set
     */
    public void setRecords(Integer records) {
	this.records = records;
    }

    /**
     * @return the rows
     */
    public List<JQGridRow> getRows() {
	return rows;
    }

    /**
     * @param rows
     *            the rows to set
     */
    public void setRows(List<JQGridRow> rows) {
	this.rows = rows;
    }

}
